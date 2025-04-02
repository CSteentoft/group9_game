package io.github.group9;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class CoreResources {
    private static SpriteBatch batch;
    private static Vector2 getPlayerPosition = new Vector2(0, 0); // Starting point

    public static float getVelocityX() {
        return velocityX;
    }

    public static void setVelocityX(float velocityX) {
        CoreResources.velocityX = velocityX;
    }

    public static float getVelocityY() {
        return velocityY;
    }

    public static void setVelocityY(float velocityY) {
        CoreResources.velocityY = velocityY;
    }

    private static float velocityX;
    private static float velocityY;

    public static Rectangle getPlayerHurtBox() {
        return playerHurtBox;
    }

    public static void setPlayerHurtBox(Rectangle playerHurtBox) {
        CoreResources.playerHurtBox = playerHurtBox;
    }

    private static Rectangle playerHurtBox;

    public static void setSpriteBatch(SpriteBatch spriteBatch) {
        batch = spriteBatch;
    }

    public static SpriteBatch getSpriteBatch() {
        return batch;
    }

    public static Vector2 getPlayerPosition() {
        return getPlayerPosition;
    }

    public static void setPlayerPosition(Vector2 vec) {
        getPlayerPosition = vec;

    }

    public static List<Rectangle> getGameMapCollisionBoxes() {
        return gameMapCollisionBoxes;
    }

    public static void setGameMapCollisionBoxes(List<Rectangle> gameMapCollisionBoxes) {
        CoreResources.gameMapCollisionBoxes = gameMapCollisionBoxes;
    }

    private static List<Rectangle> gameMapCollisionBoxes = new ArrayList<>();

    public static OrthographicCamera getOrthographicCamera() {
        return orthographicCamera;
    }

    public static void setOrthographicCamera(OrthographicCamera orthographicCamera) {
        CoreResources.orthographicCamera = orthographicCamera;
    }

    private static OrthographicCamera orthographicCamera;

    public static float getXLeft() {
        return xLeft;
    }

    public static void setXLeft(float xLeft) {
        CoreResources.xLeft = xLeft;
    }

    public static float getXRight() {
        return xRight;
    }

    public static void setXRight(float xRight) {
        CoreResources.xRight = xRight;
    }

    static float xLeft;
    static float xRight;

    public static boolean isLanded() {
        return landed;
    }

    public static void setLanded(boolean landed) {
        CoreResources.landed = landed;
    }

    static boolean landed;

}


