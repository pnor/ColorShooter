package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/7/2016.
 */
public class ColorComponent implements Component{
    public char color;
    /*
    x : no color
    r : red
    b : blue
    g : green
    y : yellow
    p : pink
    v : purple
     */

    public char oldColor;
}
