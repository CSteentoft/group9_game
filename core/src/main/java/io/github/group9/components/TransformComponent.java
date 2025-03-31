package io.github.group9.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component {
    public Vector2 position = new Vector2();
    // We'll store the previous frame's position here
    public Vector2 oldPosition = new Vector2();

    public Vector2 size = new Vector2(32, 32);
    public Rectangle bounds = new Rectangle();

    public void updateBounds() {
        bounds.set(position.x, position.y, size.x, size.y);
    }
}
