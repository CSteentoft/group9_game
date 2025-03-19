package io.github.group9;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private List<Rendering> animations; // Store multiple animations

    @Override
    public void create() {
        batch = new SpriteBatch();
        animations = new ArrayList<>();

        // Add multiple animations
        animations.add(new Rendering("Punch_cross.png", 7, 1, 140, 210));
        animations.add(new Rendering("Player_hurt.png", 4, 1, 200, 210));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1); // Clear screen before drawing anything

        batch.begin();
        for (Rendering animation : animations) {
            animation.render(batch);
        }
        batch.end();
    }


    @Override
    public void dispose() {
        batch.dispose();
        for (Rendering animation : animations) {
            animation.dispose();
        }
    }
}
