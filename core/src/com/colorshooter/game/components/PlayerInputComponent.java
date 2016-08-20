package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by pnore_000 on 7/7/2016.
 */
public class PlayerInputComponent implements Component{
    public int forward;
    public int back;
    public int right;
    public int left;
    public int shoot;
    public int sprint;
}
