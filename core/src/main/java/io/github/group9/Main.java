package io.github.group9;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import org.example.GameCamera;
import org.example.GameMap;
import org.example.Player;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Player player;
    private GameCamera gameCamera;
    private GameMap gameMap;

    @Override
    public void create() {
        player = new Player();
        batch = new SpriteBatch();
        gameCamera = new GameCamera(640, 360, player.getPosition().x, player.getPosition().y, true);
        gameMap = new GameMap(gameCamera.getCamera(), "TEST.tmx");
    }

    @Override
    public void render() {
        // Clear the screen.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera first so that rendering uses the latest camera position.
        if (gameCamera.isFollowingPLayer()){
            gameCamera.setCameraPos(player.getPosition().x, player.getPosition().y);
        }
        gameCamera.getCamera().update();
        batch.setProjectionMatrix(gameCamera.getCamera().combined);

        gameMap.GameMapUpdate();

        batch.begin();
        player.update();
        player.render(batch);
        batch.end();

        player.renderHurtBox(batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameMap.dispose();
        player.dispose();
        // Remove or dispose gameMap if you later use it.
    }
}
