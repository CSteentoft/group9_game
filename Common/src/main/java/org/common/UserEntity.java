package org.common;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
import java.util.UUID;

public class UserEntity implements Serializable, Component {
    private final UUID id = UUID.randomUUID();
    private Vector2 position = new Vector2(0, 0);
    private Vector2 velocity = new Vector2(0, 0);
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
    public Vector2 getVelocity() {
        return velocity;
    }
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
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





