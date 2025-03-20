package io.github.group9.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Map {
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;

    // Map dimensions
    private static final float MAP_WIDTH = 800;
    private static final float MAP_HEIGHT = 480;

    // Platform dimensions
    private static final float PLATFORM_HEIGHT = 30;
    private static final float PLATFORM_Y = 100;

    // Boundaries
    private Rectangle leftBoundary;
    private Rectangle rightBoundary;
    private Rectangle platform;
    private Rectangle floor;

    public Map() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

        // Center the camera
        camera.position.set(MAP_WIDTH / 2, MAP_HEIGHT / 2, 0);

        // Create boundaries
        leftBoundary = new Rectangle(0, 0, 10, MAP_HEIGHT);
        rightBoundary = new Rectangle(MAP_WIDTH - 10, 0, 10, MAP_HEIGHT);
        platform = new Rectangle(MAP_WIDTH / 4, PLATFORM_Y, MAP_WIDTH / 2, PLATFORM_HEIGHT);
        floor = new Rectangle(0, 0, MAP_WIDTH, 10);
    }

    public void render() {
        // Clear the screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw map elements
        shapeRenderer.begin(ShapeType.Filled);

        // Draw floor (main fighting area)
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
        shapeRenderer.rect(floor.x, floor.y, floor.width, floor.height);

        // Draw platform
        shapeRenderer.setColor(0.6f, 0.6f, 0.6f, 1);
        shapeRenderer.rect(platform.x, platform.y, platform.width, platform.height);

        // Draw boundaries
        shapeRenderer.setColor(0.8f, 0.2f, 0.2f, 1);
        shapeRenderer.rect(leftBoundary.x, leftBoundary.y, leftBoundary.width, leftBoundary.height);
        shapeRenderer.rect(rightBoundary.x, rightBoundary.y, rightBoundary.width, rightBoundary.height);

        shapeRenderer.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
