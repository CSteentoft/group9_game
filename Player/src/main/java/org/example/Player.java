package org.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    //Entity
    protected Entity entity;

    //Rendering & Animation
    protected Rendering rendering;
    private Rendering currentAnimation;
    private final List<Rendering> animations;


    //HurtBox
    private final int playerWidth = 20;
    private final int playerHeight = 31;
    private float xOffset = 14;
    private float yOffset = 7;

    //Input handling
    private boolean isFlipped;

    //Velocity
    private float velocityX = 0; // Current x velocity
    private float velocityY = 0; // Current y velocity

    //Horizontal movement
    private static final float kPlayerMaxSpeed = 300.0f; // Maximum horizontal speed
    private static final float kPlayerInputAccel = 900.0f; // Player input acceleration
    private static final float kFrictionAir = 1200.0f; //  // Air friction factor (applied each frame, value between 0 and 1)

    //Jumping
   // private boolean isJumping;
    private int jumpCount = 0; // How many times did the player jump
    private static final float JUMP_VELOCITY = 500f; // Initial velocity for the jump

    //Gravity
   // private float FLOOR_Y = 0; // Stating ground level
    private static final float TERMINAL_VELOCITY = -1000f; // Maximum downward velocity, that gravity can make due of
    private static final float GRAVITY = -1300f; // Acceleration due to gravity

    public Player(float xPos, float yPos) {
        //Entity
        entity = new Entity();
        entity.setPosition(new Vector2(xPos, yPos));
        entity.setCollisionBox(new Rectangle(entity.getPosition().x , entity.getPosition().y, playerWidth, playerHeight));

        //Rendering & Animation
        rendering = new Rendering();
        animations = new ArrayList<>();
        animations.add(new Rendering("player/Player_idle.png", 10, 1, entity.getPosition().x, entity.getPosition().y)); // 0 Idle
        animations.add(new Rendering("player/Player_run.png", 8, 1, entity.getPosition().x, entity.getPosition().y));   // 1 Run
        animations.add(new Rendering("player/Player_jump.png", 6, 1, entity.getPosition().x, entity.getPosition().y));  // 2 Jump
        animations.add(new Rendering("player/Player_dash.png", 9, 1, entity.getPosition().x, entity.getPosition().y));  // 3 Dash
        animations.add(new Rendering("player/Player_airSpin.png", 6, 1, entity.getPosition().x, entity.getPosition().y));  // 4 Double Jump

        currentAnimation = animations.get(0);  // Default to idle animation
        isFlipped = false;
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        //Handle input
        handleInput(deltaTime);
        //Fix jump animation
        updateJump(deltaTime);
        //Apply gravity
        gravity(deltaTime);
        //Update animation
        updateAnimation(deltaTime);
        //Update setCollisionBox position
        updateCollisionBox();

       // System.out.println(isJumping);
    }

    //Rendering & Animation
    public void render(SpriteBatch batch) {
        if (currentAnimation != null) {// Ensure batch is started
            currentAnimation.render(batch);
        }
    }
    public void renderHurtBox(SpriteBatch batch) {
        rendering.drawCollisionBox(batch, entity.getCollisionBox(), 1, 0, 0);
    }
    public void dispose() {
        rendering.dispose();  // Dispose of the rendering object
    }
    public void updateAnimation(float dt){
        if (currentAnimation != null) {
            // Grounded state
            if (jumpCount == 0) {
                boolean isMoving = Math.abs(velocityX) > 10f;

                // Force animation reset when landing
                if (currentAnimation != animations.get(0) && currentAnimation != animations.get(1)) {
                    currentAnimation = isMoving ? animations.get(1) : animations.get(0);
                    currentAnimation.setStateTime(0);
                }
                // Regular transition
                else {
                    currentAnimation = isMoving ? animations.get(1) : animations.get(0);
                }
            }
            // Airborne state
            else {
                if (jumpCount == 1 && velocityY > 0) {
                    currentAnimation = animations.get(2); // Rising jump
                } else {
                    currentAnimation = animations.get(4); // Falling/Double jump
                }
            }

            // Update animation frame
            currentAnimation.setPosition(entity.getPosition().x, entity.getPosition().y);
            currentAnimation.setStateTime(currentAnimation.getStateTime() + dt);
            currentAnimation.setFlip(isFlipped);
        }
    }


    //Player
    public void setPosition(float posX, float posY){
        entity.setPosition(new Vector2(posX, posY));
    }
    public Vector2 getPosition() {
        return entity.getPosition();
    }


    //HurtBox
    public float getXOffset() {
        return xOffset;
    }
    public float getYOffset() {
        return yOffset;
    }
    public void updateCollisionBox() {
        entity.setCollisionBox(new Rectangle(
            entity.getPosition().x + xOffset,
            entity.getPosition().y + yOffset,
            playerWidth,
            playerHeight));
    }
    public Rectangle getHurtBox(){
        return new Rectangle(entity.getCollisionBox().x, entity.getCollisionBox().y, entity.getCollisionBox().width, entity.getCollisionBox().height);
    }


    //Input handler
    public void handleInput(float dt) {
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
        moveHorizontal(horizontalInput, dt);

    }


    //Velocity
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


    //Horizontal movement
    public void moveHorizontal(float horizontalInput, float dt) {

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


    //Jumping
    public void jump() {
        if (jumpCount < 2) {
            velocityY = JUMP_VELOCITY;
            jumpCount++;
          //  isJumping = true;

            if(jumpCount == 1){ // Normal Jump
                currentAnimation = animations.get(2);
            } else if( jumpCount == 2){ // Double Jump
                currentAnimation = animations.get(4);
            }
            currentAnimation.setStateTime(0);
        }
    }


    public void updateJump(float dt) {
        if (jumpCount < 2) {
            //Animation
            //--------------------------------------------------------------------------------------------------
            //this is how libgdx calculates frameIndex "int frameIndex = (int)(stateTime / FRAME_DURATION);"
            //stateTime = how long the animation has been running (like a stopwatch).
            //FRAME_DURATION = how long each frame is displayed before switching to the next.
            //frameIndex = which frame should be shown.
            if (jumpCount == 1) {
                if (velocityY > 0) { // Rising: allow the first two frames only
                    float newTime = currentAnimation.getStateTime() + dt;
                    float maxTime = 2 * currentAnimation.getFRAME_DURATION();
                    /* if (newTime > 1 * currentAnimation.getFRAME_DURATION()) {
                        newTime = 1 * currentAnimation.getFRAME_DURATION();
                    }
                    currentAnimation.setStateTime(newTime);
                     */
                    currentAnimation.setStateTime(Math.min(newTime, maxTime));
                } else {
                    /*
                    if (currentAnimation.getStateTime() >= currentAnimation.getAnimationDuration()) {
                        currentAnimation.setStateTime(currentAnimation.getAnimationDuration());
                    } else {
                        currentAnimation.setStateTime(currentAnimation.getStateTime() + dt);
                    } */
                    currentAnimation.setStateTime(currentAnimation.getStateTime() + dt);
                }
            }
            else if (jumpCount == 2) {
               /* float newTime = currentAnimation.getStateTime() + dt;
                // If the airSpin animation hasn't finished, keep updating it.
                // Alternative falling animation
                currentAnimation.setStateTime(Math.min(newTime, currentAnimation.getAnimationDuration())); */
                currentAnimation.setStateTime(currentAnimation.getStateTime() + dt);
            }
        }
    }
    public void land() {
        jumpCount = 0;  // Reset jumps when landing
        velocityY = 0;
       // isJumping = false;
        // Reset animation to idle
        currentAnimation = animations.get(0);
        currentAnimation.setStateTime(0);
    }

    /*
    //Gravity
    public float getFLOOR_Y() {
        return FLOOR_Y;
    }
    public void setFLOOR_Y(float FLOOR_Y) {
        this.FLOOR_Y = FLOOR_Y;
    }
     */
    public void gravity(float dt){
        // Apply gravity
        //--------------------------------------------------------------------------------------------------
        velocityY += GRAVITY * dt;

        // Limit fall speed to TERMINAL_VELOCITY
        if (velocityY < TERMINAL_VELOCITY) {
            velocityY = TERMINAL_VELOCITY;
        }

        entity.getPosition().y += velocityY * dt;

        // Ground collision
        //--------------------------------------------------------------------------------------------------
        /*
        if (entity.getPosition().y <= FLOOR_Y) {
            entity.getPosition().y = FLOOR_Y;
            velocityY = 0;
            jumpCount = 0;
            currentAnimation = animations.get(0);  // Back to idle (or another appropriate animation)
        } */
    }
}
