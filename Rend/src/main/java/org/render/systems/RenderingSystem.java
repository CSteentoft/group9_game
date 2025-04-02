package org.render.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.common.UserEntity;
import org.render.components.RenderingComponent;

public class RenderingSystem extends IteratingSystem {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    public RenderingSystem(SpriteBatch batch) {
        // Entities must have both RenderingComponent and UserEntity
        super(Family.all(RenderingComponent.class, UserEntity.class).get());
        this.batch = batch;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        RenderingComponent render = entity.getComponent(RenderingComponent.class);
        UserEntity userEntity = entity.getComponent(UserEntity.class);

        // Update animation time
        render.stateTime += deltaTime;

        // Get position from UserEntity
        float posX = userEntity.getPosition().x;
        float posY = userEntity.getPosition().y;

        // Render the current animation frame at the given position
        batch.begin();
        batch.draw(render.currentAnimation.getKeyFrame(render.stateTime, true), posX, posY);
        batch.end();
    }


    public void dispose() {
        shapeRenderer.dispose();
    }
}



