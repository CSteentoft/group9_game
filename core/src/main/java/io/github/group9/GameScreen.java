package io.github.group9;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import org.common.Services.ECSPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;


public class GameScreen implements Screen {
    private Engine engine;
    private List<ServiceLoader<ECSPlugin>> loaders = new ArrayList<>();

    @Override
    public void show() {
        engine = new Engine();
        // Load ECS plugins (which might be absent if we disable the player module)
        ServiceLoader<ECSPlugin> loader = ServiceLoader.load(ECSPlugin.class);
        int count = 0;
        for (ECSPlugin plugin : loader) {
            Gdx.app.log("GameScreen", "Loaded plugin: " + plugin.getClass().getName());
            plugin.registerSystems(engine);
            plugin.createEntities(engine);
            count++;
        }
        if (count == 0) {
            Gdx.app.log("GameScreen", "No ECSPlugin implementations found.");
        }

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
