package org.player.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import org.example.GameMap;
import org.example.systems.CollisionSystem;
import org.example.components.CollisionComponent;
import org.player.components.PlayerComponent;

/**
 * A system that calls the player's handleCollisionsSwept(...) each frame,
 * letting the player do multi-iteration collision resolution with the same bounding box
 * used by the main CollisionSystem.
 */
public class PlayerCollisionSystem extends EntitySystem {
    /*private final Family family = Family.all(PlayerComponent.class, CollisionComponent.class).get();
    private ImmutableArray<Entity> entities;

    private final ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);

    private final GameMap gameMap;
    private final CollisionSystem collisionSystem;

    public PlayerCollisionSystem(GameMap gameMap, CollisionSystem collisionSystem) {
        this.gameMap = gameMap;
        this.collisionSystem = collisionSystem;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float deltaTime) {
        for (Entity e : entities) {
            PlayerComponent pc = pm.get(e);
            CollisionComponent cc = cm.get(e);

            // Call the player's iterative collision approach
            pc.handleCollisionsSwept(deltaTime, gameMap, collisionSystem, cc);
        }
    }

     */
}

