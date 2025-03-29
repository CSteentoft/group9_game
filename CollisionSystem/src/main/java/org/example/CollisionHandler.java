package org.example;

import com.badlogic.gdx.math.Rectangle;

public class CollisionHandler {

    /**
      Detects collisions between a moving and static rectangle using Swept AABB.

     Calculates:
     - Collision Time: When the collision occurs (0.0 = start of movement, 1.0 = end)
     - Collision Normal: Which face was hit (left/right/top/bottom)

     Works by:
     1. Calculating entry/exit distances for X and Y axes based on movement direction
     2. Converting distances to time values using object velocity
     3. Finding the earliest valid collision time between axes
     4. Determining collision side based on dominant axis

     Ensures smooth collision resolution by predicting collisions before they happen.
     **/

    public SweptCollisionResult sweptAABB(Rectangle moving, float dx, float dy, Rectangle staticRect) {

        // Calculate distances for X axis collision
        float xEntryDist, xExitDist;
        if (dx > 0) {
            xEntryDist = staticRect.x - (moving.x + moving.width);  // Moving right: distance to left side of static object

            xExitDist = (staticRect.x + staticRect.width) - moving.x; // Distance to right side of static object
        } else {
            xEntryDist = (staticRect.x + staticRect.width) - moving.x;  // Moving left: distance to right side of static object

            xExitDist = staticRect.x - (moving.x + moving.width);  // Distance to left side of static object
        }

        // Calculate distances for Y axis collision
        float yEntryDist, yExitDist;
        if (dy > 0) {
            yEntryDist = staticRect.y - (moving.y + moving.height);  // Moving up: distance to bottom of static object

            yExitDist = (staticRect.y + staticRect.height) - moving.y;  // Distance to top of static object
        } else {
            yEntryDist = (staticRect.y + staticRect.height) - moving.y; // Moving down: distance to top of static object

            yExitDist = staticRect.y - (moving.y + moving.height);  // Distance to bottom of static object
        }

        // Calculate time until collisions happen
        float xEntryTime, xExitTime, yEntryTime, yExitTime;

        // X-axis entry time
        if (dx == 0) {
            xEntryTime = Float.NEGATIVE_INFINITY; // No horizontal movement
        } else {
            xEntryTime = xEntryDist / dx; // Time until X collision starts
        }

        // X-axis exit time
        if (dx == 0) {
            xExitTime = Float.POSITIVE_INFINITY; // No horizontal movement
        } else {
            xExitTime = xExitDist / dx; // Time until X collision ends
        }

        // Y-axis entry time
        if (dy == 0) {
            yEntryTime = Float.NEGATIVE_INFINITY; // No vertical movement
        } else {
            yEntryTime = yEntryDist / dy; // Time until Y collision starts
        }

        // Y-axis exit time
        if (dy == 0) {
            yExitTime = Float.POSITIVE_INFINITY; // No vertical movement
        } else {
            yExitTime = yExitDist / dy; // Time until Y collision ends
        }

        // Find when collision first happens
        float collisionStart = Math.max(xEntryTime, yEntryTime);

        // Find when collision stops happening
        float collisionEnd = Math.min(xExitTime, yExitTime);

        // Check if collision is valid
        if (collisionStart > collisionEnd) return null; // No overlap
        if (collisionStart < 0) return null; // Collision happened in the past
        if (collisionStart > 1) return null; // Collision happens beyond next frame

        // Determine which side we collided with
        float normalX = 0;
        float normalY = 0;

        if (xEntryTime > yEntryTime) {
            // X-axis collision happened first
            if (xEntryDist < 0) {
                normalX = 1; // Hit left side
            } else {
                normalX = -1; // Hit right side
            }

        } else {
            // Y-axis collision happened first
            if (yEntryDist < 0) {
                normalY = 1; // Hit bottom
            } else {
                normalY = -1; // Hit top
            }
        }

        return new SweptCollisionResult(collisionStart, normalX, normalY);
    }
}
