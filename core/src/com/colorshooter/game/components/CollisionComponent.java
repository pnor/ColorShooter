package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;

/**
 * Created by pnore_000 on 7/4/2016.
 */
public class CollisionComponent implements Component{
    public Polygon boundingBox;

    public int collisionReaction;
    /* determines how OTHER OBJECTS will react when colliding with this
    0 : do nothing
    1 : move out of range
    2 : puts into bouncing state (with collision)
    3 : ricochet
    4 : puts into bouncing state (without collision)
    5 : destroy thing causing collision
    6 : puts only players into bouncing state
    7 : puts only the players into bouncing state (without collision)
    8 : puts into poison state (without collision)
    9 : puts into freeze state (with collision)


    --represents how THIS OBJECT will react when colliding with something
    5 : destroys self
     */

    public boolean rotateBox = false;
}
