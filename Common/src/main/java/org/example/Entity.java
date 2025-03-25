package org.example;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
import java.util.UUID;

public class Entity implements Serializable {
    private final UUID id = UUID.randomUUID();
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle hitbox;
    private Rectangle oldHitbox;
    private Rectangle hurtBox;


    public String getID() {
        return id.toString();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setHurtBox(Rectangle hurtBox) {
        this.hurtBox = hurtBox;
    }

    public Rectangle getHurtBox() {
        return hurtBox;
    }

}

