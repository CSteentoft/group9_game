package org.example;

import java.io.Serializable;

public class Entity implements Serializable {

    private float velocityY, velocityX;

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }

    public float getRight() {
        return right;
    }

    public float getLeft() {
        return left;
    }

    public float getOldTop() {
        return oldTop;
    }

    public float getOldBottom() {
        return oldBottom;
    }

    public float getOldRight() {
        return oldRight;
    }

    public float getOldLeft() {
        return oldLeft;
    }

    private float height, width;
        private float top, bottom, right, left;
        private float oldTop, oldBottom, oldRight, oldLeft;

        public Entity(float left, float top, float width, float height){
            this.left = this.oldLeft = left;
            this.top = this.oldTop = top;
            this.width = width;
            this.height = height;
            this.right = this.oldRight = left + width;
            this.bottom = this.oldBottom = top + height;
            velocityX = 0;
            velocityY = 0;

        }

        public void setTop(float top) {
            this.top = top;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }

        public void setRight(float right) {
            this.right = right;
        }

        public void setLeft(float left) {
            this.left = left;
        }

        public float getVelocityY() {
            return velocityY;
        }

        public float getVelocityX() {
            return velocityX;
        }

}




