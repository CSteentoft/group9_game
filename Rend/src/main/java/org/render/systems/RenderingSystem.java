package org.render.systems;


import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Rectangle;
import org.render.components.RenderingComponent;
import org.common.UserEntityComponent;
import org.common.UserEntity;

public class RenderingSystem extends EntitySystem {
    private final Family family = Family.all(UserEntityComponent.class, RenderingComponent.class).get();

    private ImmutableArray<Entity> entities;

    private final ComponentMapper<UserEntityComponent> uecm = ComponentMapper.getFor(UserEntityComponent.class);
    private final ComponentMapper<RenderingComponent> rcm = ComponentMapper.getFor(RenderingComponent.class);

    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;

    public RenderingSystem(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float deltaTime) {
        batch.begin();

        for (Entity e : entities) {
            UserEntityComponent uec = uecm.get(e);
            RenderingComponent rc = rcm.get(e);

            // Update the animation time
            rc.stateTime += deltaTime;

            // Grab the current frame
            TextureRegion currentFrame = rc.animation.getKeyFrame(rc.stateTime, true);

            // Position from the userEntity
            UserEntity userData = uec.userEntity;
            float x = userData.getPosition().x;
            float y = userData.getPosition().y;

            // Draw the frame
            batch.draw(currentFrame, x, y);
        }

        batch.end();

        // OPTIONAL: Debug collision/hurt boxes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        for (Entity e : entities) {
            UserEntityComponent uec = uecm.get(e);
            UserEntity userData = uec.userEntity;

            // For example, let's draw the hurt box in red
            if (userData.getCollisionBox() != null) {
                Rectangle hb = userData.getCollisionBox();
                shapeRenderer.rect(hb.x, hb.y, hb.width, hb.height);
            }
        }
        shapeRenderer.end();
    }
}


