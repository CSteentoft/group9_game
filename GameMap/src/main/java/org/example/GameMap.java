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

import java.util.*;


public class GameMap extends Entity {
    private OrthographicCamera camera;
    private TiledMap map_stage;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer groundLayer;
    private Entity entity_create;
    protected Rendering rendering;
    List<Entity> entitiesOld = new ArrayList<>();
    List<Entity> entitiesNew = new ArrayList<>();

    public GameMap(OrthographicCamera camera, String GameMapName) {
        this.camera = camera;
        map_stage = new TmxMapLoader().load(GameMapName);
        mapRenderer = new OrthogonalTiledMapRenderer(map_stage);
        groundLayer = (TiledMapTileLayer) map_stage.getLayers().get(0);
        rendering = new Rendering();
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

                        entity_create = new Entity(); // Create a new entity
                        entity_create.setCollisionBox(new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight)); // Correct world position
                        entitiesOld.add(entity_create);
                    }
                }
            }
        }
    }
    public void tileMerging() {
        // Groups all the entity's that have the same y position
        Map<Float, List<Entity>> rows = new HashMap<>();

        // Goes through each entity, and assigns it to its corresponding y position
        for (Entity e : entitiesOld) {
            float y = e.getCollisionBox().y;

            // This line ensures that the list exists for the y-coordinate, creating it if necessary
            List<Entity> rowEntities = rows.computeIfAbsent(y, k -> new ArrayList<>());

            // Adds the current entity to the list for its corresponding y-coordinate
            rowEntities.add(e);
        }

        // For each row (y position in the map), sort by x position and merge consecutive entities
        for (Map.Entry<Float, List<Entity>> entry : rows.entrySet()) {
            List<Entity> rowEntities = entry.getValue();
            // Sort entities on the row by their x coordinate
            rowEntities.sort(Comparator.comparing(e -> e.getCollisionBox().x));

            Rectangle mergedBox = new Rectangle(
                rowEntities.get(0).getCollisionBox().x,
                rowEntities.get(0).getCollisionBox().y,
                rowEntities.get(0).getCollisionBox().width,
                rowEntities.get(0).getCollisionBox().height
            );

            for (int i = 1; i < rowEntities.size(); i++) {
                Rectangle current = rowEntities.get(i).getCollisionBox();

                if (mergedBox.x + mergedBox.width == current.x) {
                    mergedBox.width += current.width;
                } else {
                    // If there's a gap, store the mergedBox as a new entity
                    Entity mergedEntity = new Entity();
                    mergedEntity.setCollisionBox(new Rectangle(mergedBox));
                    entitiesNew.add(mergedEntity);

                    // The mergedBox is now just the next tile in the row that didn’t connect with the others
                    mergedBox = new Rectangle(current);
                }
            }
            // Add the last merged box from this row to the list
            Entity mergedEntity = new Entity();
            mergedEntity.setCollisionBox(new Rectangle(mergedBox));
            entitiesNew.add(mergedEntity);
        }
        entitiesOld.clear();
    }
    public void renderAllHurtBoxes(SpriteBatch batch) {
        for (Entity entity : entitiesNew) {
            renderHurtBox(batch, entity);
        }
    }
    public void renderHurtBox(SpriteBatch batch, Entity entity) {
        rendering.drawCollisionBox(batch, entity.getCollisionBox(), 0, 0, 1);
    }
    public void GameMapUpdate() {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }
    public void dispose() {
        mapRenderer.dispose();
    }
}
