package org.example;

import com.badlogic.gdx.math.Rectangle;

public class CollisionHandler {

    public CollisionResult getCollisionData(Rectangle A, Rectangle B) {
        // No overlap => return quickly
        if (!A.overlaps(B)) {
            return new CollisionResult(false, "No collision", 0, 0);
        }

        // Calculate overlap amounts on each axis
        float overlapX = Math.min(A.x + A.width, B.x + B.width) - Math.max(A.x, B.x);
        float overlapY = Math.min(A.y + A.height, B.y + B.height) - Math.max(A.y, B.y);

        // Determine which axis has the smallest overlap
        if (overlapX < overlapY) {
            // Horizontal collision
            if (A.x < B.x) {
                // Collision from left side
                return new CollisionResult(true, "LEFT", -overlapX, 0);
            } else {
                // Collision from right side
                return new CollisionResult(true, "RIGHT", overlapX, 0);
            }
        } else {
            // Vertical collision
            if (A.y < B.y) {
                // Collision from bottom (player is below tile)
                return new CollisionResult(true, "BOTTOM", 0, -overlapY);
            } else {
                // Collision from top (player is above tile)
                return new CollisionResult(true, "TOP", 0, overlapY);
            }
        }
    }
}
