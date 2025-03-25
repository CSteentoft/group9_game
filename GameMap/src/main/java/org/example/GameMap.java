package org.example;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;


public class GameMap extends Entity {
    private OrthographicCamera camera;
    private TiledMap map_stage;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer groundLayer;
    private Entity entity;
    protected Rendering rendering;
    List<Entity> entities = new ArrayList<>();


    public GameMap(OrthographicCamera camera, String GameMapName) {
        this.camera = camera;
        map_stage = new TmxMapLoader().load(GameMapName);
        mapRenderer = new OrthogonalTiledMapRenderer(map_stage);
        groundLayer = (TiledMapTileLayer) map_stage.getLayers().get(0);

        rendering = new Rendering();

        //entity = new Entity();
        //entity.setHurtBox(new Rectangle(50, 10, 300, 50));
    }

    public void generateEntitiesForTiles() {
        int width = groundLayer.getWidth();
        int height = groundLayer.getHeight();
        int tileWidth = (int) groundLayer.getTileWidth();
        int tileHeight = (int) groundLayer.getTileHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                if (cell != null) {
                    TiledMapTile tile = cell.getTile();
                    if (tile != null) {
                        Entity entity = new Entity(); // Create a new entity
                        entity.setHurtBox(new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight)); // Correct world position
                        entities.add(entity);
                    }
                }
            }
        }
    }


    public void renderAllHurtBoxes(SpriteBatch batch) {
        for (Entity entity : entities) {
            renderHurtBox(batch, entity);
        }
    }
    public void renderHurtBox(SpriteBatch batch, Entity entity) {
        rendering.drawCollisionBox(batch, entity.getHurtBox(), 0, 0, 1);
    }

    public void GameMapUpdate() {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public void dispose() {
        mapRenderer.dispose();
    }
}
