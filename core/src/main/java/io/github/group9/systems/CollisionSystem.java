package io.github.group9.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import io.github.group9.components.*;

public class CollisionSystem extends EntitySystem {
    private final ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);
    private ImmutableArray<Entity> collidables;

    @Override
    public void addedToEngine(Engine engine) {
        collidables = engine.getEntitiesFor(Family.all(CollisionComponent.class, TransformComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < collidables.size(); i++) {
            Entity entityA = collidables.get(i);
            TransformComponent tA = tm.get(entityA);

            for (int j = i + 1; j < collidables.size(); j++) {
                Entity entityB = collidables.get(j);
                TransformComponent tB = tm.get(entityB);

                if (tA.bounds.overlaps(tB.bounds)) {
                    WallComponent wallA = entityA.getComponent(WallComponent.class);
                    WallComponent wallB = entityB.getComponent(WallComponent.class);

                    // If both are walls, ignore
                    if (wallA != null && wallB != null) {
                        continue;
                    }
                    // If only A is a wall => revert B
                    if (wallA != null && wallB == null) {
                        tB.position.set(tB.oldPosition);
                        tB.updateBounds();
                    }
                    // If only B is a wall => revert A
                    else if (wallB != null && wallA == null) {
                        tA.position.set(tA.oldPosition);
                        tA.updateBounds();
                    }
                    // If neither is a wall, do nothing or handle entity-entity collision differently
                }
            }
        }
    }
}

