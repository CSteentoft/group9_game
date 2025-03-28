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

        for (Rectangle rectangle : gameMap.getCollisionBoxes()) {
            if (player.getFLOOR_Y() != player.getPosition().y) {
                player.setFLOOR_Y(-5); // Reset if not standing
            }

            // Check collision between the player’s hurtbox and the obstacle.
            if (collisionHandler.checkAABBCollision(player.getHurtBox(), rectangle)) {

                // Calculate the intersection rectangle (penetration).
                Rectangle intersection = new Rectangle();
                Intersector.intersectRectangles(player.getHurtBox(), rectangle, intersection);

                // First handle vertical collisions, then horizontal
                if (intersection.width < intersection.height) {
                    // Resolve horizontally.
                    if (player.getHurtBox().x < rectangle.x) {
                        // Player is to the left of the box.
                        player.setPosition(rectangle.x - player.getHurtBox().width, player.getPosition().y);
                        player.setVelocityX(0); // Stop horizontal movement
                    } else {
                        // Player is to the right of the box.
                        player.setPosition(rectangle.x + rectangle.width, player.getPosition().y);
                        player.setVelocityX(0); // Stop horizontal movement
                    }
                } else {
                    // Resolve vertically only if moving vertically.

                    if (player.getVelocityY() > 0) {
                        // Moving up, check if hitting the ceiling
                        if (player.getHurtBox().y + player.getHurtBox().height > rectangle.y) {
                            player.setPosition(player.getPosition().x, rectangle.y - player.getHurtBox().height);
                            player.setVelocityY(0); // Stop vertical movement (ceiling hit)
                        }
                    } else if (player.getVelocityY() < 0) {
                        // Moving down, check if hitting the ground
                        if (player.getHurtBox().y < rectangle.y + rectangle.height) {
                            player.setVelocityY(0); // Stop downward movement
                            player.setFLOOR_Y(rectangle.y + rectangle.height); // Update floor level
                            player.setPosition(player.getPosition().x, rectangle.y + rectangle.height); // Place player on top of the box
                        }
                    }
                }
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
