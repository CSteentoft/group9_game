package io.github.group9;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CoreResources {
    private static SpriteBatch batch;
    private static Vector2 getPlayerPosition = new Vector2(0, 0);

    public static void setSpriteBatch(SpriteBatch spriteBatch) {
        batch = spriteBatch;
    }

    public static SpriteBatch getSpriteBatch() {
        return batch;
    }

    public static Vector2 getPlayerPosition() {
        return getPlayerPosition;
    }

    public static void setPlayerPosition(Vector2 vec) {
        getPlayerPosition = vec;

    }
}


