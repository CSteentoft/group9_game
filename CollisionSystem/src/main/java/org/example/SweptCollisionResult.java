package org.example;

public class SweptCollisionResult {
    public float collisionTime;
    public float normalX;
    public float normalY;

    public SweptCollisionResult(float collisionTime, float normalX, float normalY) {
        this.collisionTime = collisionTime;
        this.normalX = normalX;
        this.normalY = normalY;
    }
}
