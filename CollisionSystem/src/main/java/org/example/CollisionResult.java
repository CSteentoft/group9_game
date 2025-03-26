package org.example;

public class CollisionResult {
    public final boolean collided;
    public final String direction;
    public final float overlapX;
    public final float overlapY;

    public CollisionResult(boolean collided, String direction, float overlapX, float overlapY) {
        this.collided = collided;
        this.direction = direction;
        this.overlapX = overlapX;
        this.overlapY = overlapY;
    }
}
