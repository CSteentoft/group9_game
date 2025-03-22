package org.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements ICommonFighter {
    protected Entity entity;
    protected Rendering rendering;
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
    private boolean isDashing = false;
    private float velocityY = 0; // Vertical velocity
    private static final float GRAVITY = -1300f; // Acceleration due to gravity -3000
    private static final float JUMP_VELOCITY = 450f; // Initial velocity for the jump
    private static final float FLOOR_Y = -5; // Ground level
    private static final float TERMINAL_VELOCITY = -1000f; // Max downward speed
    private boolean isFlipped;
    private static final float speed = 200; // Movement speed in units per second (adjust as needed)

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


    //Completed
    //------------------------------------------------------------------------------------------------------------
    public Player() {
        entity = new Entity();
        entity.setPosition(new Vector2(0, 0));

        animations = new ArrayList<>();
        animations.add(new Rendering("Player_idle.png", 10, 1, entity.getPosition().x, entity.getPosition().y)); // Idle
        animations.add(new Rendering("Player_run.png", 8, 1, entity.getPosition().x, entity.getPosition().y));   // Run
        animations.add(new Rendering("Player_jump.png", 6, 1, entity.getPosition().x, entity.getPosition().y));  // Jump
        animations.add(new Rendering("Player_dash.png", 9, 1, entity.getPosition().x, entity.getPosition().y));  // Dash

        currentAnimation = animations.get(0);  // Default to idle animation
        isFlipped = false;

    }
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        handleInput();

        if (isJumping) {
            updateJump();
        }
        if (isDashing) {
            updateDash();
        }

        if (currentAnimation != null) {
            currentAnimation.setPosition(entity.getPosition().x, entity.getPosition().y);
            currentAnimation.setStateTime(currentAnimation.getStateTime() + deltaTime);
            currentAnimation.setFlip(isFlipped);
        }
    }
    public void render(SpriteBatch batch) {
        if (currentAnimation != null) {
            currentAnimation.render(batch);  // Render the current animation
        }
    }
    public void handleInput() {
        int horizontalInput = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            horizontalInput -= 1;  // Move left
            isFlipped = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            horizontalInput += 1;  // Move right
            isFlipped = false;
        }

        move(horizontalInput);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            dash();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jump();
        }
    }
    @Override
    public void move(float deltaX) {
        float deltaTime = Gdx.graphics.getDeltaTime(); // Get deltaTime for smooth movement

        entity.setPosition(new Vector2(entity.getPosition().x + speed * deltaX * deltaTime, entity.getPosition().y));

        if (deltaX == 0 && !isJumping && !isDashing) {
            // Switch to idle animation if not already idle
            currentAnimation = animations.get(0);
        } else if ((deltaX < 0 || deltaX > 0) && !isJumping && !isDashing) {
            // Switch to running animation
            currentAnimation = animations.get(1);
        }
    }
    @Override
    public void jump() {
        if (!isJumping) {
            velocityY = JUMP_VELOCITY;
            isJumping = true;
            currentAnimation = animations.get(2);
            currentAnimation.setStateTime(0);
        }
    }
    public void updateJump() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Apply gravity
        //--------------------------------------------------------------------------------------------------
        velocityY += GRAVITY * deltaTime;

        // Limit fall speed to TERMINAL_VELOCITY
        if (velocityY < TERMINAL_VELOCITY) {
            velocityY = TERMINAL_VELOCITY;
        }

        entity.getPosition().y += velocityY * deltaTime;

        // Ground collision
        //--------------------------------------------------------------------------------------------------
        if (entity.getPosition().y <= FLOOR_Y) {
            entity.getPosition().y = FLOOR_Y;
            velocityY = 0;
            isJumping = false;
            currentAnimation = animations.get(0);  // Back to idle (or another appropriate animation)
        }

        //Animation
        //--------------------------------------------------------------------------------------------------
        //this is how libgdx calculates frameIndex "int frameIndex = (int)(stateTime / FRAME_DURATION);"
        //stateTime = how long the animation has been running (like a stopwatch).
        //FRAME_DURATION = how long each frame is displayed before switching to the next.
        //frameIndex = which frame should be shown.
        if (velocityY > 0) { // Rising: allow the first two frames only
            float newTime = currentAnimation.getStateTime() + deltaTime;
            if (newTime > 1 * currentAnimation.getFRAME_DURATION()) {
                newTime = 1 * currentAnimation.getFRAME_DURATION();
            }
            currentAnimation.setStateTime(newTime);
        } else {
            if (currentAnimation.getStateTime() >= currentAnimation.getAnimationDuration()) {
                currentAnimation.setStateTime(currentAnimation.getAnimationDuration());
            }else {
                currentAnimation.setStateTime(currentAnimation.getStateTime() + deltaTime);
            }
        }
    }



    private float dashTime = 0f;
    private static final float DASH_DURATION = 0.25f;
    private static final float DASH_SPEED = 350f;

    public void dash() {
        if (!isDashing) {
            isDashing = true;
            dashTime = 0f;
            currentAnimation = animations.get(3); // Dash animation (assumed index 3)
            currentAnimation.setStateTime(0);      // Start from the beginning
        }
    }

    public void updateDash() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        dashTime += deltaTime;

        if (isFlipped) {
            entity.getPosition().x -= DASH_SPEED * deltaTime;
        } else {
            entity.getPosition().x += DASH_SPEED * deltaTime;
        }

        // Update dash animation state time and clamp it so that it matches the dash duration.
        float newStateTime = currentAnimation.getStateTime() + deltaTime;
        if (newStateTime > DASH_DURATION) {
            newStateTime = DASH_DURATION;
        }
        currentAnimation.setStateTime(newStateTime);

        // End the dash after DASH_DURATION seconds.
        if (dashTime >= DASH_DURATION) {
            isDashing = false;
            currentAnimation.setStateTime(0);
        }
    }

    public void dispose() {
        rendering.dispose();  // Dispose of the rendering object (which will dispose of the sprite sheet texture)
    }

}
