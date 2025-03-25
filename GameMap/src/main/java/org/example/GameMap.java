package org.example;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameMap {
    private Texture mapTexture;
    private OrthographicCamera camera;
    private TiledMap map_stage;
    private OrthogonalTiledMapRenderer mapRenderer;
    private GameMap gameMap;
    private TiledMapTileLayer groundLayer;
    private String GameMapName;

    public GameMap(OrthographicCamera camera, String GameMapName) {
        this.camera = camera;
        map_stage = new TmxMapLoader().load(GameMapName);
        mapRenderer = new OrthogonalTiledMapRenderer(map_stage);

    }

    public void GameMapUpdate() {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public void dispose() {
        mapRenderer.dispose();
    }
}
