package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.colorshooter.game.components.BouncingComponent;
import com.colorshooter.game.components.ItemReceivableComponent;

import static com.colorshooter.game.Mappers.bm;
import static com.colorshooter.game.Mappers.irm;

/**
 * Created by pnore_000 on 8/31/2016.
 */
public class ItemReceivableSystem extends EntitySystem{
    private Family family;
    private ImmutableArray<Entity> entities;

    public ItemReceivableSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.one(ItemReceivableComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        for (Entity e : entities) {
            updateItemState(irm.get(e), dt);
        }
    }

    public void updateItemState(ItemReceivableComponent ir, float dt) {
        if (ir.gotItem) {
            ir.currentTime += dt;
            if (ir.currentTime >= ir.endTime) {
                ir.gotItem = false;
                ir.currentTime = 0.0f;
            }
        }
    }

    public Family getFamily() {
        return family;
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }
}
