package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by pnore_000 on 7/7/2016.
 */
public class ColorComponent implements Component{
    /**
     x : no color <p>
     r : red <p></p>
     b : blue <p></p>
     g : green<p></p>
     y : yellow<p></p>
     p : pink<p></p>
     v : purple<p></p>
     o : orange<p></p>
     w : white <p></p>
     */
    public char color;

    public char oldColor;
}
