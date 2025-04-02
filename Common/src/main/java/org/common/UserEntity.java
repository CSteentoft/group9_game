package org.common;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.UUID;

public class UserEntity implements Serializable, Component {
    private final UUID id = UUID.randomUUID();
    private Vector2 position = new Vector2(0, 0);
    // Replace Vector2 velocity with two separate float values for x and y
    private float velocityX = 0;
    private float velocityY = 0;
    private Rectangle hitbox = new Rectangle();
    private Rectangle oldHitbox = new Rectangle();
    private Rectangle hurtBox = new Rectangle();

    public String getID() {
        return id.toString();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    // New methods for getting and setting the x component of velocity
    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    // New methods for getting and setting the y component of velocity
    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public Rectangle getOldHitbox() {
        return oldHitbox;
    }

    public void setOldHitbox(Rectangle oldHitbox) {
        this.oldHitbox = oldHitbox;
    }

    public Rectangle getCollisionBox() {
        return hurtBox;
    }

    public void setCollisionBox(Rectangle hurtBox) {
        this.hurtBox = hurtBox;
    }
}
