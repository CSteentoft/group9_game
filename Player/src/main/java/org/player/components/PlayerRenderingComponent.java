package org.player.components;

import com.badlogic.ashley.core.Component;
import org.render.Rendering;
import java.util.ArrayList;
import java.util.List;

public class PlayerRenderingComponent implements Component {
    // Indices: 0=idle, 1=run, 2=jump, 3=dash, 4=doubleJump, 5=walk
    public List<Rendering> animations = new ArrayList<>();
    public Rendering currentAnimation = null;
    public int currentIndex = 0;
}


