package io.github.group9;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.Player;

public class Main extends ApplicationAdapter {

    private Player player;
    private SpriteBatch batch;

    @Override
    public void create() {
        player = new Player();
        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Clears the screen with the current background color
        batch.begin();
        player.update();  // Update player state (including handling input)
        player.render(batch);  // Render the player's current animation
        batch.end();
    }
    @Override
    public void dispose() {
        batch.dispose();  // Dispose of resources when done
    }
}
