package org.example.systems;


import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import io.github.group9.PositionComponent;
import org.example.components.MovementComponent;

public class MovementSystem extends EntitySystem {
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);

    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, MovementComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity e : entities) {
            PositionComponent pos = pm.get(e);
            MovementComponent mov = mm.get(e);

            // Move position by velocity * delta
            pos.x += mov.vx * deltaTime;
            pos.y += mov.vy * deltaTime;
        }
    }
}
