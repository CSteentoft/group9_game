package io.github.group9;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ServiceLoader;

import io.github.group9.components.Map;

public class GameScreen implements Screen {
    private Engine engine;
    private ShapeRenderer shapeRenderer;
    private Map gameMap; // Add the Map component

    @Override
    public void show() {
        engine = new Engine();
        shapeRenderer = new ShapeRenderer();

        // Initialize the map
        gameMap = new Map();

        // Load ECS plugins
        ServiceLoader<ECSPlugin> loader = ServiceLoader.load(ECSPlugin.class);
        int count = 0;
        for (ECSPlugin plugin : loader) {
            Gdx.app.log("GameScreen", "Loaded plugin: " + plugin.getClass().getName());
            plugin.registerSystems(engine);
            plugin.createEntities(engine);
            count++;
        }
        if (count == 0) {
            Gdx.app.log("GameScreen", "No ECSPlugin implementations found.");
        }
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render map
        if (gameMap != null) {
            gameMap.render();
        }

        // Update ECS
        engine.update(delta);

        // Render entities
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : engine.getEntities()) {
            PositionComponent pos = e.getComponent(PositionComponent.class);
            if (pos != null) {
                shapeRenderer.rect(pos.x, pos.y, 32, 32);
            }
        }
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        if (gameMap != null) {
            gameMap.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (gameMap != null) {
            gameMap.dispose();
        }
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
}
