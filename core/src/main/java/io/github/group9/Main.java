package io.github.group9;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture walkSheet;
    private Animation<TextureRegion> walkAnimation;
    private float stateTime;

    private static final int FRAME_COLS = 8, FRAME_ROWS = 1; // Adjust based on your sprite sheet

    @Override
    public void create() {
        batch = new SpriteBatch();
        walkSheet = new Texture(Gdx.files.internal("Player_run.png")); // Make sure this exists

        // Split the sprite sheet into individual frames
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
            walkSheet.getWidth() / FRAME_COLS,
            walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize animation with frame interval
        walkAnimation = new Animation<>(.066f, walkFrames);
        stateTime = 0f;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        stateTime += Gdx.graphics.getDeltaTime(); // Track animation time

        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true); // Loop animation

        batch.begin();
        batch.draw(currentFrame, 140, 210);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        walkSheet.dispose();
    }
}
