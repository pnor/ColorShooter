package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 8/7/2016.
 */
public class PoisonComponent implements Component{
    public boolean isPoisoned = false;
    public float poisonDuration = 1.0f;
    public float currentDuration = 0.0f;
}
