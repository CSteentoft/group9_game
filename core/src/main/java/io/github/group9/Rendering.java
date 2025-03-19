package io.github.group9;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Rendering {
    private Animation<TextureRegion> animation;
    private Texture spriteSheet;
    private float stateTime;
    private float posX, posY; // Position

    public Rendering(String spriteSheetPath, int frameCols, int frameRows, float x, float y) {
        spriteSheet = new Texture(Gdx.files.internal(spriteSheetPath));
        posX = x;
        posY = y;

        // Split the sprite sheet into frames
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / frameCols,
            spriteSheet.getHeight() / frameRows);

        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        animation = new Animation<>(0.066f, frames);
        stateTime = 0f;
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, posX, posY);
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
