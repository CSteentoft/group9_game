package org.player.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.group9.CoreResources;
import org.common.UserEntity;
import org.player.components.PlayerComponent;
import org.render.Rendering;

public class PlayerSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private final ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private UserEntity ue;
    private PlayerComponent pc;
    private Rendering rendering = new Rendering();
    SpriteBatch batch = new SpriteBatch();

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }


    @Override
    public void update(float deltaTime) {
        for (Entity e : entities) {
            pc = pm.get(e);
            ue = pc.userEntity;

            ue.setPosition(new Vector2(CoreResources.getPlayerPosition()));



            handleInput(deltaTime);
            updateJump(deltaTime);
            gravity(deltaTime);

            // Improved ground check before handling landing
            isOnGround();  // Moved BEFORE CoreResources.isLanded() check

            if (CoreResources.isLanded()) {
                land();
            }


            updateCollisionBox();
            CoreResources.setPlayerPosition(ue.getPosition());
            CoreResources.setPlayerHurtBox(ue.getCollisionBox());
            renderHurtBox();

        }
    }

    public void handleInput(float dt) {
        int horizontalInput = 0;
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jump();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            horizontalInput -= 1;  // Move left
            //isFlipped = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            horizontalInput += 1;  // Move right
            //isFlipped = false;
        }
        moveHorizontal(horizontalInput, dt);

    }

    //Horizontal movement

    public void moveHorizontal(float horizontalInput, float dt) {

        // Apply acceleration based on input


        ue.setVelocityX(ue.getVelocityX() + horizontalInput * pc.horizontalAccel * dt);

        // Clamp speed to prevent exceeding max speed
        if (Math.abs(ue.getVelocityX()) > pc.maxSpeed) {
            ue.setVelocityX(Math.signum(ue.getVelocityX()) * pc.maxSpeed);
        }

        // Apply deceleration when no input is given
        if (horizontalInput == 0) {
            float deceleration = pc.frictionAir * dt; // Deceleration force
            if (Math.abs(ue.getVelocityX()) <= deceleration) {
                ue.setVelocityX(0); // Stop completely when speed is very low
            } else {
                ue.setVelocityX(ue.getVelocityX() - Math.signum(ue.getVelocityX()) * deceleration); // Reduce speed smoothly
            }
        }

        // Update position
        float newX = ue.getPosition().x + ue.getVelocityX() * dt;
        ue.setPosition(new Vector2(newX, ue.getPosition().y));
    }

    //Jumping
    public void jump() {
        if (pc.jumpCount < 2) {
            ue.setVelocityY(pc.jumpVelocity);
            pc.jumpCount++;
            //  isJumping = true;
            System.out.println("jgi0rwå");
            if(pc.jumpCount == 1){ // Normal Jump
                //currentAnimation = animations.get(2);
            } else if(pc.jumpCount == 2){ // Double Jump
                //currentAnimation = animations.get(4);
            }
            //currentAnimation.setStateTime(0);
        }
    }


    public void updateJump(float dt) {
        if (pc.jumpCount < 2) {
            if (pc.jumpCount == 1) {
                if (ue.getVelocityY() > 0) { // Rising: allow the first two frames only
                    //float newTime = currentAnimation.getStateTime() + dt;
                    //float maxTime = 2 * currentAnimation.getFRAME_DURATION();
                    //currentAnimation.setStateTime(Math.min(newTime, maxTime));
                } else {
                    //currentAnimation.setStateTime(currentAnimation.getStateTime() + dt);
                }
            }
            else if (pc.jumpCount == 2) {
                // If the airSpin animation hasn't finished, keep updating it.
                // Alternative falling animation
                //currentAnimation.setStateTime(currentAnimation.getStateTime() + dt);
            }
        }
    }

    public void land() {
        pc.jumpCount = 0;  // Reset jumps when landing
        ue.setVelocityY(0);
        pc.onGround = true;
        CoreResources.setLanded(false);

    }

    public void isOnGround(){
        if ((ue.getCollisionBox().x + ue.getCollisionBox().width > CoreResources.getXLeft() &&
            ue.getCollisionBox().x < CoreResources.getXRight()) && ue.getVelocityY() == 0) {
            pc.onGround = true;
        } else {
            pc.onGround = false;
        }
    }


    public void gravity(float dt){
        if (!pc.onGround){
            ue.setVelocityY(ue.getVelocityY() + pc.GRAVITY * dt);

            // Limit fall speed to TERMINAL_VELOCITY
            if (ue.getVelocityY() < pc.terminalVelocity) {
                ue.setVelocityY(pc.terminalVelocity);
            }
            ue.getPosition().y += ue.getVelocityY() * dt;
        }
    }

    public void updateCollisionBox() {
        ue.setCollisionBox(new Rectangle(
            ue.getPosition().x + pc.xOffset,
            ue.getPosition().y + pc.yOffset,
            pc.playerWidth,
            pc.playerHeight));
    }

    public void renderHurtBox() {
        batch.setProjectionMatrix(CoreResources.getOrthographicCamera().combined);
        rendering.drawCollisionBox(batch, ue.getCollisionBox(), 1, 0, 0);
    }

}


