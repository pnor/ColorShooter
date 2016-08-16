package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;
import com.colorshooter.game.GameEvent;

/**
 * Created by pnore_000 on 8/2/2016.
 */
public class EventComponent implements Component{
    public float targetTime;
    public float currentTime;
    public boolean repeat;
    public boolean ticking;
    public GameEvent event;
}
