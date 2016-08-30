package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/5/2016.
 */
public class HealthComponent implements Component{
    public int maxHealth;
    public int health;

    public boolean isAlive = true;

    /**
     *  determines what it does on death <p>
     * 0 : does nothing special<p>
     * 1 : explosion<p>
     * 2 : big explosion<p>
     * 3 : player's explosion<p>
     * 4 : generate mini ghost UFO<p>
     * 5 : generate blue explosion
     */
    public int deathAction;

    public float invinciblityDuration;
    public float currentDuration;
    public boolean invincible;
    public char tag;
}
