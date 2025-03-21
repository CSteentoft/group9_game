package org.example;

import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
import java.util.UUID;

public class Entity implements Serializable {
    private final UUID id = UUID.randomUUID();
    private Vector2 position;
    private boolean isFlipped;
    public String getID() {
        return id.toString();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}

