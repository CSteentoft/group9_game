package org.example;

import com.badlogic.gdx.math.Rectangle;

public class CollisionHandler {

    public CollisionHandler() {

    }

    public boolean checkAABBCollision(Rectangle A, Rectangle B) {
        return (A.x < B.x + B.width) &&
            (A.x + A.width > B.x) &&
            (A.y < B.y + B.height) &&
            (A.y + A.height > B.y);
    }

    public SweptCollisionResult sweptAABB(Rectangle moving, float dx, float dy, Rectangle staticRect) {
        float xInvEntry, yInvEntry;
        float xInvExit, yInvExit;

        if (dx > 0.0f) {
            xInvEntry = staticRect.x - (moving.x + moving.width);
            xInvExit  = (staticRect.x + staticRect.width) - moving.x;
        } else {
            xInvEntry = (staticRect.x + staticRect.width) - moving.x;
            xInvExit  = staticRect.x - (moving.x + moving.width);
        }

        if (dy > 0.0f) {
            yInvEntry = staticRect.y - (moving.y + moving.height);
            yInvExit  = (staticRect.y + staticRect.height) - moving.y;
        } else {
            yInvEntry = (staticRect.y + staticRect.height) - moving.y;
            yInvExit  = staticRect.y - (moving.y + moving.height);
        }

        float xEntry, yEntry;
        float xExit, yExit;

        if (dx == 0.0f) {
            xEntry = Float.NEGATIVE_INFINITY;
            xExit = Float.POSITIVE_INFINITY;
        } else {
            xEntry = xInvEntry / dx;
            xExit = xInvExit / dx;
        }

        if (dy == 0.0f) {
            yEntry = Float.NEGATIVE_INFINITY;
            yExit = Float.POSITIVE_INFINITY;
        } else {
            yEntry = yInvEntry / dy;
            yExit = yInvExit / dy;
        }

        float entryTime = Math.max(xEntry, yEntry);
        float exitTime  = Math.min(xExit, yExit);

        // Debug logs to inspect the computed times.
        com.badlogic.gdx.Gdx.app.log("SweptDebug", "xEntry: " + xEntry + " yEntry: " + yEntry + " entryTime: " + entryTime + " exitTime: " + exitTime);

        // Adjusted condition: treat any collision that starts before time 0 or after 1 as non-colliding.
        if (entryTime > exitTime || entryTime < 0.0f || entryTime > 1.0f) {
            return null;
        }

        // Determine collision normal.
        float normalX = 0, normalY = 0;
        if (xEntry > yEntry) {
            normalX = (xInvEntry < 0) ? 1 : -1;
        } else {
            normalY = (yInvEntry < 0) ? 1 : -1;
        }
        com.badlogic.gdx.Gdx.app.log("SweptDebug", "Collision normal: (" + normalX + ", " + normalY + ")");
        return new SweptCollisionResult(entryTime, normalX, normalY);
    }

}
