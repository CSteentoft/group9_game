package org.example;


import com.badlogic.gdx.math.Vector2;
import io.github.group9.Entity;

public class Player extends Entity implements ICommonFighter {
    protected Entity entity;
    protected float stateTime = 0;
    protected boolean isAttacking = false;
    protected boolean isMoving = false;
    protected boolean isDead = false;
    protected boolean isJumping = false;
    protected int health = 100;
    protected boolean isTakingDamage = false;
    protected float velocityY = 0;
    protected float gravity = -0.25f;

    protected Stats stat = new Stats(1,1,1,1);


    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public void takeDamage(int amount) {
        if (!isDead) {
            health -= amount;
            isTakingDamage = true;
            if (health <= 0) {
                health = 0;
                isDead = true;
            }
        }

    }

    @Override
    public void heavyAttack() {

    }

    @Override
    public void lightAttack() {

    }

    @Override
    public void block() {

    }

    @Override
    public void equipWeapon() {

    }

    @Override
    public void dropWeapon() {

    }

    @Override
    public void throwWeapon() {

    }

    @Override
    public void jump() {
        if (!isJumping) {
            isJumping = true;
            velocityY = 9.5f;
        }

    }

    @Override
    public void move(float deltaX) {
        float e = entity.getPosition().x + deltaX * 2f;
        isMoving = true;

        //updateHitbox();

    }

    @Override
    public Stats getStats() {
        return null;
    }

    @Override
    public void setStats(Stats stats) {

    }
}
