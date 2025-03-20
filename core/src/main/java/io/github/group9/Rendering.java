package io.github.group9;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Rendering {
    private Animation<TextureRegion> animation;
    private Texture spriteSheet;
    private float stateTime;
    private float posX, posY; // Position
    private TextureRegion[] frames; // Save frames (for flipping, etc.)
    private boolean flipped; // current horizontal flip state

    // For jump animation external control
    private boolean externalControl;

    public Rendering(String spriteSheetPath, int frameCols, int frameRows, float x, float y) {
        spriteSheet = new Texture(Gdx.files.internal(spriteSheetPath));
        posX = x;
        posY = y;
        flipped = false;
        externalControl = false;

        // Split the sprite sheet into frames
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / frameCols,
            spriteSheet.getHeight() / frameRows);

        frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        // Use a fixed frame duration (0.066f seconds)
        animation = new Animation<>(0.066f, frames);
        stateTime = 0f;
    }

    // Call this to flip the animation horizontally if needed.
    public void setFlip(boolean flip) {
        if (flip != flipped) {
            for (TextureRegion frame : frames) {
                frame.flip(true, false);
            }
            flipped = flip;
        }
    }

    // Allow external code (Main) to override stateTime (for jump animation)
    public void setStateTime(float newTime) {
        stateTime = newTime;
        externalControl = true;
    }

    // When not under external control (for normal animations)
    public void disableExternalControl() {
        externalControl = false;
    }

    // Set the position for rendering
    public void setPosition(float x, float y) {
        this.posX = x;
        this.posY = y;
    }

    public void render(SpriteBatch batch) {
        // If external control is off, update stateTime automatically
        if (!externalControl) {
            stateTime += Gdx.graphics.getDeltaTime();
        }
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, posX, posY);
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
