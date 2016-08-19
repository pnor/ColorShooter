package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/5/2016.
 */
public class HealthComponent implements Component{
    public int maxHealth;
    public int health;

    public boolean isAlive = true;

    public int deathAction;
    /* determines what it does on death
    0 : does nothing special
    1 : explosion
    2 : big explosion
    3 : player's explosion
     */

    public float invinciblityDuration;
    public float currentDuration;
    public boolean invincible;
    public char tag;
}
