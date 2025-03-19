package org.example;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import io.github.group9.ECSPlugin;
import org.example.components.MovementComponent;
import io.github.group9.PositionComponent;
import org.example.systems.MovementSystem;
import org.example.systems.PlayerInputSystem;

/**
 * Implementation of ECSPlugin that adds player systems and creates a player entity.
 */
public class PlayerPlugin implements ECSPlugin {

    @Override
    public void registerSystems(Engine engine) {
        // Ashley Systems (from this subproject)
        engine.addSystem(new PlayerInputSystem());
        engine.addSystem(new MovementSystem());
    }

    @Override
    public void createEntities(Engine engine) {
        // Create the player entity, add your Position/Movement components, etc.
        Entity player = new Entity();

        PositionComponent pos = new PositionComponent();
        pos.x = 100;
        pos.y = 100;

        MovementComponent mov = new MovementComponent();
        mov.speed = 200; // e.g. 200 units/sec

        player.add(pos);
        player.add(mov);

        engine.addEntity(player);
    }
}

