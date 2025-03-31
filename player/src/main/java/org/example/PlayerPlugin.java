package org.example;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import io.github.group9.ECSPlugin;
import io.github.group9.components.TagComponent;
import io.github.group9.components.TransformComponent;
import io.github.group9.components.CollisionComponent;
import org.example.systems.PlayerInputSystem;
import io.github.group9.components.MovementComponent; // your plugin's own movement?

public class PlayerPlugin implements ECSPlugin {

    @Override
    public void registerSystems(Engine engine) {
        // Possibly add PlayerInputSystem, MovementSystem if not already added
        engine.addSystem(new PlayerInputSystem());
        // If your MovementSystem is also in the plugin, add it here
        // e.g. engine.addSystem(new MovementSystem());
    }

    @Override
    public void createEntities(Engine engine) {
        // Create a Player
        Entity player = new Entity();

        TransformComponent transform = new TransformComponent();
        transform.position.set(100, 100);
        transform.size.set(32, 32);
        transform.updateBounds();
        player.add(transform);

        // Mark it collidable if you want collisions
        player.add(new CollisionComponent());

        // Tag it as 'player' so core can color it green
        TagComponent tag = new TagComponent("player");
        player.add(tag);

        // If your plugin has MovementComponent:
        MovementComponent mov = new MovementComponent();
        mov.speed = 200;
        // vx, vy set by PlayerInputSystem
        player.add(mov);

        engine.addEntity(player);
    }
}

