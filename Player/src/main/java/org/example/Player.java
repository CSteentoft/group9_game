package org.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    protected Entity entity;
    protected Rendering rendering;
    private Rendering currentAnimation;
    private List<Rendering> animations;
    private boolean isJumping = false;
    private boolean isDashing = false;
    private boolean isFlipped;
    private static final float speed = 200; // Movement speed in units per second (adjust as needed)
    private int playerWidth = 30;
    private int playerHeight = 48;

    public Player() {
        rendering = new Rendering();

        entity = new Entity();
        entity.setPosition(new Vector2(750, 0));
        entity.setCollisionBox(new Rectangle(entity.getPosition().x , entity.getPosition().y, playerWidth, playerHeight));


        animations = new ArrayList<>();
        animations.add(new Rendering("player/Player_idle.png", 10, 1, entity.getPosition().x, entity.getPosition().y)); // 0 Idle
        animations.add(new Rendering("player/Player_run.png", 8, 1, entity.getPosition().x, entity.getPosition().y));   // 1 Run
        animations.add(new Rendering("player/Player_jump.png", 6, 1, entity.getPosition().x, entity.getPosition().y));  // 2 Jump
        animations.add(new Rendering("player/Player_dash.png", 9, 1, entity.getPosition().x, entity.getPosition().y));  // 3 Dash
        animations.add(new Rendering("player/Player_airspin.png", 6, 1, entity.getPosition().x, entity.getPosition().y));  // 4 Double Jump


        currentAnimation = animations.get(0);  // Default to idle animation
        isFlipped = false;

    }
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        handleInput();
        if (isJumping) {
            updateJump();
        }
        gravity();

        if (currentAnimation != null) {
            currentAnimation.setPosition(entity.getPosition().x, entity.getPosition().y);
            currentAnimation.setStateTime(currentAnimation.getStateTime() + deltaTime);
            currentAnimation.setFlip(isFlipped);
        }

        // Update setCollisionBox position
        updateCollisionBox();
    }


    public void handleInput() {
        int horizontalInput = 0;
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jump();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            horizontalInput -= 1;  // Move left
            isFlipped = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            horizontalInput += 1;  // Move right
            isFlipped = false;
        }
        moveHorizontal(horizontalInput);

    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    private float velocityX = 0;
    private float velocityY = 0;
    // Maximum horizontal speed (in units per second)
    private static final float kPlayerMaxSpeed = 300.0f;

    // Player input acceleration (in units per second squared)
    private static final float kPlayerInputAccel = 900.0f;

    // Air friction factor (applied each frame, value between 0 and 1)
    private static final float kFrictionAir = 1200.0f; // Higher value = faster stopping


    public void moveHorizontal(float horizontalInput) {
        float dt = Gdx.graphics.getDeltaTime(); // delta time for frame independence

        // Apply acceleration based on input
        velocityX += horizontalInput * kPlayerInputAccel * dt;

        // Clamp speed to prevent exceeding max speed
        if (Math.abs(velocityX) > kPlayerMaxSpeed) {
            velocityX = Math.signum(velocityX) * kPlayerMaxSpeed;
        }

        // Apply deceleration when no input is given
        if (horizontalInput == 0) {
            float deceleration = kFrictionAir * dt; // Deceleration force
            if (Math.abs(velocityX) <= deceleration) {
                velocityX = 0; // Stop completely when speed is very low
            } else {
                velocityX -= Math.signum(velocityX) * deceleration; // Reduce speed smoothly
            }
        }

        // Update position
        float newX = entity.getPosition().x + velocityX * dt;
        entity.setPosition(new Vector2(newX, entity.getPosition().y));
    }





    public void render(SpriteBatch batch) {
        if (currentAnimation != null) {// Ensure batch is started
            currentAnimation.render(batch);
        }
    }
    public void renderHurtBox(SpriteBatch batch) {
        rendering.drawCollisionBox(batch, entity.getCollisionBox(), 1, 0, 0);
    }
    public Vector2 getPosition() {
        return entity.getPosition();
    }
    public Rectangle getHurtBox(){
        return entity.getCollisionBox();
    }
    private void updateCollisionBox() {
        entity.setCollisionBox(new Rectangle(entity.getPosition().x , entity.getPosition().y, playerWidth, playerHeight));
    }
    public void dispose() {
        rendering.dispose();  // Dispose of the rendering object (which will dispose of the sprite sheet texture)
    }

    public void setPosition(float posx, float posy){
        entity.setPosition(new Vector2(posx,posy));
    }
    private int jumpCount = 0;

    public void jump() {
        if (jumpCount < 2) {
            velocityY = JUMP_VELOCITY;
            jumpCount++;
            isJumping = true;

            if(jumpCount == 1){ // Normal Jump
                currentAnimation = animations.get(2);
            } else if( jumpCount == 2){ // Double Jump
                currentAnimation = animations.get(4);
            }

            currentAnimation.setStateTime(0);
        }
    }
    public void updateJump() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        //Animation
        //--------------------------------------------------------------------------------------------------
        //this is how libgdx calculates frameIndex "int frameIndex = (int)(stateTime / FRAME_DURATION);"
        //stateTime = how long the animation has been running (like a stopwatch).
        //FRAME_DURATION = how long each frame is displayed before switching to the next.
        //frameIndex = which frame should be shown.
        if (jumpCount == 1) {
            if (velocityY > 0) { // Rising: allow the first two frames only
                float newTime = currentAnimation.getStateTime() + deltaTime;
                if (newTime > 1 * currentAnimation.getFRAME_DURATION()) {
                    newTime = 1 * currentAnimation.getFRAME_DURATION();
                }
                currentAnimation.setStateTime(newTime);
            } else {
                if (currentAnimation.getStateTime() >= currentAnimation.getAnimationDuration()) {
                    currentAnimation.setStateTime(currentAnimation.getAnimationDuration());
                } else {
                    currentAnimation.setStateTime(currentAnimation.getStateTime() + deltaTime);
                }
            }
        }
        else if (jumpCount == 2) {
            float newTime = currentAnimation.getStateTime() + deltaTime;
            // If the airspin animation hasn't finished, keep updating it.
            if (newTime < currentAnimation.getAnimationDuration()) {
                currentAnimation.setStateTime(newTime);
            }  else {
                // Alternative falling animation
                currentAnimation.setStateTime(currentAnimation.getAnimationDuration());
            }
        }
    }
    private static final float GRAVITY = -1300f; // Acceleration due to gravity -3000
    private static final float JUMP_VELOCITY = 500f; // Initial velocity for the jump

    public float getFLOOR_Y() {
        return FLOOR_Y;
    }

    public void setFLOOR_Y(float FLOOR_Y) {
        this.FLOOR_Y = FLOOR_Y;
    }

    private float FLOOR_Y = 0; // Ground level
    private static final float TERMINAL_VELOCITY = -1000f;
    public void gravity(){
        // Apply gravity
        float deltaTime = Gdx.graphics.getDeltaTime();
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
            jumpCount = 0;
            currentAnimation = animations.get(0);  // Back to idle (or another appropriate animation)
        }
    }


}
