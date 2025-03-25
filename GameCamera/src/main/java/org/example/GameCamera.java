package org.example;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameCamera {
    private OrthographicCamera camera;
    private boolean followPLayer;

    public GameCamera(int width, int height, float initialX, float initialY, boolean followPlayer) {
        //Standard Width 640 and Height 360
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.position.set(initialX + 40, initialY + 100, 0);  // Set initial camera position
        camera.update();

        this.followPLayer = followPlayer;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
    public boolean isFollowingPLayer() {
        return followPLayer;
    }
    public void setCameraPos(float x, float y) {
        camera.position.set(x + 40, y + 100, 0);
        camera.update();  // Move update after setting the position
    }


}
