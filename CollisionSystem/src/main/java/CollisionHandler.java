import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class CollisionHandler {
    protected Rectangle hitbox;
    protected Rectangle attackBox;
    protected Rectangle collisionBox;

    public CollisionHandler() {
        hitbox = new Rectangle();
        attackBox = new Rectangle();
        collisionBox = new Rectangle();
    }

    // Basic AABB collision detection
    public boolean checkAABBCollision(Rectangle A, Rectangle B) {
        return (A.x < B.x + B.width) &&
            (A.x + A.width > B.x) &&
            (A.y < B.y + B.height) &&
            (A.y + A.height > B.y);
    }

    // Check collision with a list of objects
    public boolean checkCollisionWithObjects(Rectangle playerBox, Array<Rectangle> objects) {
        for (Rectangle obj : objects) {
            if (checkAABBCollision(playerBox, obj)) {
                return true; // Collided with an object
            }
        }
        return false; // No collision
    }

    // Platform collision (handles one-way platforms)
    public boolean checkPlatformCollision(Rectangle playerBox, Rectangle platform, boolean oneWay) {
        if (!checkAABBCollision(playerBox, platform)) return false;

        if (oneWay) {
            // Only collide from the top
            return playerBox.y >= platform.y + platform.height - 5; // Small threshold for smooth landing
        }

        return true; // Normal solid platform collision
    }

    // Check if an attack hits an enemy
    public boolean checkAttackCollision(Rectangle attackBox, Rectangle enemyBox) {
        return checkAABBCollision(attackBox, enemyBox);
    }
}
