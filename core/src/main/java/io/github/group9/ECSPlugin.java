package io.github.group9;

import com.badlogic.ashley.core.Engine;

/**
 * An interface that optional subprojects can implement to
 * register their Ashley systems and create any entities they need.
 */
public interface ECSPlugin {
    /**
     * Called so the plugin can add its Ashley systems, e.g. input systems.
     */
    void registerSystems(Engine engine);

    /**
     * Called so the plugin can create any initial entities, e.g. a player.
     */
    void createEntities(Engine engine);


    // Add a new method for disabling/unregistering:
    //void disable(Engine engine);


}

