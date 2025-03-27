package org.example;

import com.badlogic.gdx.math.Rectangle;

public class CollisionHandler {
    public void collideRectangle(Rectangle rectangle) {
        if (b < rectangle.t || t > rectangle.b || l > rectangle.r || r < rectangle.l) return;
        if (b >= rectangle.t && ob < rectangle.ot) {
            setBottom(rectangle.t - 0.1f);
            vy = rectangle.vy;
            jumping = false;
        } else if (t <= rectangle.b && ot > rectangle.ob) {
            setTop(rectangle.b + 0.1f);
            vy = rectangle.vy;
        } else if (r >= rectangle.l && or < rectangle.ol) {
            setRight(rectangle.l - 0.1f);
            vx = rectangle.vx;
        } else if (l <= rectangle.r && ol > rectangle.or) {
            setLeft(rectangle.r + 0.1f);
            vx = rectangle.vx;
        }
    }
}
