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

    private float velocityY = 0;  // Initial vertical speed
    private float gravity = -800f;  // Gravity pulling the player down (adjusted)
    private float jumpStrength = 350f;  // Jump strength (upward force)
    private boolean isJumping = false; // To check if the player is in the air
    private boolean isOnGround = true;  // To check if the player is on the ground

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
        currentAnimation.setFlip(deltaX < 0);
        entity.setPosition(new Vector2(entity.getPosition().x + speed * deltaX * deltaTime, entity.getPosition().y));
    }

    @Override
    public void jump() {
        if (isOnGround && !isJumping) {  // Ensure the player can only jump if on the ground
            isJumping = true;
            velocityY = jumpStrength;  // Set initial upward velocity
            isOnGround = false;  // The player is no longer on the ground
        }
    }

    public Player() {
        entity = new Entity();  // Initialize the entity here
        entity.setPosition(new Vector2(0, 0));
        currentAnimation = new Rendering("Player_idle.png", 10, 1, entity.getPosition().x, entity.getPosition().y);  // Default idle animation
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Update jump mechanics with gravity
        if (isJumping) {
            velocityY += gravity * deltaTime;  // Apply gravity to the velocity
            float newY = entity.getPosition().y + velocityY * deltaTime;

            // Prevent the player from going below the ground level (y = 0)
            if (newY <= 0) {
                newY = 0;
                isJumping = false;  // Player lands on the ground
                velocityY = 0;  // Reset velocity when landing
                isOnGround = true;  // Player is on the ground
            }

            // Update position
            entity.setPosition(new Vector2(entity.getPosition().x, newY));
        }

        // Handle horizontal movement (left-right)
        handleInput();  // Handle user input for movement
        if (currentAnimation != null) {
            // Update the current animation's position
            currentAnimation.setPosition(entity.getPosition().x, entity.getPosition().y);
            currentAnimation.setStateTime(currentAnimation.getStateTime() + Gdx.graphics.getDeltaTime());  // Update animation time
        }
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
        }

        // Handle jump input
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            jump();  // Initiate jump when space is pressed
        }
    }
}
