package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/1/2016.
 */
public class PositionComponent implements Component {
    public float x = 0.0f;
    public float y = 0.0f;

    public float originX = 0.0f;
    public float originY = 0.0f;

    public float height = 0.0f;
    public float width = 0.0f;

    public float rotation = 0.0f;

    public boolean drawable = true;
}
