package io.github.group9;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.GameCamera;
import org.example.Player;

public class Main extends ApplicationAdapter {

    private Player player;
    private SpriteBatch batch;
    private GameCamera gameCamera;

    @Override
    public void create() {
        player = new Player();
        batch = new SpriteBatch();
        gameCamera = new GameCamera(640, 360, player.getPosition().x, player.getPosition().y, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Clears the screen with the current background color

        gameCamera.setCameraPos(player.getPosition().x, player.getPosition().y);
        batch.setProjectionMatrix(gameCamera.getCamera().combined);

        batch.begin();
        player.update();  // Update player state (including handling input)
        player.render(batch);  // Render the player's current animation
        batch.end();


    }
    @Override
    public void dispose() {
        batch.dispose();  // Dispose of resources when done
        player.dispose();
    }
}
