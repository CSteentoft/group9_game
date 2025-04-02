package org.render.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;
import java.util.Map;

public class RenderingComponent implements Component {
    private Map<String, Animation<TextureRegion>> animations = new HashMap<>();
    public Animation<TextureRegion> currentAnimation;
    public Texture currentTexture;
    public float stateTime = 0f;
    public float frameDuration = 0.066f;
    public boolean flipped = false;

    // Add a new animation with a unique name
    public void addAnimation(String name, String spriteSheetPath, int frameCols, int frameRows, float frameDuration) {
        Texture texture = new Texture(Gdx.files.internal(spriteSheetPath));
        TextureRegion[][] tmp = TextureRegion.split(texture,
            texture.getWidth() / frameCols,
            texture.getHeight() / frameRows);

        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animations.put(name, animation);

        // Optionally set this animation as the current one
        if (currentAnimation == null) {
            currentAnimation = animation;
            currentTexture = texture;
            this.frameDuration = frameDuration;
        }
    }

    // Switch the current animation
    public void setCurrentAnimation(String name) {
        Animation<TextureRegion> animation = animations.get(name);
        if (animation != null && animation != currentAnimation) {
            currentAnimation = animation;
            setStateTime(0f);
            // Optionally update currentTexture if necessary.
        }
    }

    // Public getter to retrieve an animation by name
    public Animation<TextureRegion> getAnimation(String name) {
        return animations.get(name);
    }

    // Getter and Setter for stateTime
    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float newTime) {
        stateTime = newTime;
    }

    // Flipping method remains similar
    public void setFlip(boolean flip) {
        if (flip != flipped) {
            // Flip all frames for each animation:
            for (Animation<TextureRegion> anim : animations.values()) {
                for (TextureRegion region : anim.getKeyFrames()) {
                    region.flip(true, false);
                }
            }
            flipped = flip;
        }
    }

    private static ShapeRenderer shapeRenderer = new ShapeRenderer();

    public void dispose() {
        // Dispose of the texture. In a more complex setup, track each texture separately.
        currentTexture.dispose();
    }
}

