package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.*;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 8/2/2016.
 */
public class ItemSystem extends EntitySystem{
    private Family playerFam;
    private Family itemFam;
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> items;

    public ItemSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        playerFam = Family.all(PlayerInputComponent.class, PositionComponent.class, ColorComponent.class).get();
        itemFam = Family.all(ItemComponent.class, PositionComponent.class).get();
        players = engine.getEntitiesFor(playerFam);
        items = engine.getEntitiesFor(itemFam);
    }

    @Override
    public void update(float dt) {
        PositionComponent pos;
        PositionComponent pos2;
        boolean collide;

        for (Entity e : players) {
            for (Entity e2 : items) {
                if (e == e2)
                    continue;

                collide = false;
                pos = pm.get(e);
                pos2 = pm.get(e2);

                if (cm.has(e) && cm.has(e2)) {
                    collide = CollisionSystem.checkCollision(cm.get(e), cm.get(e2));
                } else {
                    collide = CollisionSystem.checkCollision(
                            new float[] {
                                    pos.x, pos.y,
                                    pos.x + pos.width, pos.y,
                                    pos.x + pos.width, pos.y + pos.height,
                                    pos.x, pos.y + pos.height },
                            new float[] {
                                    pos2.x, pos2.y,
                                    pos2.x + pos2.width, pos2.y,
                                    pos2.x + pos2.width, pos2.y + pos2.height,
                                    pos2.x, pos2.y + pos2.height} );
                }
                if (collide) {
                    itm.get(e2).event.event((GameEntity) e, getEngine());
                    ((GameEntity) e2).setDisposed(true);
                }
            }
        }
    }
}
