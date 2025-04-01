package org.example.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import org.example.components.CollisionComponent;
import org.example.components.SweptCollisionResult;

/**
 * Uses Swept AABB for collision detection among entities that have CollisionComponent.
 * Dynamic entities (isStatic = false) check collisions against static entities (isStatic = true).
 */
public class CollisionSystem extends EntitySystem {

    private final Family family = Family.all(CollisionComponent.class).get();
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float deltaTime) {
        // We'll do a simple double-loop approach:
        // - For each dynamic entity, check collisions against each static entity
        for (int i = 0; i < entities.size(); i++) {
            Entity e1 = entities.get(i);
            CollisionComponent cc1 = e1.getComponent(CollisionComponent.class);
            if (cc1 == null) continue;

            // If it's static, skip checking against other static
            if (cc1.isStatic) continue;

            // This is a dynamic entity => check collisions
            for (int j = 0; j < entities.size(); j++) {
                if (i == j) continue; // skip self

                Entity e2 = entities.get(j);
                CollisionComponent cc2 = e2.getComponent(CollisionComponent.class);
                if (cc2 == null) continue;

                // We only check dynamic vs. static collisions
                if (!cc2.isStatic) continue;

                // Perform swept AABB
                SweptCollisionResult result = sweptAABB(cc1.boundingBox, cc1.dx, cc1.dy, cc2.boundingBox);
                if (result != null) {
                    // We have a collision before the end of this frame
                    // Handle or log it
                    System.out.println("Collision between dynamic entity " + i
                        + " and static entity " + j + ": " + result);

                    // Example resolution: Move entity out of collision at time t
                    float collisionTime = result.time;
                    // Reposition bounding box to the collision moment
                    cc1.boundingBox.x += cc1.dx * collisionTime;
                    cc1.boundingBox.y += cc1.dy * collisionTime;

                    // Optionally zero out velocity along the normal
                    // e.g. if normalX != 0, we stop dx
                    // if normalY != 0, we stop dy
                    if (Math.abs(result.normalX) > 0.5f) {
                        cc1.dx = 0;
                    }
                    if (Math.abs(result.normalY) > 0.5f) {
                        cc1.dy = 0;
                    }
                }
            }
        }
    }

    /**
     * Swept AABB detection
     * Translated from your original code in CollisionHandler.
     *
     * @param moving The bounding box of the moving entity
     * @param dx The horizontal velocity of the moving entity
     * @param dy The vertical velocity of the moving entity
     * @param staticRect The bounding box of the static entity
     * @return SweptCollisionResult or null if no collision within [0..1]
     */
    public SweptCollisionResult sweptAABB(Rectangle moving, float dx, float dy, Rectangle staticRect) {

        // 1) Calculate distances for X axis
        float xEntryDist, xExitDist;
        if (dx > 0) {
            xEntryDist = staticRect.x - (moving.x + moving.width);
            xExitDist = (staticRect.x + staticRect.width) - moving.x;
        } else {
            xEntryDist = (staticRect.x + staticRect.width) - moving.x;
            xExitDist = staticRect.x - (moving.x + moving.width);
        }

        // 2) Calculate distances for Y axis
        float yEntryDist, yExitDist;
        if (dy > 0) {
            yEntryDist = staticRect.y - (moving.y + moving.height);
            yExitDist = (staticRect.y + staticRect.height) - moving.y;
        } else {
            yEntryDist = (staticRect.y + staticRect.height) - moving.y;
            yExitDist = staticRect.y - (moving.y + moving.height);
        }

        // 3) Convert distances to time
        float xEntryTime, xExitTime;
        if (dx == 0) {
            xEntryTime = Float.NEGATIVE_INFINITY;
            xExitTime  = Float.POSITIVE_INFINITY;
        } else {
            xEntryTime = xEntryDist / dx;
            xExitTime  = xExitDist / dx;
        }

        float yEntryTime, yExitTime;
        if (dy == 0) {
            yEntryTime = Float.NEGATIVE_INFINITY;
            yExitTime  = Float.POSITIVE_INFINITY;
        } else {
            yEntryTime = yEntryDist / dy;
            yExitTime  = yExitDist / dy;
        }

        float collisionStart = Math.max(xEntryTime, yEntryTime);
        float collisionEnd   = Math.min(xExitTime, yExitTime);

        // 4) Check if collision is valid
        if (collisionStart > collisionEnd) return null;   // no overlap
        if (collisionStart < 0) return null;              // collision in the past
        if (collisionStart > 1) return null;              // collision after this frame

        // 5) Determine collision side
        float normalX = 0;
        float normalY = 0;

        if (xEntryTime > yEntryTime) {
            // X collision first
            if (xEntryDist < 0) {
                normalX = 1; // Hit left side
            } else {
                normalX = -1; // Hit right side
            }
        } else {
            // Y collision first
            if (yEntryDist < 0) {
                normalY = 1; // Hit bottom
            } else {
                normalY = -1; // Hit top
            }
        }

        return new SweptCollisionResult(collisionStart, normalX, normalY);
    }
}
