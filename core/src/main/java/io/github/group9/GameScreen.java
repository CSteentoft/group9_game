package io.github.group9;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.group9.components.*;
import io.github.group9.systems.CollisionSystem;
// If MovementSystem is also in core, you can import it:
import io.github.group9.systems.MovementSystem;

import java.util.ServiceLoader;

public class GameScreen implements Screen {

    private Engine engine;
    private ShapeRenderer shapeRenderer;

    @Override
    public void show() {
        engine = new Engine();

        // Load ECS plugins (which might be absent if we disable the player module)
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

        // If you keep MovementSystem in core, ensure it's added here (only if plugin doesn't add it)
         MovementSystem moveSystem = new MovementSystem();
         moveSystem.priority = 1;
         engine.addSystem(moveSystem);

        // Always add CollisionSystem in core
        CollisionSystem collisionSystem = new CollisionSystem();
        collisionSystem.priority = 2;
        engine.addSystem(collisionSystem);

        // Create a wall entity (blue)
        Entity wall = new Entity();
        TransformComponent tWall = new TransformComponent();
        tWall.position.set(200, 100);
        tWall.size.set(32, 32);
        tWall.updateBounds();

        wall.add(tWall);
        wall.add(new CollisionComponent());
        wall.add(new WallComponent()); // identifies it as a wall
        engine.addEntity(wall);

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update ECS
        engine.update(delta);

        // Draw shapes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : engine.getEntities()) {
            TransformComponent tc = e.getComponent(TransformComponent.class);
            if (tc != null) {

                // Decide color:
                if (e.getComponent(WallComponent.class) != null) {
                    // It's a wall => Blue
                    shapeRenderer.setColor(Color.BLUE);
                }
                else {
                    // See if there's a TagComponent to color the entity
                    TagComponent tag = e.getComponent(TagComponent.class);
                    if (tag != null && tag.tag.equalsIgnoreCase("player")) {
                        shapeRenderer.setColor(Color.GREEN); // player => green
                    } else {
                        shapeRenderer.setColor(Color.WHITE); // default => white
                    }
                }

                // Draw rectangle
                shapeRenderer.rect(tc.position.x, tc.position.y, tc.size.x, tc.size.y);
            }
        }
        shapeRenderer.end();
    }

    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}




