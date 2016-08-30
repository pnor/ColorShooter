package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/7/2016.
 */
public class DamageComponent implements Component{
    public int damage;

    /**
     * e : hurts enemy<p>
     * p : hurts player<p>
     * n : neutral<p>
     */
    public char tag;
}
