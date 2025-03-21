package org.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements ICommonFighter {
    protected Entity entity;
    protected boolean isAttacking = false;
    protected boolean isMoving = false;
    protected boolean isDead = false;

    protected int health = 100;
    protected boolean isTakingDamage = false;

    protected Stats stat = new Stats(1, 1, 1, 1);

    private Rendering currentAnimation;  // Current animation to be displayed

    private List<Rendering> animations; // List to store animations (idle, run, etc.)

    // Jumping variables
    private boolean isJumping = false;
    private float x, y; // Position of the player
    private float velocityY = 0; // Vertical velocity
    private static final float GRAVITY = -3000f; // Acceleration due to gravity
    private static final float JUMP_VELOCITY = 800f; // Initial velocity for the jump
    private static final float FLOOR_Y = 0; // Ground level
    private static final float TERMINAL_VELOCITY = -1000f; // Max downward speed

    @Override
    public boolean isAlive() {
        return !isDead;
    }

    @Override
    public void takeDamage(int amount) {
        if (!isDead) {
            health -= amount;
            isTakingDamage = true;
            if (health <= 0) {
                health = 0;
                isDead = true;
            }
        }
    }

    @Override
    public void heavyAttack() {}

    @Override
    public void lightAttack() {}

    @Override
    public void block() {}

    @Override
    public void equipWeapon() {}

    @Override
    public void dropWeapon() {}

    @Override
    public void throwWeapon() {}

    @Override
    public Stats getStats() {
        return stat;
    }

    @Override
    public void setStats(Stats stats) {
        this.stat = stats;
    }

    @Override
    public void move(float deltaX) {
        float deltaTime = Gdx.graphics.getDeltaTime(); // Get deltaTime for smooth movement
        float speed = 200; // Movement speed in units per second (adjust as needed)
        entity.setPosition(new Vector2(entity.getPosition().x + speed * deltaX * deltaTime, entity.getPosition().y));

        if (deltaX == 0) {
            // Switch to idle animation if not already idle
            if (!currentAnimation.equals(animations.get(0))) {
                currentAnimation = animations.get(0);
                currentAnimation.setFlip(false); // Ensure no flip on idle
            }
        } else {
            // Switch to running animation
            currentAnimation = animations.get(1);
            currentAnimation.setFlip(deltaX < 0); // Flip for left movement
        }
    }

    @Override
    public void jump() {
        if (!isJumping) {  // Allow jumping only when not already jumping
            velocityY = JUMP_VELOCITY;
            isJumping = true;
        }
    }
    public void updateJump() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Apply gravity
        velocityY += GRAVITY * deltaTime;

        // Limit fall speed to TERMINAL_VELOCITY
        if (velocityY < TERMINAL_VELOCITY) {
            velocityY = TERMINAL_VELOCITY;
        }

        // Update position
        entity.getPosition().y += velocityY * deltaTime;

        // Ground collision
        if (entity.getPosition().y <= FLOOR_Y) {
            entity.getPosition().y = FLOOR_Y;
            velocityY = 0;
            isJumping = false;
        }
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Handle horizontal movement (left-right)
        handleInput();  // Handle user input for movement

        updateJump();

        if (currentAnimation != null) {
            // Update animation with new position
            currentAnimation.setPosition(entity.getPosition().x, entity.getPosition().y);
            currentAnimation.setStateTime(currentAnimation.getStateTime() + deltaTime);  // Update animation time
        }
    }

    public Player() {
        entity = new Entity();  // Initialize the entity here
        entity.setPosition(new Vector2(0, 0));

        animations = new ArrayList<>();
        animations.add(new Rendering("Player_idle.png", 10, 1, entity.getPosition().x, entity.getPosition().y));  // Idle
        animations.add(new Rendering("Player_run.png", 8, 1, entity.getPosition().x, entity.getPosition().y));  // Run
        animations.add(new Rendering("Player_jump.png", 6, 1, entity.getPosition().x, entity.getPosition().y));  // Jump
        // Add more animations as needed, e.g., attack animations
        currentAnimation = animations.get(0);  // Default to idle animation
    }

    public void render(SpriteBatch batch) {
        if (currentAnimation != null) {
            currentAnimation.render(batch);  // Render the current animation
        }
    }

    public void handleInput() {
        // Handle horizontal movement
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            move(-1);  // Move left
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            move(1);  // Move right
        } else {
            move(0);
        }

        // Handle jump input
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jump();
        }
    }
}
