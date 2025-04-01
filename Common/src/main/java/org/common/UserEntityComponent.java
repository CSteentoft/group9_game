package org.common;

import com.badlogic.ashley.core.Component;

/**
 * Wraps your custom data class. This is effectively your \"transform\"
 * plus velocity, collision, etc.
 */
public class UserEntityComponent implements Component {
    public UserEntity userEntity;

    public UserEntityComponent(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
