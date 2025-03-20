package org.example;

public interface ICommonFighter {
    boolean isAlive();

    void takeDamage(int amount);

    void heavyAttack();

    void lightAttack();

    void block();

    void equipWeapon();

    void dropWeapon();

    void throwWeapon();

    void jump();

    void move(float deltaX);

    Stats getStats();

    void setStats(Stats stats);
}

