package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;

/**
 * Created by pnore_000 on 7/4/2016.
 */
public class CollisionComponent implements Component{
    public Polygon boundingBox;

    /**
     * determines how OTHER OBJECTS will react when colliding with this.
     * <p>
     * 0 : do nothing <p>
     * 1 : move out of range <p>
     * 2 : puts into bouncing state (with collision) <p>
     * 3 : ricochet <p>
     * 4 : puts into bouncing state (without collision) <p>
     * 5 : destroy thing causing collision <p>
     * 6 : puts only players into bouncing state <p>
     * 7 : puts only the players into bouncing state (without collision) <p>
     * 8 : puts into poison state (without collision) <p>
     * 9 : puts into freeze state (with collision) <p>
     <p>
     * --represents how THIS OBJECT will react when colliding with something <p>
     * 5 : destroys self <p>
     */
    public int collisionReaction;

    public boolean rotateBox = false;
    public boolean unmovable;
}
