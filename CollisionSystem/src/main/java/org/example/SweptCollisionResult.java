package org.example;

import com.badlogic.ashley.core.Component;

public class SweptCollisionResult implements Component {
    public float collisionTime;
    public float normalX;
    public float normalY;

    public SweptCollisionResult(float collisionTime, float normalX, float normalY) {
        this.collisionTime = collisionTime;
        this.normalX = normalX;
        this.normalY = normalY;
    }
}
