package org.player.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.github.group9.CoreResources;
import org.common.UserEntity;
import org.player.components.PlayerComponent;
import org.player.components.PlayerRenderingComponent;

public class PlayerSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private final ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<PlayerRenderingComponent> prcm = ComponentMapper.getFor(PlayerRenderingComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PlayerComponent.class, PlayerRenderingComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity e : entities) {
            PlayerComponent pc = pm.get(e);
            PlayerRenderingComponent prc = prcm.get(e);
            UserEntity ue = pc.userEntity;

            CoreResources.setPlayerPosition(new Vector2(ue.getPosition().x, ue.getPosition().y));

            // Handle horizontal input
            int inputX = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) inputX -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) inputX += 1;

            // Update horizontal velocity
            ue.getVelocity().x += inputX * pc.horizontalAccel * deltaTime;
            if (inputX != 0) {
                boolean shouldFlip = inputX < 0;  // Flip when moving left
                if (prc.currentAnimation != null) {
                    prc.currentAnimation.setFlip(shouldFlip);
                }
            }

            if (inputX == 0) {
                float friction = pc.frictionAir * deltaTime;
                if (Math.abs(ue.getVelocity().x) < friction)
                    ue.getVelocity().x = 0;
                else
                    ue.getVelocity().x -= Math.signum(ue.getVelocity().x) * friction;
            }
            if (Math.abs(ue.getVelocity().x) > pc.maxSpeed) {
                ue.getVelocity().x = Math.signum(ue.getVelocity().x) * pc.maxSpeed;
            }

            // Jumping
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (pc.jumpCount < 2) {
                    ue.getVelocity().y = pc.jumpVelocity;
                    pc.jumpCount++;
                    pc.onGround = false;
                }
            }

            // Gravity
            if (!pc.onGround) {
                ue.getVelocity().y += pc.gravity * deltaTime;
                if (ue.getVelocity().y < pc.terminalVelocity)
                    ue.getVelocity().y = pc.terminalVelocity;
            }

            // Update position using velocity
            ue.getPosition().x += ue.getVelocity().x * deltaTime;
            ue.getPosition().y += ue.getVelocity().y * deltaTime;

            // Simple ground check
            if (Math.abs(ue.getVelocity().y) < 0.0001f) {
                pc.onGround = true;
                pc.jumpCount = 0;
            } else {
                pc.onGround = false;
            }

            // Update animations based on state
            if (prc != null && !prc.animations.isEmpty()) {
                if (pc.onGround) {
                    if (inputX != 0) {
                        // Running animation (index 1)
                        prc.currentAnimation = prc.animations.get(1);
                    } else {
                        // Idle animation (index 0)
                        prc.currentAnimation = prc.animations.get(0);
                    }
                } else {
                    // In the air
                    if (pc.jumpCount >= 2) {
                        // Double jump animation (index 4)
                        prc.currentAnimation = prc.animations.get(4);
                    } else {
                        // Jump animation (index 2)
                        prc.currentAnimation = prc.animations.get(2);
                    }
                }
            }
        }
    }
}
