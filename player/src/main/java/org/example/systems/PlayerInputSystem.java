package org.example.systems;


import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.example.components.MovementComponent;

public class PlayerInputSystem extends EntitySystem {
    private ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        // We only care about entities that have a MovementComponent
        entities = engine.getEntitiesFor(Family.all(MovementComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        // Check input state each frame and set velocity accordingly
        boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);

        for (Entity e : entities) {
            MovementComponent mov = mm.get(e);

            // Reset velocity each frame
            mov.vx = 0f;
            mov.vy = 0f;

            // Vertical movement
            if (up) {
                mov.vy = mov.speed;
            } else if (down) {
                mov.vy = -mov.speed;
            }

            // Horizontal movement
            if (left) {
                mov.vx = -mov.speed;
            } else if (right) {
                mov.vx = mov.speed;
            }
        }
    }
}
