package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;
import com.colorshooter.game.GameEvent;

/**
 * Created by pnore_000 on 8/2/2016.
 */
public class ItemComponent implements Component{
    public GameEvent event;
    public boolean toggleGetItem = true;
    public boolean disposeAfterUse = true;
    public boolean useableByEnemy = true;
}
