package org.example;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class weapon {
    public float attackRate; // Attacks per second
    public int damage;
    public float lastAttackTime;
    public float attackRange;
    public boolean isPickedUp;
    public Vector2 position;
    public Sprite sprite;

    // Physics properties
    private static final float GRAVITY = 500f; // Increased gravity for visibility
    private static final float TERMINAL_VELOCITY = 300f;
    private Vector2 velocity = new Vector2(0, 0);
    private boolean isGrounded = false;

    public void update(float deltaTime, Rectangle[] collisionBoxes) {
        // Debug print
        System.out.println("Weapon Update Called");
        System.out.println("Collision Boxes: " + (collisionBoxes != null ? collisionBoxes.length : "NULL"));

        if (!isPickedUp && !isGrounded) {
            // Apply gravity
            velocity.y -= GRAVITY * deltaTime;

            // Cap terminal velocity
            velocity.y = Math.max(velocity.y, -TERMINAL_VELOCITY);

            // Debug print current state
            System.out.println("Current Position: " + position);
            System.out.println("Current Velocity: " + velocity);

            // Temporary position for collision check
            Vector2 newPosition = new Vector2(
                position.x + velocity.x * deltaTime,
                position.y + velocity.y * deltaTime
            );

            // Check for ground collision
            boolean collided = checkGroundCollision(newPosition, collisionBoxes);

            if (collided) {
                // Snap to ground
                velocity.y = 0;
                isGrounded = true;
                System.out.println("Weapon Grounded");
            } else {
                // Update position if no collision
                position.set(newPosition);
                System.out.println("Weapon Falling");
            }
        }
    }

    private boolean checkGroundCollision(Vector2 newPosition, Rectangle[] collisionBoxes) {
        // Ensure collision boxes exist
        if (collisionBoxes == null || collisionBoxes.length == 0) {
            System.out.println("No Collision Boxes Available");
            return false;
        }

        // Create weapon's bounding box
        Rectangle weaponBounds = new Rectangle(
            newPosition.x,
            newPosition.y,
            sprite.getWidth(),
            sprite.getHeight()
        );

        // Check collision with each collision box
        for (Rectangle collisionBox : collisionBoxes) {
            if (weaponBounds.overlaps(collisionBox)) {
                // Snap to top of collision box
                position.y = collisionBox.y + collisionBox.height;
                System.out.println("Collision Detected with Box: " + collisionBox);
                return true;
            }
        }
        return false;
    }

    // Existing methods remain the same
    public boolean canAttack(float currentTime) {
        return isPickedUp && (currentTime - lastAttackTime >= 1 / attackRate);
    }

    public void attack(float currentTime) {
        if (canAttack(currentTime)) {
            // Perform attack logic here (handled by your entity system)
            lastAttackTime = currentTime;
        }
    }

    public void pickUp() {
        isPickedUp = true;
        velocity.setZero(); // Stop physics when picked up
    }

    public void drop(Vector2 newPosition) {
        isPickedUp = false;
        position.set(newPosition);
        velocity.setZero();
        isGrounded = false;
    }

    public void render(SpriteBatch batch) {
        if (!isPickedUp) {
            sprite.setPosition(position.x, position.y);
            sprite.draw(batch);
        }
    }

    public void update(float deltaTime, List<Rectangle> collisionBoxes) {
    }
}
