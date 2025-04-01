package org.render.RenderingFactory;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import org.render.components.RenderingComponent;

/**
 * Creates a single RenderingComponent from a sprite sheet.
 * You can call this multiple times for idle/run/jump animations, etc.
 */
public class AnimationFactory {

    public static RenderingComponent createRenderingComponent(
        String spriteSheetPath,
        int frameCols,
        int frameRows,
        float frameDuration
    ) {
        RenderingComponent rc = new RenderingComponent();
        rc.frameDuration = frameDuration;
        rc.spriteSheet = new Texture(spriteSheetPath);

        TextureRegion[][] tmp = TextureRegion.split(
            rc.spriteSheet,
            rc.spriteSheet.getWidth() / frameCols,
            rc.spriteSheet.getHeight() / frameRows
        );

        rc.frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                rc.frames[index++] = tmp[i][j];
            }
        }

        rc.animation = new Animation<>(rc.frameDuration, rc.frames);
        return rc;
    }
}

