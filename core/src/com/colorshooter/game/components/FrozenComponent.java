package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 8/8/2016.
 */
public class FrozenComponent implements Component {
    public boolean isFrozen = false;
    public float frozenDuration = 1.0f;
    public float currentDuration = 0.0f;
}
