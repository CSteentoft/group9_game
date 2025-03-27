package io.github.group9;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.example.weapon;
import java.util.ArrayList;
import java.util.List;

public class WeaponSpawner {
    // Weapon type definitions
    public enum WeaponType {
        SWORD("weapon/sword1.png", 0.4f),    // 40% chance
        AXE("weapon/sword2.png", 0.25f),     // 25% chance
        HAMMER("weapon/sword3.png", 0.2f),   // 20% chance
        SPEAR("weapon/sword4.png", 0.1f),    // 10% chance
        DAGGER("weapon/sword5.png", 0.05f);  // 5% chance

        private final String spritePath;
        private final float spawnWeight;

        WeaponType(String spritePath, float spawnWeight) {
            this.spritePath = spritePath;
            this.spawnWeight = spawnWeight;
        }

        public String getSpritePath() {
            return spritePath;
        }
        public float getSpawnWeight() {
            return spawnWeight;
        }
    }

    // Weapon stats ranges
    private static final float MIN_ATTACK_RATE = 0.5f;
    private static final float MAX_ATTACK_RATE = 2.5f;
    private static final int MIN_DAMAGE = 5;
    private static final int MAX_DAMAGE = 25;
    private static final float MIN_ATTACK_RANGE = 1.0f;
    private static final float MAX_ATTACK_RANGE = 3.0f;

    // Spawn configuration
    private static final float SPAWN_AREA_WIDTH = 800;
    private static final float SPAWN_AREA_HEIGHT = 600;
    private static final float MIN_SPAWN_DELAY = 10f;
    private static final float MAX_SPAWN_DELAY = 30f;
    private static final int MAX_WEAPONS_IN_WORLD = 5;

    // State management
    private List<Texture> weaponTextures;
    private float timeUntilNextSpawn;
    private int activeWeapons;

    public WeaponSpawner() {
        weaponTextures = new ArrayList<>();
        resetSpawnTimer();
        activeWeapons = 0;
    }

    /**
     * Spawns a single weapon with randomized properties
     */
    public weapon spawnWeapon() {
        WeaponType weaponType = getWeightedRandomWeaponType();
        Texture weaponTexture = new Texture(Gdx.files.internal(weaponType.getSpritePath()));
        weaponTextures.add(weaponTexture);

        Vector2 spawnPosition = getValidSpawnPosition();

        weapon newWeapon = new weapon();
        newWeapon.attackRate = MathUtils.random(MIN_ATTACK_RATE, MAX_ATTACK_RATE);
        newWeapon.damage = MathUtils.random(MIN_DAMAGE, MAX_DAMAGE);
        newWeapon.attackRange = MathUtils.random(MIN_ATTACK_RANGE, MAX_ATTACK_RANGE);
        newWeapon.position = spawnPosition;
        newWeapon.sprite = new Sprite(weaponTexture);
        newWeapon.isPickedUp = false;

        return newWeapon;
    }

    /**
     * Spawns a weapon at a specific position
     */
    public weapon spawnWeaponAt(float x, float y) {
        weapon newWeapon = spawnWeapon();
        newWeapon.position.set(x, y);
        return newWeapon;
    }

    /**
     * Updates spawn timing and manages automatic spawning
     */
    public void update(float deltaTime, List<weapon> weapons) {
        // Count active (not picked up) weapons
        activeWeapons = (int) weapons.stream().filter(w -> !w.isPickedUp).count();

        // Spawn new weapon if timer expired and under max count
        timeUntilNextSpawn -= deltaTime;
        if (timeUntilNextSpawn <= 0 && activeWeapons < MAX_WEAPONS_IN_WORLD) {
            weapons.add(spawnWeapon());
            resetSpawnTimer();
            activeWeapons++;
        }
    }

    /**
     * Gets a random spawn position that's not too close to edges
     */
    private Vector2 getValidSpawnPosition() {
        return new Vector2(
            MathUtils.random(50, SPAWN_AREA_WIDTH - 50),
            MathUtils.random(50, SPAWN_AREA_HEIGHT - 50)
        );
    }

    /**
     * Gets weapon type with weighted random distribution
     */
    private WeaponType getWeightedRandomWeaponType() {
        float totalWeight = 0;
        for (WeaponType type : WeaponType.values()) {
            totalWeight += type.getSpawnWeight();
        }

        float random = MathUtils.random(0, totalWeight);
        float runningSum = 0;

        for (WeaponType type : WeaponType.values()) {
            runningSum += type.getSpawnWeight();
            if (random <= runningSum) {
                return type;
            }
        }

        return WeaponType.SWORD; // fallback
    }

    private void resetSpawnTimer() {
        timeUntilNextSpawn = MathUtils.random(MIN_SPAWN_DELAY, MAX_SPAWN_DELAY);
    }

    public void dispose() {
        for (Texture texture : weaponTextures) {
            texture.dispose();
        }
        weaponTextures.clear();
    }

    // Getters for configuration (useful for UI/debug)
    public static float getMinSpawnDelay() { return MIN_SPAWN_DELAY; }
    public static float getMaxSpawnDelay() { return MAX_SPAWN_DELAY; }
    public static int getMaxWeaponsInWorld() { return MAX_WEAPONS_IN_WORLD; }
}
