package org.example;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector2;
import org.common.Services.ECSPlugin;
import org.example.systems.CollisionSystem;
import com.badlogic.ashley.core.Entity;
import org.example.components.CollisionComponent;
import io.github.group9.CoreResources;

/**
 * This plugin registers the CollisionSystem and
 * optionally creates some static collision entities (like walls).
 */
public class CollisionPlugin implements ECSPlugin {
    public CollisionPlugin() { }
    @Override
    public void registerSystems(Engine engine) {
        engine.addSystem(new CollisionSystem());
        Vector2 getPlayerPosition = CoreResources.getPlayerPosition();
       /* if(getPlayerPosition!=null){
            engine.addSystem();
        }*/
    }

    @Override
    public void createEntities(Engine engine) {
        // e.g., create a static floor
        // Entity floor = new Entity();
        // CollisionComponent cc = new CollisionComponent();
        // cc.boundingBox.set(0,0, 1000,10);
        // cc.isStatic = true;
        // floor.add(cc);
        // engine.addEntity(floor);
    }
}
