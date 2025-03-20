package io.github.group9;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private List<Rendering> animations; // animations: 0=idle, 1=run, 2=jump
    private Rendering currentAnimation; // current animation

    // Global character position and movement
    private float xPos, yPos;
    private float xSpeed, ySpeed;
    private boolean isJumping;
    private boolean facingLeft; // for flipping the animation

    // Physics constants
    private static final float GRAVITY = -0.5f;
    private static final float JUMP_STRENGTH = 10f;
    private static final float MAX_JUMP_HEIGHT = 100f; // maximum height reached during jump
    private static final float FRAME_DURATION = 0.066f; // fixed frame duration for animations

    @Override
    public void create() {
        batch = new SpriteBatch();
        animations = new ArrayList<>();

        // Create animations: arguments: file name, columns, rows, default x, default y
        animations.add(new Rendering("Player_idle.png", 10, 1, 0, 0));   // Idle
        animations.add(new Rendering("Player_run.png", 8, 1, 50, 0));      // Run
        animations.add(new Rendering("Player_jump.png", 6, 1, 100, 30));   // Jump

        // Start with idle animation
        currentAnimation = animations.get(0);

        // Initialize global position and speeds
        xPos = 100;
        yPos = 0;
        xSpeed = 0;
        ySpeed = 0;
        isJumping = false;
        facingLeft = false;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        // Process input and update speeds/animation states
        handleMovement();

        // Update global character position
        xPos += xSpeed;
        yPos += ySpeed;

        // Apply gravity if above ground
        if (yPos > 0) {
            ySpeed += GRAVITY;
        } else {
            yPos = 0;
            ySpeed = 0;
            isJumping = false;
        }

        // Update the current animation's position and flip state
        currentAnimation.setPosition(xPos, yPos);
        currentAnimation.setFlip(facingLeft);

        // If the current animation is the jump animation, control its frame based on jump progress.
        if (currentAnimation == animations.get(2)) {  // jump animation
            if (ySpeed > 0) {
                // Ascending
                // If near max jump height (e.g. 90% of MAX_JUMP_HEIGHT), show the second frame; otherwise, first frame.
                if (yPos >= MAX_JUMP_HEIGHT * 0.9f) {
                    currentAnimation.setStateTime(1 * FRAME_DURATION);
                } else {
                    currentAnimation.setStateTime(0); // frame 0
                }
            } else {
                // Descending: map yPos from MAX_JUMP_HEIGHT to 0 onto frames 2-5.
                // When yPos is near MAX_JUMP_HEIGHT, fraction will be small; when near ground, fraction ~1.
                float fraction = (MAX_JUMP_HEIGHT - yPos) / MAX_JUMP_HEIGHT;
                int frameIndex = 2 + (int)(fraction * 4); // 4 frames for falling: indices 2,3,4,5
                frameIndex = Math.min(frameIndex, 5);
                currentAnimation.setStateTime(frameIndex * FRAME_DURATION);
            }
        } else {
            // For non-jump animations, disable external control so they update automatically.
            currentAnimation.disableExternalControl();
        }

        batch.begin();
        currentAnimation.render(batch);
        batch.end();
    }

    private void handleMovement() {
        // Process jump input first (if space is pressed and not already jumping)
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !isJumping) {
            ySpeed = JUMP_STRENGTH;
            isJumping = true;
            // Set jump animation regardless of horizontal keys
            currentAnimation = animations.get(2);
        }

        // Process horizontal movement:
        // If not jumping (or even if jumping, we allow horizontal movement)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -5;
            facingLeft = true;
            // If not jumping, set run animation; if jumping, keep jump animation.
            if (!isJumping) {
                currentAnimation = animations.get(1);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = 5;
            facingLeft = false;
            if (!isJumping) {
                currentAnimation = animations.get(1);
            }
        } else {
            // No horizontal input: stop horizontal movement.
            xSpeed = 0;
            if (!isJumping) {
                currentAnimation = animations.get(0); // Idle animation
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Rendering animation : animations) {
            animation.dispose();
        }
    }
}
