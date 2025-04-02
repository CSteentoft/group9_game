package org.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import io.github.group9.CoreResources;
import org.common.Services.ECSPlugin;
import org.common.UserEntity;
import org.enemy.components.EnemyComponent;
import org.render.components.RenderingComponent;
import org.enemy.systems.EnemySystem;
import org.enemy.systems.EnemyRenderingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EnemyPlugin implements ECSPlugin {

    // Public no-arg constructor required by ServiceLoader
    public EnemyPlugin() { }

    @Override
    public void registerSystems(Engine engine) {
        engine.addSystem(new EnemySystem());
        // Get the shared SpriteBatch from CoreResources
        SpriteBatch batch = CoreResources.getSpriteBatch();
        if (batch != null) {
            engine.addSystem(new EnemyRenderingSystem(batch));
        }
    }

    @Override
    public void createEntities(Engine engine) {
        Entity enemyEntity = new Entity();

        // Create the underlying UserEntity (for position, collision, etc.)
        UserEntity ue = new UserEntity();
        ue.setPosition(new Vector2(50, 90));
        ue.setCollisionBox(new com.badlogic.gdx.math.Rectangle(ue.getPosition().x, ue.getPosition().y, 20, 32));
        CoreResources.setEnemyPosition(new Vector2(ue.getPosition().x, ue.getPosition().y));
        enemyEntity.add(ue);

        // Create and add the PlayerComponent (physics, input, etc.)
        EnemyComponent ec = new EnemyComponent(ue);
        enemyEntity.add(ec);

        // Create and add the RenderingComponent (handles animations)
        RenderingComponent rc = new RenderingComponent();
        rc.addAnimation("idle", "player/Player_idle.png", 10, 1, 0.100f);
        rc.addAnimation("run", "player/Player_run.png", 8, 1, 0.092f);
        rc.addAnimation("jump", "player/Player_jump.png", 6, 1, 0.088f);
        rc.addAnimation("dash", "player/Player_dash.png", 9, 1, 0.088f);
        rc.addAnimation("airSpin", "player/Player_airSpin.png", 6, 1, 0.099f);
        rc.addAnimation("walk", "player/Player_walk.png", 8, 1, 0.088f);
        // Set the default animation (idle)
        rc.setCurrentAnimation("idle");
        enemyEntity.add(rc);

        engine.addEntity(enemyEntity);
    }
}




