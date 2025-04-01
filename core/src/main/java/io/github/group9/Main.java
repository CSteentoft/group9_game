package io.github.group9;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import org.common.Services.ECSPlugin;
import io.github.group9.CoreResources;
import org.example.GameCamera;
import org.example.GameMap;

import java.util.ServiceLoader;
import java.util.Vector;

public class Main extends ApplicationAdapter {

    private Engine engine;           // Ashley ECS engine
    private SpriteBatch batch;
    private Music mohamed;
    private GameCamera gameCamera;
    private GameMap gameMap;
    Vector2 playerPosition;

    @Override
    public void create() {
        engine = new Engine();
        batch = new SpriteBatch();
        CoreResources.setSpriteBatch(batch); // Set shared resources in core
        mohamed = Gdx.audio.newMusic(Gdx.files.internal("mohamed.mp3"));

        playerPosition = CoreResources.getPlayerPosition();

        // Set up GameCamera and GameMap
        gameCamera = new GameCamera(640, 360, playerPosition.x, playerPosition.y,  true);
        gameMap = new GameMap(gameCamera.getCamera(), "map/New4.tmx");
        gameMap.generateEntitiesForTiles();
        gameMap.tileMerging();




        // Load ECS plugins via ServiceLoader (PlayerPlugin, CollisionPlugin, etc.)
        ServiceLoader<ECSPlugin> loader = ServiceLoader.load(ECSPlugin.class);
        int count = 0;
        for (ECSPlugin plugin : loader) {
            Gdx.app.log("Main", "Loaded plugin: " + plugin.getClass().getName());
            plugin.registerSystems(engine);
            plugin.createEntities(engine);
            count++;
        }
        if (count == 0) {
            Gdx.app.log("Main", "No ECSPlugin implementations found.");
        }
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(deltaTime);

        gameCamera.setCameraPos(playerPosition.x, playerPosition.y);
        System.out.println(playerPosition);

        gameCamera.getCamera().update();
        batch.setProjectionMatrix(gameCamera.getCamera().combined);

        gameMap.GameMapUpdate();
        gameMap.renderAllCollisionBoxes(batch);
        mohamed.setVolume(0.00f);
        mohamed.play();
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameMap.dispose();
        mohamed.dispose();
    }
}



