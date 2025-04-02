package org.enemy.components;

import com.badlogic.ashley.core.Component;
import org.common.UserEntity;

public class EnemyComponent implements Component {
    // Movement & physics constants
    public float horizontalAccel = 900f;
    public float frictionAir     = 1200f;
    public float maxSpeed        = 300f;
    public float jumpVelocity    = 500f;
    public float gravity         = -1300f;
    public float terminalVelocity= -1000f;

    // State
    public int jumpCount = 0;
    public boolean onGround = true;


    //HurtBox
    public final int EnemyWidth = 20; //20
    public final int EnemyHeight = 32; //32
    public float xOffset = 14;
    public float yOffset = 7;

    //Input handling
    public boolean isFlipped;

    //Velocity
    private float velocityX = 0; // Current x velocity
    private float velocityY = 0; // Current y velocity

    //Horizontal movement
    private static final float kEnemyMaxSpeed = 300.0f; // Maximum horizontal speed
    private static final float kEnemyInputAccel = 900f; // Player input acceleration
    private static final float kFrictionAir = 1200.0f; //  // Air friction factor (applied each frame, value between 0 and 1)

    //Jumping
    private static final float JUMP_VELOCITY = 500f; // Initial velocity for the jump

    //Gravity
    private static final float TERMINAL_VELOCITY = -1000f; // Maximum downward velocity, that gravity can make due of
    public float GRAVITY = -1300f; // Acceleration due to gravity
    private float xLeft, xRight; // Boundaries of the platform the player is standing on

    // Reference to underlying UserEntity (the transform)
    public UserEntity userEntity;

    public EnemyComponent(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    // Called when the player lands

}



