package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/5/2016.
 */
public class BouncingComponent implements Component {
    public boolean isBouncing = false;
    public float bounceDuration = 1.0f;
    public float currentDuration = 0.0f;
}
