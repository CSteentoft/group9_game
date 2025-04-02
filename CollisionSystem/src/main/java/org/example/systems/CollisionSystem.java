package org.example.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.group9.CoreResources;
import org.common.UserEntity;
import org.common.UserEntityComponent;
import org.example.components.CollisionComponent;
import org.example.components.SweptCollisionResult;

/**
 * Uses Swept AABB for collision detection among entities that have CollisionComponent.
 * Dynamic entities (isStatic = false) check collisions against static entities (isStatic = true).
 */
public class CollisionSystem extends EntitySystem {

    private final Family family = Family.all(CollisionComponent.class).get();

    @Override
    public void addedToEngine(Engine engine) {
    }

    @Override
    public void update(float deltaTime) {
        handleCollisionsSwept(deltaTime);
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

   public void handleCollisionsSwept(float deltaTime) {
        resolveStaticCollisions();

        float remainingTime = 1.0f;
        int maxIterations = 4;
        int iteration = 0;
        Rectangle collidedRect = null;

        while (remainingTime > 0.0f && iteration < maxIterations) {
            iteration++;
            float dx = CoreResources.getVelocityX() * deltaTime * remainingTime;
            //System.out.println(CoreResources.getVelocityX());
            //System.out.println(CoreResources.getVelocityY());
            float dy = CoreResources.getVelocityY() * deltaTime * remainingTime;
            Rectangle playerBox = CoreResources.getPlayerHurtBox();

            // Broad phase check
            Rectangle broadPhaseBox = new Rectangle(
                Math.min(playerBox.x, playerBox.x + dx),
                Math.min(playerBox.y, playerBox.y + dy),
                playerBox.width + Math.abs(dx),
                playerBox.height + Math.abs(dy)
            );

            SweptCollisionResult earliestCollision = null;
            float earliestTime = 1.0f;

            // Narrow phase check
            for (Rectangle rect : CoreResources.getGameMapCollisionBoxes()) {
                if (!broadPhaseBox.overlaps(rect)) continue;

                SweptCollisionResult result = sweptAABB(playerBox, dx, dy, rect);
                if (result != null && result.time < earliestTime) { // Changed to .time
                    earliestCollision = result;
                    earliestTime = result.time; // Changed to .time
                    collidedRect = rect;
                }
            }

            if (earliestCollision != null) {
                // Update position using .time
                CoreResources.setPlayerPosition(new Vector2(
                    CoreResources.getPlayerPosition().x + dx * earliestCollision.time,
                    CoreResources.getPlayerPosition().y + dy * earliestCollision.time
                ));
                //CoreResources.setEnemyPosition(new Vector2(
                    //CoreResources.getEnemyPosition().x + dx * earliestCollision.time,
                    //CoreResources.getEnemyPosition().y + dy * earliestCollision.time
                //));

                if (earliestCollision.normalX != 0) {
                    CoreResources.setNewVelocityX(0);
                    CoreResources.setCollidedX(true);
                }
                if (earliestCollision.normalY != 0) {
                    CoreResources.setVelocityY(0);
                    if (earliestCollision.normalY == 1) {
                        CoreResources.setXLeft(collidedRect.x);
                        CoreResources.setXRight(collidedRect.x + collidedRect.width);
                        CoreResources.setLanded(true);
                    }
                }

                remainingTime *= (1.0f - earliestCollision.time); // Changed to .time
            } else {
                remainingTime = 0.0f;
            }
        }
    }
  private void resolveStaticCollisions() {
        Rectangle playerBox = CoreResources.getPlayerHurtBox();
        float minOverlapThreshold = 0.1f; // Ignore overlaps smaller than this threshold
        float maxOverlap = 0;
        float resolveX = 0;
        float resolveY = 0;
        Rectangle collidedRect = null;

        // Iterate over each collision rectangle
        for (Rectangle rect : CoreResources.getGameMapCollisionBoxes()) {
            if (!playerBox.overlaps(rect)) continue;

            // Calculate overlaps for each side
            float overlapLeft = playerBox.x + playerBox.width - rect.x;
            float overlapRight = rect.x + rect.width - playerBox.x;
            float overlapTop = playerBox.y + playerBox.height - rect.y;
            float overlapBottom = rect.y + rect.height - playerBox.y;

            float minX = Math.min(overlapLeft, overlapRight);
            float minY = Math.min(overlapTop, overlapBottom);
            float depth = Math.min(minX, minY);

            // Only consider correction if the overlap is significant
            if (depth > maxOverlap && depth > minOverlapThreshold) {
                maxOverlap = depth;
                collidedRect = rect;

                if (minX < minY) {
                    resolveX = (overlapLeft < overlapRight) ? -minX : minX;
                    resolveY = 0;
                } else {
                    resolveY = (overlapTop < overlapBottom) ? -minY : minY;
                    resolveX = 0;
                    CoreResources.setXLeft(collidedRect.x);
                    CoreResources.setXRight(collidedRect.x + collidedRect.width);
                }
            }
        }

        if (maxOverlap > 0) {
            // Use a larger epsilon if needed to push the player out enough
            float epsilon = 0.05f;
            Vector2 currentPosition = CoreResources.getPlayerPosition();
            //Vector2 currentPosition = CoreResources.getEnemyPosition();
            // Adjust position: subtract a small offset in the direction of correction
            CoreResources.setPlayerPosition(new Vector2(
                currentPosition.x + (resolveX != 0 ? (resolveX - Math.signum(resolveX) * epsilon) : resolveX),
                currentPosition.y + (resolveY != 0 ? (resolveY - Math.signum(resolveY) * epsilon) : resolveY)
            ));

            // Immediately update the player's collision box after changing the position
            // (Assuming you have a method to update the collision box based on the new position.)
            // e.g., updateCollisionBoxFromPosition(CoreResources.getPlayerPosition());

            // Reset velocities if a vertical collision occurred
            if (resolveY < 0) {
                CoreResources.setVelocityY(0);
            } else if (resolveY > 0) {
                CoreResources.setXLeft(collidedRect.x);
                CoreResources.setXRight(collidedRect.x + collidedRect.width);
                CoreResources.setLanded(true);
            }
        }
    }

}
