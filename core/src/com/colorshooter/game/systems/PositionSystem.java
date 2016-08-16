package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.colorshooter.game.components.CollisionComponent;
import com.colorshooter.game.components.PositionComponent;

/**
 * Created by pnore_000 on 7/4/2016.
 */
public class PositionSystem extends EntitySystem{

    @Override
    public void addedToEngine(Engine engine) {
    }

    @Override
    public void update(float dt) {
    }

    public static void setOrigins(PositionComponent pos) {
        pos.originX = pos.width / 2;
        pos.originY = pos.height / 2;
    }

    public static void lookAt(PositionComponent pos, float x, float y, CollisionComponent col) {
        float direction = (float) Math.atan2(y - (pos.y + pos.originY), x - (pos.x + pos.originX));
        pos.rotation = (float) Math.toDegrees(direction);
        if (col != null) {
            CollisionSystem.updateBoundingBox(col, 0, 0, pos.rotation);
        }
    }

    public Family getFamily() {
        return null;
    }

    public void updateProcessing(Engine engine) {
        System.out.println("-");
    }
}
