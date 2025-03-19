package org.example.components;

import com.badlogic.ashley.core.Component;

public class MovementComponent implements Component {
    public float speed = 200f;  // world units per second
    public float vx = 0f;      // velocity in X
    public float vy = 0f;      // velocity in Y
}
