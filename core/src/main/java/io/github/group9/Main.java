package io.github.group9;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import org.example.*;
import com.badlogic.gdx.math.Rectangle;


public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Player player;
    private GameCamera gameCamera;
    private GameMap gameMap;
    private CollisionHandler collisionHandler;
    private Rendering rendering;
    @Override
    public void create() {
        player = new Player();
        batch = new SpriteBatch();
        gameCamera = new GameCamera(640, 360, player.getPosition().x, player.getPosition().y, true);
        gameMap = new GameMap(gameCamera.getCamera(), "map/TEST2.tmx");
        gameMap.generateEntitiesForTiles();
        gameMap.tileMerging();
        collisionHandler = new CollisionHandler();

        rendering = new Rendering();
    }

    @Override
    public void render() {
        // Clear the screen.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera first so that rendering uses the latest camera position.
        if (gameCamera.isFollowingPLayer()){
            gameCamera.setCameraPos(player.getPosition().x, player.getPosition().y);
        }
        gameCamera.getCamera().update();
        batch.setProjectionMatrix(gameCamera.getCamera().combined);

        gameMap.GameMapUpdate();

        batch.begin();
        player.update();
        player.render(batch);
        batch.end();

        player.renderHurtBox(batch);
        gameMap.renderAllCollisionBoxes(batch);



        if (player.getFLOOR_Y() != player.getHurtBox().y - player.getYOffset()) {
            player.setFLOOR_Y(-5); // Reset if not standing
        }


        // Process collisions against each obstacle in the game map.
        for (Rectangle rectangle : gameMap.getCollisionBoxes()) {
            // Check if the player's hurtbox intersects the current rectangle.
            if (collisionHandler.checkAABBCollision(player.getHurtBox(), rectangle)) {
                // Calculate the intersection rectangle to determine penetration depth.
                Rectangle intersection = new Rectangle();
                Intersector.intersectRectangles(player.getHurtBox(), rectangle, intersection);

                if (intersection.width < intersection.height) {
                    resolveHorizontalCollision(rectangle);
                } else {
                    resolveVerticalCollision(rectangle);
                }
            }
        }
    }



    public void resolveHorizontalCollision(Rectangle rectangle) {
        // Precompute the adjusted y position for the player.
        float adjustedY = player.getHurtBox().y - player.getYOffset();

        if (player.getHurtBox().x < rectangle.x) {
            // Collision from the left: place the player to the left of the obstacle.
            player.setPosition(rectangle.x - player.getHurtBox().width - player.getXOffset(), adjustedY);
        } else {
            // Collision from the right: place the player to the right of the obstacle.
            player.setPosition(rectangle.x + rectangle.width - player.getXOffset(), adjustedY);
        }
        // Stop horizontal movement after resolving the collision.
        player.setVelocityX(0);
    }

    public void resolveVerticalCollision(Rectangle rectangle) {
        // Precompute the adjusted x position for the player.
        float adjustedX = player.getHurtBox().x - player.getXOffset();

        if (player.getVelocityY() > 0) { // Moving upward
            // Check if the top of the player's hurtbox exceeds the bottom of the obstacle (ceiling collision).
            if (player.getHurtBox().y + player.getHurtBox().height > rectangle.y) {
                player.setPosition(adjustedX, rectangle.y - player.getHurtBox().height - player.getYOffset());
                player.setVelocityY(0); // Stop upward movement.
            }
        } else if (player.getVelocityY() < 0) { // Moving downward
            // Check if the player's hurtbox collides with the top of the obstacle (ground collision).
            if (player.getHurtBox().y < rectangle.y + rectangle.height) {
                player.setVelocityY(0); // Stop downward movement.
                // Update the floor level for the player (accounting for the yOffset).
                player.setFLOOR_Y(rectangle.y + rectangle.height - player.getYOffset());
                // Position the player on top of the obstacle.
                player.setPosition(adjustedX, rectangle.y + rectangle.height - player.getYOffset());

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
