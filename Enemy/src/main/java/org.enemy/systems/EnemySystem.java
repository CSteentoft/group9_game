package org.enemy.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.group9.CoreResources;
import org.common.UserEntity;
import org.render.Rendering;
import org.render.components.RenderingComponent;
import org.enemy.components.EnemyComponent;


public class EnemySystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private final ComponentMapper<EnemyComponent> ecm = ComponentMapper.getFor(EnemyComponent.class);
    private final ComponentMapper<RenderingComponent> rcm = ComponentMapper.getFor(RenderingComponent.class);
    private final ComponentMapper<UserEntity> uem = ComponentMapper.getFor(UserEntity.class);
    private Rendering rend = new Rendering();

    SpriteBatch batch = new SpriteBatch();

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(EnemyComponent.class, UserEntity.class, RenderingComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity e : entities) {
            EnemyComponent ec = ecm.get(e);
            UserEntity ue = uem.get(e);
            RenderingComponent rc = rcm.get(e);

            // Update the player's position from CoreResources if needed.
            ue.setPosition(new Vector2(CoreResources.getEnemyPosition()));

            // Handle input, jumping, gravity, collision box updates, etc.
            handleInput(deltaTime, ec, ue);
            updateJump(deltaTime, ec, ue);
            gravity(deltaTime, ec, ue);
            isOnGround(ec, ue);
            if (CoreResources.isLanded()) {
                land(ec, ue);
            }
            updateCollisionBox(ec, ue);
            CoreResources.setEnemyPosition(ue.getPosition());
            CoreResources.setEnemyHurtBox(ue.getCollisionBox());
            renderHurtBox(ue);

            // Update the animation based on current state.
            updateAnimation(deltaTime, ec, ue, rc);
        }
    }

    private void handleInput(float dt, EnemyComponent ec, UserEntity ue) {
        int horizontalInput = 0;
       moveHorizontal(1, dt, ec, ue);
    }

    private void moveHorizontal(float horizontalInput, float dt, EnemyComponent ec, UserEntity ue) {
        ue.setVelocityX(ue.getVelocityX() + horizontalInput * ec.horizontalAccel * dt);
        if (Math.abs(ue.getVelocityX()) > ec.maxSpeed) {
            ue.setVelocityX(Math.signum(ue.getVelocityX()) * ec.maxSpeed);
        }
        if (horizontalInput == 0) {
            float deceleration = ec.frictionAir * dt;
            if (Math.abs(ue.getVelocityX()) <= deceleration) {
                ue.setVelocityX(0);
            } else {
                ue.setVelocityX(ue.getVelocityX() - Math.signum(ue.getVelocityX()) * deceleration);
            }
        }
        float newX = ue.getPosition().x + ue.getVelocityX() * dt;
        ue.setPosition(new Vector2(newX, ue.getPosition().y));
    }

    private void jump(EnemyComponent ec, UserEntity ue) {
        if (ec.jumpCount < 2) {
            ue.setVelocityY(ec.jumpVelocity);
            ec.jumpCount++;
            // Optionally reset animation state time when jump starts.
        }
    }

    private void updateJump(float dt, EnemyComponent ec, UserEntity ue) {
        // Implement additional jump-related logic as needed.
    }

    private void land(EnemyComponent ec, UserEntity ue) {
        ec.jumpCount = 0;
        ue.setVelocityY(0);
        ec.onGround = true;
        CoreResources.setLanded(false);
    }

    private void isOnGround(EnemyComponent ec, UserEntity ue) {
        if ((ue.getCollisionBox().x + ue.getCollisionBox().width > CoreResources.getXLeft() &&
            ue.getCollisionBox().x < CoreResources.getXRight()) &&
            ue.getVelocityY() == 0) {
            ec.onGround = true;
        } else {
            ec.onGround = false;
        }
    }

    private void gravity(float dt, EnemyComponent ec, UserEntity ue) {
        if (!ec.onGround) {
            ue.setVelocityY(ue.getVelocityY() + ec.GRAVITY * dt);
            if (ue.getVelocityY() < ec.terminalVelocity) {
                ue.setVelocityY(ec.terminalVelocity);
            }
            ue.getPosition().y += ue.getVelocityY() * dt;
        }
    }

    private void updateCollisionBox(EnemyComponent ec, UserEntity ue) {
        ue.setCollisionBox(new Rectangle(
            ue.getPosition().x + ec.xOffset,
            ue.getPosition().y + ec.yOffset,
            ec.EnemyWidth,
            ec.EnemyHeight));
    }

    private void renderHurtBox(UserEntity ue) {
        batch.setProjectionMatrix(CoreResources.getOrthographicCamera().combined);
        // Using a temporary ShapeRenderer instance; consider centralizing collision box rendering.
        // For example purposes only.
        rend.drawCollisionBox(batch, ue.getCollisionBox(), 1, 0, 0);
    }

    /**
     * Updates the player's current animation based on movement and jump state.
     * Assumes the following key-to-animation mapping:
     * - "idle" for idle (index 0)
     * - "run" for running (index 1)
     * - "jump" for rising jump (index 2)
     * - "airSpin" for falling/double jump (index 4)
     */
    private void updateAnimation(float dt, EnemyComponent ec, UserEntity ue, RenderingComponent rc) {
        if (rc.currentAnimation != null) {
            // Grounded state
            if (ec.jumpCount == 0) {
                boolean isMoving = Math.abs(ue.getVelocityX()) > 10f;
                // Force animation reset when landing: if current animation is not "idle" or "run"
                if (!rc.currentAnimation.equals(rc.getAnimation("idle")) &&
                    !rc.currentAnimation.equals(rc.getAnimation("run"))) {
                    rc.setCurrentAnimation(isMoving ? "run" : "idle");
                    rc.setStateTime(0f);
                } else {
                    rc.setCurrentAnimation(isMoving ? "run" : "idle");
                }
            }
            // Airborne state
            else {
                if (ec.jumpCount == 1 && ue.getVelocityY() > 0) {
                    rc.setCurrentAnimation("jump"); // Rising jump
                } else {
                    rc.setCurrentAnimation("airSpin"); // Falling/Double jump
                }
            }

            // Update the animation frame time using the component's stateTime.
            rc.setStateTime(rc.getStateTime() + dt);
            // Set sprite flipping based on the player's current state.
            rc.setFlip(ec.isFlipped);
        }
    }

}


