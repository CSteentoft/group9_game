package org.example;

public class Player extends Entity {
    boolean isJumping;
    private Entity entity;

    public Player(float left, float top, float width, float height) {
        super(left, top, width, height);
        isJumping = true;
        entity = new Entity(69, 69, 69, 69);
    }

    public void update(float gravity, float friction) {
        getVelocityX() += gravity;
        getVelocityX() *= friction;
        getVelocityY() *= friction;
        getOldBottom() = getBottom();
        getOldLeft() = getLeft();
        getOldRight() = getRight();
        getOldLeft() = getLeft();
        getLeft() += getVelocityX();
        getTop() += getVelocityY();
        getRight() = getLeft() + getWidth();
        getBottom() = getTop() + getHeight();
    }
}
}
