package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/3/2016.
 */
public class AIComponent implements Component{
    /**
     * value determines which AI it will run
     * r : randomly moves around, and changes directionally to a random direction. Aims at player when they go
     * into their awareness radius <p>
     * f : follows player<p>
     * e : escapes player, and wanders randomly when player is outside target rotation<p>
     * l : escapes player at double speed, and wanders randomly when player is outside<p>
     * a : follows player but escapes when they get within awareness radius<p>
     * w : Moves randomly, always shooting<p>
     * x : follows player but escapes when they get within awareness radius (uses target time to control turning. better for
     *     rotating images<p>
     */
    public char AIType;

    /**
     * tells what state the enemy is in, whether it be turning, following, etc. <p>
     * 0 : moving in straight lines <p>
     * 1 : turning<p>
     * 2 : following<p>
     */
    public int AIState;

    public float targetRotation = 999f;
    public float currenttime;
    public float targetTime;
    public float awarenessRadius;

    //for what it shoots
    public boolean shoots;
    /**
     * l : basic laser <p>
     * e : exploding laser <p>
     * s : swirl <p>
     * w : weak laser <p>
     * p : pink laser <p>
     * g : ghost's laser <p>
     * i : wisp projectile <p>
     * h : shock wave <p>
     * m : homing Missile <p>
     * b : bubble attack <p>
     * f : fire laser <p>
     * c : ice laser <p>
     * t : thunder laser <p>
     * u : multi green arrow <p>
     * o : orange arrow <p>
     * B : blue arrow <p>
     * W : white arrow <p>
     * 1 : blue beam <p>
     * 2 : spinning red <p>
     * 3 : green beam <p>
     * 4 : Turret Laser <p></p>
     */
    public char projectileType;
    public boolean gradualTurning = true;



}
