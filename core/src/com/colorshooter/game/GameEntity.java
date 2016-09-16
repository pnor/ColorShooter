package com.colorshooter.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.utils.Disposable;
import com.colorshooter.game.systems.PositionSystem;

import java.util.ArrayList;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/2/2016.
 */
public class GameEntity extends Entity {
    private boolean disposed = false;


    public void setDisposed(boolean b) {
        disposed = b;
    }

    public boolean getDisposed() {
        return disposed;
    }

    public boolean dispose() {
        if (disposed) {
            this.removeAll();
            return true;
        }
        return false;
    }

    public void revive() {
        if (hm.has(this)) {
            hm.get(this).health = hm.get(this).maxHealth;
            hm.get(this).invincible = true;
        }
        setDisposed(false);
    }

    public String toString() {
        //debug!
        String output = "";
        if (pim.has(this))
            output = "Player Controlled Entity";
        else if (aim.has(this))
            output = "AI Controlled Entity";
        else
            output = "An Entity";

        return output;
    }

}
