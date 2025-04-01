package org.example.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

/**
 * Stores bounding box and velocity for collision checks.
 * isStatic = true => the object is immovable (e.g., walls, floors).
 */
public class CollisionComponent implements Component {
    public Rectangle boundingBox = new Rectangle();
    public float dx = 0f;  // velocity on X for this frame
    public float dy = 0f;  // velocity on Y for this frame
    public boolean isStatic = false;
}

