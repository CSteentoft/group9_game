package org.example;

import com.badlogic.gdx.math.Rectangle;

public class CollisionHandler {

    public CollisionHandler() {

    }

    public boolean checkAABBCollision(Rectangle A, Rectangle B) {
        return (A.x < B.x + B.width) &&
            (A.x + A.width > B.x) &&
            (A.y < B.y + B.height) &&
            (A.y + A.height > B.y);
    }



}
