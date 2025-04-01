package org.render.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Single animation data: sprite sheet, frames, state time, etc.
 */
public class RenderingComponent implements Component {
    public Texture spriteSheet;
    public Animation<TextureRegion> animation;
    public TextureRegion[] frames;
    public float stateTime = 0f;
    public boolean flipped = false;
    public float frameDuration = 0.066f;

    public void flipFrames(boolean flip) {
        if (flip == flipped) return;
        for (TextureRegion frame : frames) {
            frame.flip(true, false);
        }
        flipped = flip;
    }

    public void dispose() {
        if (spriteSheet != null) {
            spriteSheet.dispose();
        }
    }
}

