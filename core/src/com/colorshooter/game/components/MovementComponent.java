package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/2/2016.
 */
public class MovementComponent implements Component {
    public float speedPerSecond = 0.0f;
    public float direction = 0f;
    public boolean move = true;
}
