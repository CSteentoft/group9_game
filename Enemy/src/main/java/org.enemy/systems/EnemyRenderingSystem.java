package org.enemy.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.common.UserEntity;
import org.render.components.RenderingComponent;

public class EnemyRenderingSystem extends IteratingSystem {
    private SpriteBatch batch;

    public EnemyRenderingSystem(SpriteBatch batch) {
        super(Family.all(RenderingComponent.class, UserEntity.class).get());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        RenderingComponent rc = entity.getComponent(RenderingComponent.class);
        UserEntity ue = entity.getComponent(UserEntity.class);

        // Update the animation time for the active animation
        rc.stateTime += deltaTime;

        float posX = ue.getPosition().x;
        float posY = ue.getPosition().y;

        //batch.begin();
        batch.draw(rc.currentAnimation.getKeyFrame(rc.stateTime, true), posX, posY);
       // batch.end();
    }



        @Override
        public void update(float deltaTime) {
            batch.begin();
            super.update(deltaTime); // Processes all entities
            batch.end();
        }
}
