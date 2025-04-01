package org.example.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import org.common.UserEntity;
import org.render.Rendering;

import java.util.*;


public class GameMap extends UserEntity {
    private OrthographicCamera camera;
    private TiledMap map_stage;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer groundLayer;
    private UserEntity entity_create;
    protected Rendering rendering;
    private List<UserEntity> entitiesOld = new ArrayList<>();
    private List<UserEntity> entitiesNew = new ArrayList<>();
    private List<Rectangle> collisionBoxes = new ArrayList<>();
    private List<TiledMapTileLayer> layers = new ArrayList<>();
    private List<Float> layerParallaxX = new ArrayList<>();

    public GameMap(OrthographicCamera camera, String GameMapName) {
        this.camera = camera;
        map_stage = new TmxMapLoader().load(GameMapName);
        mapRenderer = new OrthogonalTiledMapRenderer(map_stage);
        groundLayer = (TiledMapTileLayer) map_stage.getLayers().get(2);
        rendering = new Rendering();


        for (int i = 0; i < map_stage.getLayers().size(); i++) {
            layers.add((TiledMapTileLayer) map_stage.getLayers().get(i));
            layerParallaxX.add(1.0f); // Default to no horizontal parallax
        }
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

                        entity_create = new UserEntity(); // Create a new entity
                        entity_create.setCollisionBox(new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight)); // Correct world position
                        entitiesOld.add(entity_create);
                    }
                }
            }
        }
    }
    public void tileMerging() {
        // 1. Horizontal merging (your current method)
        Map<Float, List<UserEntity>> rows = new HashMap<>();
        for (UserEntity e : entitiesOld) {
            float y = e.getCollisionBox().y;
            List<UserEntity> rowEntities = rows.computeIfAbsent(y, k -> new ArrayList<>());
            rowEntities.add(e);
        }

        List<Rectangle> horizontalMerged = new ArrayList<>();
        for (List<UserEntity> rowEntities : rows.values()) {
            rowEntities.sort(Comparator.comparing(e -> e.getCollisionBox().x));
            Rectangle mergedBox = new Rectangle(rowEntities.get(0).getCollisionBox());

            for (int i = 1; i < rowEntities.size(); i++) {
                Rectangle current = rowEntities.get(i).getCollisionBox();
                if (mergedBox.x + mergedBox.width == current.x) {
                    mergedBox.width += current.width;
                } else {
                    horizontalMerged.add(new Rectangle(mergedBox));
                    mergedBox = new Rectangle(current);
                }
            }
            horizontalMerged.add(new Rectangle(mergedBox));
        }

        // 2. Vertical merging (new step)
        horizontalMerged.sort((a, b) -> {
            if (a.y != b.y) return Float.compare(a.y, b.y);
            return Float.compare(a.x, b.x);
        });

        boolean[] merged = new boolean[horizontalMerged.size()];
        for (int i = 0; i < horizontalMerged.size(); i++) {
            if (merged[i]) continue;
            Rectangle current = horizontalMerged.get(i);

            for (int j = i + 1; j < horizontalMerged.size(); j++) {
                if (merged[j]) continue;
                Rectangle next = horizontalMerged.get(j);

                // Check if same X, same width, and stacked vertically
                if (next.x == current.x && next.width == current.width &&
                    next.y == current.y + current.height) {
                    current.height += next.height;
                    merged[j] = true;
                } else break;
            }

            UserEntity mergedEntity = new UserEntity();
            mergedEntity.setCollisionBox(new Rectangle(current));
            entitiesNew.add(mergedEntity);
        }
        entitiesOld.clear();
    }

    public void renderAllCollisionBoxes(SpriteBatch batch) {
        for (UserEntity entity : entitiesNew) {
            renderHurtBox(batch, entity);
        }
    }
    public void renderHurtBox(SpriteBatch batch, UserEntity entity) {
        rendering.drawCollisionBox(batch, entity.getCollisionBox(), 0, 0, 1);
    }
    public void setLayerParallaxX(int layerIndex, float parallaxX){
        if (layerIndex >= 0 && layerIndex < layerParallaxX.size()) {
            layerParallaxX.set(layerIndex, parallaxX);
        }
    }
    public void GameMapUpdate() {
        for (int i = 0; i < layers.size(); i++) {
            // Create a camera copy for this layer
            OrthographicCamera layerCamera = new OrthographicCamera(camera.viewportWidth, camera.viewportHeight);
            layerCamera.position.set(
                camera.position.x * layerParallaxX.get(i), // Horizontal parallax
                camera.position.y,             // Y matches main camera (no parallax)
                0
            );
            layerCamera.zoom = camera.zoom;
            layerCamera.update();

            // Render the layer
            mapRenderer.setView(layerCamera);
            mapRenderer.getBatch().begin();
            mapRenderer.renderTileLayer(layers.get(i));
            mapRenderer.getBatch().end();
        }
    }
    public List<Rectangle> getCollisionBoxes(){
        collisionBoxes.clear();
        for (UserEntity e : entitiesNew) {
            collisionBoxes.add(e.getCollisionBox());
        }
        return collisionBoxes;
    }
    public void dispose() {
        mapRenderer.dispose();
    }
}

