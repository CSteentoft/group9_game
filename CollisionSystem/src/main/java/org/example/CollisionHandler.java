package org.example;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

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

    public String getCollisionDirection(Rectangle A, Rectangle B) {

        // If there's no collision, simply return "No collision"
        if (!checkAABBCollision(A, B)) {
            return "No collision";
        }

        // Center points of A
        float centerAx = A.x + A.width / 2;
        float centerAy = A.y + A.height / 2;

        // Center points of B
        float centerBx = B.x + B.width / 2;
        float centerBy = B.y + B.height / 2;

        // Calculate the difference between centers
        float dx = centerAx - centerBx;
        float dy = centerAy - centerBy;

        // Calculate combined half-widths and half-heights
        float halfWidths = (A.width / 2) + (B.width / 2);
        float halfHeights = (A.height / 2) + (B.height / 2);

        // Overlap on the X and Y axes
        float overlapX = halfWidths - Math.abs(dx);
        float overlapY = halfHeights - Math.abs(dy);

        // Whichever overlap is smaller determines the collision side
        if (overlapX < overlapY) {
            // Horizontal collision
            if (dx > 0) {
                return "Collision from RIGHT side of B";
            } else {
                return "Collision from LEFT side of B";
            }
        } else {
            // Vertical collision
            if (dy > 0) {
                return "Collision from TOP of B";
            } else {
                return "Collision from BOTTOM of B";
            }
        }
    }
 
}
