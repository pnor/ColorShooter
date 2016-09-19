package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

/**
 * Created by pnore_000 on 8/1/2016.
 */
public class AnimationComponent implements Component{
    public AnimationComponent() {
        animations = new ArrayList<ArrayList<TextureRegion>>();
        animations.add(new ArrayList<TextureRegion>());
    }

    public boolean animate;
    public boolean repeat;

    public float animationTime;
    public float currentTime;

    public TextureRegion baseTextureRegion; //default texture region

    public ArrayList<ArrayList<TextureRegion>> animations;
    public int currentAnimation; //refers to which array list
    public int currentIndex; // refers to which index array list
}
