package io.github.group9;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.example.*;

import java.util.List;
import java.util.ArrayList;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private GameCamera gameCamera;
    private GameMap gameMap;
    private CollisionHandler collisionHandler;
    private Engine engine;
    private OrthographicCamera camera;

    // Weapon management
    private io.github.group9.WeaponSpawner weaponSpawner;
    private List<weapon> weapons;
    private float weaponSpawnTimer;
    private static final float WEAPON_SPAWN_INTERVAL = 10f; // seconds between spawns
    private static final int MAX_WEAPONS_IN_WORLD = 3;

    @Override
    public void create() {
        // Initialize core game components
        player = new Player();
        batch = new SpriteBatch();
        gameCamera = new GameCamera(640, 360, player.getPosition().x, player.getPosition().y, true);
        gameMap = new GameMap(gameCamera.getCamera(), "map/TEST2.tmx");
        gameMap.generateEntitiesForTiles();
        gameMap.tileMerging();
        collisionHandler = new CollisionHandler();
        engine = new Engine();

        // Initialize weapon spawner and weapons list
        weaponSpawner = new io.github.group9.WeaponSpawner();
        weapons = new ArrayList<>();
        weaponSpawnTimer = WEAPON_SPAWN_INTERVAL;

        // Spawn initial weapons
        for (int i = 0; i < MAX_WEAPONS_IN_WORLD; i++) {
            weapons.add(weaponSpawner.spawnWeapon());
        }
    }

    @Override
    public void render() {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Get delta time for physics calculations
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Update weapon spawn timer
        weaponSpawnTimer -= deltaTime;
        if (weaponSpawnTimer <= 0 && countActiveWeapons() < MAX_WEAPONS_IN_WORLD) {
            weapons.add(weaponSpawner.spawnWeapon());
            weaponSpawnTimer = WEAPON_SPAWN_INTERVAL;
        }

        // Update camera
        if (gameCamera.isFollowingPLayer()) {
            gameCamera.setCameraPos(player.getPosition().x, player.getPosition().y);
        }
        gameCamera.getCamera().update();
        batch.setProjectionMatrix(gameCamera.getCamera().combined);

        // Update game logic
        gameMap.GameMapUpdate();

        // Update weapons physics
        for (weapon w : weapons) {
            if (!w.isPickedUp) {
                Rectangle[] collisionBoxes = gameMap.getCollisionBoxes().toArray(new Rectangle[0]);
                w.update(deltaTime, collisionBoxes);
            }
        }

        // Remove picked up weapons
        weapons.removeIf(w -> w.isPickedUp);

        // Begin rendering
        batch.begin();

        // Render player
        player.update();
        player.render(batch);

        // Render weapons that are not picked up
        for (weapon w : weapons) {
            if (!w.isPickedUp) {
                w.render(batch);
            }
        }

        batch.end();

        // Handle map collisions
        for (Rectangle rectangle : gameMap.getCollisionBoxes()) {
            if (collisionHandler.getCollisionData(player.getHurtBox(), rectangle).collided) {
                player.shiftPosition(
                    collisionHandler.getCollisionData(player.getHurtBox(), rectangle).overlapX,
                    collisionHandler.getCollisionData(player.getHurtBox(), rectangle).overlapY
                );
            }
        }
    }

    private int countActiveWeapons() {
        int count = 0;
        for (weapon w : weapons) {
            if (!w.isPickedUp) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameMap.dispose();
        player.dispose();
        weaponSpawner.dispose();
    }
}
