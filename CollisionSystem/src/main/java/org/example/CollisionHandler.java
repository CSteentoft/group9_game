package org.example;

import com.badlogic.gdx.math.Rectangle;

public class CollisionHandler {
    protected Rectangle hitbox;
    protected Rectangle attackBox;
    protected Rectangle collisionBox;

    public CollisionHandler() {
        /*
        hitbox = new Rectangle();
        attackBox = new Rectangle();
        collisionBox = new Rectangle();
         */
    }

    // Basic AABB collision detection
    public boolean checkAABBCollision(Rectangle A, Rectangle B) {
        return (A.x < B.x + B.width) &&
            (A.x + A.width > B.x) &&
            (A.y < B.y + B.height) &&
            (A.y + A.height > B.y);
    }

    public CollisionResult getCollisionData(Rectangle A, Rectangle B) {
        // No overlap => return quickly
        if (!checkAABBCollision(A, B)) {
            return new CollisionResult(false, "No collision", 0, 0);
        }

        // Calculate center points (using float division)
        float centerAx = A.x + A.width / 2.0f;
        float centerAy = A.y + A.height / 2.0f;
        float centerBx = B.x + B.width / 2.0f;
        float centerBy = B.y + B.height / 2.0f;

        // Differences between centers
        float dx = centerAx - centerBx;
        float dy = centerAy - centerBy;

        // Combined half-widths & half-heights
        float halfWidths  = (A.width / 2.0f) + (B.width / 2.0f);
        float halfHeights = (A.height / 2.0f) + (B.height / 2.0f);

        // Calculate overlaps on each axis
        float overlapX = halfWidths - Math.abs(dx);
        float overlapY = halfHeights - Math.abs(dy);

        // Compare which overlap is smaller: that axis is the direction of minimum displacement.
        if (overlapX < overlapY) {
            // Horizontal collision
            if (dx > 0) {
                // A is to the right of B: push A to the right
                return new CollisionResult(true, "Collision from RIGHT side of B", overlapX, 0);
            } else {
                // A is to the left of B: push A to the left
                return new CollisionResult(true, "Collision from LEFT side of B", -overlapX, 0);
            }
        } else {
            // Vertical collision
            // In a coordinate system where y increases downward,
            // if A touches the top of B then A is above B, so dy < 0.
            if (dy < 0) {
                // A is above B, so collision on the top side of B: push A upward.
                return new CollisionResult(true, "Collision from TOP of B", 0, -overlapY);
            } else {
                // A is below B, so collision on the bottom side of B: push A downward.
                return new CollisionResult(true, "Collision from BOTTOM of B", 0, overlapY);
            }
        }
    }


}
