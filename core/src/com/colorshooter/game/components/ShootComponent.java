package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/7/2016.
 */
public class ShootComponent implements Component{
    public float attackDelay;
    public float currentTime;
    public boolean isAttacking;
}
