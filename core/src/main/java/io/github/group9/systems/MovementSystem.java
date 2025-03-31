package io.github.group9.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import io.github.group9.components.TransformComponent;
import io.github.group9.components.MovementComponent; // <--- If you store MovementComponent in core

public class MovementSystem extends EntitySystem {
    private final ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);

    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(TransformComponent.class, MovementComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity e : entities) {
            TransformComponent transform = tm.get(e);
            MovementComponent mov = mm.get(e);

            // store oldPosition for collision revert
            transform.oldPosition.set(transform.position);

            // Move
            transform.position.x += mov.vx * deltaTime;
            transform.position.y += mov.vy * deltaTime;

            // Update bounding box
            transform.updateBounds();
        }
    }
}

