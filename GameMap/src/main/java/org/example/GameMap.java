package org.example;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;


public class GameMap extends Entity {
    private OrthographicCamera camera;
    private TiledMap map_stage;
    private OrthogonalTiledMapRenderer mapRenderer;
    private GameMap gameMap;
    private TiledMapTileLayer groundLayer;
    private String GameMapName;

    private Entity entity;
    protected Rendering rendering;


    public GameMap(OrthographicCamera camera, String GameMapName) {
        this.camera = camera;
        map_stage = new TmxMapLoader().load(GameMapName);
        mapRenderer = new OrthogonalTiledMapRenderer(map_stage);

        rendering = new Rendering();

        entity = new Entity();
        entity.setHurtBox(new Rectangle(50, 10, 300, 50));
    }

    public void renderHurtBox(SpriteBatch batch) {
        rendering.drawHurtBox(batch, entity.getHurtBox());
    }

    public void GameMapUpdate() {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public void dispose() {
        mapRenderer.dispose();
    }
}
