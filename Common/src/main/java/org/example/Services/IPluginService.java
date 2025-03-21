package org.example.Services;

import com.badlogic.ashley.core.Engine;

public interface IPluginService {
    public interface ECSPlugin {
        void registerSystems(Engine engine);

        void createEntities(Engine engine);


        // Add a new method for disabling/unregistering:
        //void disable(Engine engine);

    }
}
