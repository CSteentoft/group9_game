package org.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class Rendering {
    private Animation<TextureRegion> animation;
    private Texture spriteSheet;
    private float stateTime;
    private float frameDuration = 0.066f;
    private float posX, posY; // Position
    private TextureRegion[] frames; // Save frames (for flipping, etc.)
    private boolean flipped; // Current horizontal flip state
    private ShapeRenderer shapeRenderer; // For drawing collision box

    public Rendering(String spriteSheetPath, int frameCols, int frameRows, float x, float y) {
        spriteSheet = new Texture(Gdx.files.internal(spriteSheetPath));
        posX = x;
        posY = y;
        flipped = false;
        shapeRenderer = new ShapeRenderer();

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

    public Rendering() {
        shapeRenderer = new ShapeRenderer();
    }

    public float getAnimationDuration() {
        return animation.getAnimationDuration() - animation.getFrameDuration();
    }

    public float getFRAME_DURATION() {
        return animation.getFrameDuration();
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setFlip(boolean flip) {
        if (flip != flipped) {
            for (TextureRegion frame : frames) {
                frame.flip(true, false);
            }
            flipped = flip;
        }
    }

    public void setStateTime(float newTime) {
        stateTime = newTime;
    }

    public void setPosition(float x, float y) {
        this.posX = x;
        this.posY = y;
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, posX, posY);
    }
    public void drawHurtBox(SpriteBatch batch, Rectangle hurtBox) {
        // Set the ShapeRenderer's projection matrix from the SpriteBatch
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Draw the rectangle using the ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(hurtBox.x, hurtBox.y, hurtBox.width, hurtBox.height);
        shapeRenderer.end();
    }

    public void dispose() {
        spriteSheet.dispose();
        shapeRenderer.dispose();
    }
}
