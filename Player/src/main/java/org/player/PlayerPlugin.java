package org.player;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import io.github.group9.CoreResources;
import org.common.Services.ECSPlugin;
import org.common.UserEntity;
import org.player.components.PlayerComponent;
import org.player.components.PlayerRenderingComponent;
import org.player.systems.PlayerRenderingSystem;
import org.player.systems.PlayerSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.render.Rendering;

public class PlayerPlugin implements ECSPlugin {

    // Public no-arg constructor required by ServiceLoader
    public PlayerPlugin() { }

    @Override
    public void registerSystems(Engine engine) {
        engine.addSystem(new PlayerSystem());
        // Get the shared SpriteBatch from CoreResources
        SpriteBatch batch = CoreResources.getSpriteBatch();
        if (batch != null) {
            engine.addSystem(new PlayerRenderingSystem(batch));
        }
    }

    @Override
    public void createEntities(Engine engine) {
        Entity playerEntity = new Entity();

        // Create the underlying UserEntity (transform)
        UserEntity ue = new UserEntity();
        ue.setPosition(new com.badlogic.gdx.math.Vector2(50, 90));
        ue.setCollisionBox(new com.badlogic.gdx.math.Rectangle(ue.getPosition().x, ue.getPosition().y, 20, 32));
        CoreResources.setPlayerPosition(new Vector2(ue.getPosition().x, ue.getPosition().y));
        playerEntity.add(ue);

        // Create and add the PlayerComponent (physics)
        PlayerComponent pc = new PlayerComponent(ue);
        playerEntity.add(pc);

        // Create and add the PlayerRenderingComponent (animations)
        PlayerRenderingComponent prc = new PlayerRenderingComponent();
        prc.animations.add(new Rendering("player/Player_idle.png", 10, 1, ue.getPosition().x, ue.getPosition().y));
        prc.animations.add(new Rendering("player/Player_run.png", 8, 1, ue.getPosition().x, ue.getPosition().y));
        prc.animations.add(new Rendering("player/Player_jump.png", 6, 1, ue.getPosition().x, ue.getPosition().y));
        prc.animations.add(new Rendering("player/Player_dash.png", 9, 1, ue.getPosition().x, ue.getPosition().y));
        prc.animations.add(new Rendering("player/Player_airSpin.png", 6, 1, ue.getPosition().x, ue.getPosition().y));
        prc.animations.add(new Rendering("player/Player_walk.png", 8, 1, ue.getPosition().x, ue.getPosition().y));
        prc.currentAnimation = prc.animations.get(0);
        playerEntity.add(prc);

        engine.addEntity(playerEntity);
    }
}



