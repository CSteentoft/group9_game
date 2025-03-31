package io.github.group9;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.*;
import com.badlogic.gdx.math.Rectangle;



public class Main extends ApplicationAdapter {

    private Music mohamed;
    private SpriteBatch batch;
    private Player player;
    private GameCamera gameCamera;
    private GameMap gameMap;
    private CollisionHandler collisionHandler;
    @Override
    public void create() {
        mohamed = Gdx.audio.newMusic(Gdx.files.internal("mohamed.mp3"));
        player = new Player( 400, 90);
        batch = new SpriteBatch();
        gameCamera = new GameCamera(640, 360, player.getPosition().x, player.getPosition().y, true);
        gameMap = new GameMap(gameCamera.getCamera(), "map/New4.tmx");
        gameMap.generateEntitiesForTiles();
        gameMap.tileMerging();
        collisionHandler = new CollisionHandler();

        //gameMap.setLayerParallaxX(2, 1.0f); // Collision Layer
        //gameMap.setLayerParallaxX(4, 0.9f); // Leafs
        //gameMap.setLayerParallaxX(0, 0.7f); // Tree back
        //gameMap.setLayerParallaxX(1, 0.8f); // Tree front
        //gameMap.setLayerParallaxX(3, 1.0f); // Ground shadow


    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Update player movement and physics
        player.update();

        // Handle collisions and adjust player position
        handleCollisionsSwept(deltaTime);

        // Update camera to follow corrected player position
        if (gameCamera.isFollowingPLayer()) {
            gameCamera.setCameraPos(player.getPosition().x, player.getPosition().y);
        }
        gameCamera.getCamera().update();
        batch.setProjectionMatrix(gameCamera.getCamera().combined);

        gameMap.GameMapUpdate();

        // Render player at corrected position
        batch.begin();
        player.render(batch);
        batch.end();

        player.renderHurtBox(batch);
        gameMap.renderAllCollisionBoxes(batch);
        // Adjust volume
        mohamed.setVolume(0.00f);
        mohamed.play();
    }

    public void handleCollisionsSwept(float deltaTime) {
        resolveStaticCollisions();

        float remainingTime = 1.0f;
        int maxIterations = 4;
        int iteration = 0;
        Rectangle collidedRect = null;

        while (remainingTime > 0.0f && iteration < maxIterations) {
            iteration++;
            float dx = player.getVelocityX() * deltaTime * remainingTime;
            float dy = player.getVelocityY() * deltaTime * remainingTime;
            Rectangle playerBox = player.getHurtBox();

            // Broad phase check
            Rectangle broadPhaseBox = new Rectangle(
                Math.min(playerBox.x, playerBox.x + dx),
                Math.min(playerBox.y, playerBox.y + dy),
                playerBox.width + Math.abs(dx),
                playerBox.height + Math.abs(dy)
            );

            SweptCollisionResult earliestCollision = null;
            float earliestTime = 1.0f;

            // Narrow phase check
            for (Rectangle rect : gameMap.getCollisionBoxes()) {
                if (!broadPhaseBox.overlaps(rect)) continue;

                SweptCollisionResult result = collisionHandler.sweptAABB(playerBox, dx, dy, rect);
                if (result != null && result.collisionTime < earliestTime) {
                    earliestCollision = result;
                    earliestTime = result.collisionTime;
                    collidedRect = rect;
                }
            }

            if (earliestCollision != null) {
                // Update position
                player.setPosition(
                    player.getPosition().x + dx * earliestCollision.collisionTime,
                    player.getPosition().y + dy * earliestCollision.collisionTime
                );
                //player.updateCollisionBox();

                // Handle collision response
                if (earliestCollision.normalX != 0) {
                    player.setVelocityX(0);
                }
                if (earliestCollision.normalY != 0) {
                    player.setVelocityY(0);
                    // IMPORTANT: Ground detection fix
                    if (earliestCollision.normalY == 1) { // Collision from below (ground)
                        player.updateGroundBounds(collidedRect.x, collidedRect.x + collidedRect.width);
                        player.land();
                    }

                }

                remainingTime *= (1.0f - earliestCollision.collisionTime);
            } else {

                player.updateCollisionBox();
                remainingTime = 0.0f;
            }
        }
    }

    private void resolveStaticCollisions() {
        Rectangle playerBox = player.getHurtBox();
        float maxOverlap = 0;
        float resolveX = 0;
        float resolveY = 0;
        boolean isGroundCollision = false;
        Rectangle collidedRect = null;

        for (Rectangle rect : gameMap.getCollisionBoxes()) {
            if (!playerBox.overlaps(rect)) continue;

            // Calculate overlaps (existing code)
            float overlapLeft = playerBox.x + playerBox.width - rect.x;
            float overlapRight = rect.x + rect.width - playerBox.x;
            float overlapTop = playerBox.y + playerBox.height - rect.y;
            float overlapBottom = rect.y + rect.height - playerBox.y;

            float minX = Math.min(overlapLeft, overlapRight);
            float minY = Math.min(overlapTop, overlapBottom);
            float depth = Math.min(minX, minY);

            if (depth > maxOverlap) {
                maxOverlap = depth;
                collidedRect = rect;

                if (minX < minY) {
                    resolveX = (overlapLeft < overlapRight) ? -minX : minX;
                    resolveY = 0;
                } else {
                    resolveY = (overlapTop < overlapBottom) ? -minY : minY;
                    resolveX = 0;
                    player.updateGroundBounds(collidedRect.x, collidedRect.x + collidedRect.width);
                }
            }
        }

        if (maxOverlap > 0) {
            player.setPosition(
                player.getPosition().x + resolveX,
                player.getPosition().y + resolveY
            );

            // NEW: Handle velocity reset for ceiling collisions
            if (resolveY < 0) {
                // Collision from below (ceiling) → stop upward velocity
                player.setVelocityY(0);
            } else if (resolveY > 0) {
                player.updateGroundBounds(collidedRect.x, collidedRect.x + collidedRect.width);
                player.land();
            }
        }
    }


    @Override
    public void dispose() {
        batch.dispose();
        gameMap.dispose();
        player.dispose();
        // Remove or dispose gameMap if you later use it.
    }
}
