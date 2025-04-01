package org.player.components;

import com.badlogic.ashley.core.Component;
import org.common.UserEntity;

public class PlayerComponent implements Component {
    // Movement & physics constants
    public float horizontalAccel = 900f;
    public float frictionAir     = 1200f;
    public float maxSpeed        = 300f;
    public float jumpVelocity    = 500f;
    public float gravity         = -1300f;
    public float terminalVelocity= -1000f;

    // State
    public int jumpCount = 0;
    public boolean onGround = false;

    // Reference to underlying UserEntity (the transform)
    public UserEntity userEntity;

    public PlayerComponent(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    // Called when the player lands
    public void land() {
        jumpCount = 0;
        onGround = true;
    }
}



