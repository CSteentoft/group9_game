package org.example.components;


public class SweptCollisionResult {
    public final float time;
    public final float normalX;
    public final float normalY;

    public SweptCollisionResult(float time, float normalX, float normalY) {
        this.time = time;
        this.normalX = normalX;
        this.normalY = normalY;
    }

    @Override
    public String toString() {
        return "SweptCollisionResult{ time=" + time + ", nx=" + normalX + ", ny=" + normalY + " }";
    }
}

