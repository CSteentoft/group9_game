package org.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Rendering {
    private Animation<TextureRegion> animation;
    private Texture spriteSheet;
    private float stateTime;
    private float frameDuration = 0.066f;
    private float posX, posY; // Position
    private TextureRegion[] frames; // Save frames (for flipping, etc.)
    private boolean flipped; // current horizontal flip state


    public Rendering(String spriteSheetPath, int frameCols, int frameRows, float x, float y) {
        spriteSheet = new Texture(Gdx.files.internal(spriteSheetPath));
        posX = x;
        posY = y;
        flipped = false;

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

        animation = new Animation<>(frameDuration, frames);
        stateTime = 0f;
    }

    public float getAnimationDuration(){
        return animation.getAnimationDuration() - animation.getFrameDuration();
    }
    public float getFRAME_DURATION(){
        return animation.getFrameDuration();
    }

    // Getter for stateTime
    public float getStateTime() {
        return stateTime;
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
    }

    // When not under external control (for normal animations)

    // Set the position for rendering
    public void setPosition(float x, float y) {
        this.posX = x;
        this.posY = y;
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, posX, posY);
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
