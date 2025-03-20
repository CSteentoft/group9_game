package org.example;

public class Stats {
    private int health;
    private int attackPower;
    private float speed;
    private float jumpForce;

    public Stats(int health, int attackPower, float speed, float jumpForce) {
        this.health = health;
        this.attackPower = attackPower;
        this.speed = speed;
        this.jumpForce = jumpForce;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getJumpForce() {
        return jumpForce;
    }

    public void setJumpForce(float jumpForce) {
        this.jumpForce = jumpForce;
    }
}
