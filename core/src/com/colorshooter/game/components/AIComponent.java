package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/3/2016.
 */
public class AIComponent implements Component{
    public char AIType;
    //value determines which AI it will run
    /*
    r : randomly moves around, and changes directionally to a random direction. Aims at player when they go
    into their awareness radius
    f : follows player
    e : escapes player, and wanders randomly when player is outside target rotation
    l : escapes player at double speed, and wanders randomly when player is outside
    a : follows player but escapes when they get within awareness radius
    w : Moves randomly, always shooting
    x : follows player but escapes when they get within awareness radius (uses target time to control turning. better for
        rotating images
     */
    public int AIState;
    //tells what state the enemy is in, whether it be turning, following, etc.
    /*
    0 : moving in straight lines
    1 : turning
    2 : following
     */

    public float targetRotation = 999f;
    public float currenttime;
    public float targetTime;
    public float awarenessRadius;

    //for what it shoots
    public boolean shoots;
    public char projectileType;
    /*
    l : basic laser
    e : exploding laser
    s : swirl
    w : weak laser
    p : pink laser
    g : ghost's laser
    i : wisp projectile
    h : shock wave
    m : homing Missile
    b : bubble attack
    f : fire laser
    c : ice laser
    t : thunder laser
    u : multi green arrow
     */
    public boolean gradualTurning = true;



}
