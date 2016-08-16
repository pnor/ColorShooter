package com.colorshooter.game;

import com.badlogic.ashley.core.Engine;

/**
 * Created by pnore_000 on 8/2/2016.
 */
public interface GameEvent {
    void event(GameEntity e, Engine engine);
}
