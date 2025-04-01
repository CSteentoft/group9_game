package org.player.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.common.UserEntity;
import org.player.components.PlayerRenderingComponent;

public class PlayerRenderingSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private final ComponentMapper<PlayerRenderingComponent> prcm = ComponentMapper.getFor(PlayerRenderingComponent.class);
    private final ComponentMapper<UserEntity> uem = ComponentMapper.getFor(UserEntity.class);

    private SpriteBatch batch;

    public PlayerRenderingSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PlayerRenderingComponent.class, UserEntity.class).get());
    }

    @Override
    public void update(float deltaTime) {
        batch.begin();
        for (Entity e : entities) {
            PlayerRenderingComponent prc = prcm.get(e);
            UserEntity ue = uem.get(e);
            if (prc.currentAnimation != null) {
                prc.currentAnimation.setPosition(ue.getPosition().x, ue.getPosition().y);
                prc.currentAnimation.render(batch);
            }
        }
        batch.end();
    }
}

