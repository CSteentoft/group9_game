package io.github.group9;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ServiceLoader;

public class GameScreen implements Screen {

    private Engine engine;
    private ShapeRenderer shapeRenderer;

    @Override
    public void show() {
        // Create a new Ashley Engine
        engine = new Engine();

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


        // ShapeRenderer for drawing
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update ECS
        engine.update(delta);

        // Render any Entities with a PositionComponent (now in core)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : engine.getEntities()) {
            PositionComponent pos = e.getComponent(PositionComponent.class);
            if (pos != null) {
                // For example, draw a 32x32 rectangle at (x, y)
                shapeRenderer.rect(pos.x, pos.y, 32, 32);
            }
        }
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) { }
    @Override
    public void pause() { }
    @Override
    public void resume() { }
    @Override
    public void hide() { }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}





