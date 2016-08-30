package com.colorshooter.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by pnore_000 on 7/2/2016.
 */
public class ImageComponent implements Component {
    public TextureRegion texRegion;
    public boolean rotate;

    public static void dispose() {
        atlas.dispose();
        backgroundAtlas.dispose();
    }

    public static TextureAtlas atlas = new TextureAtlas("CSSprites.pack");
    public static TextureAtlas backgroundAtlas = new TextureAtlas("CSBackgrounds.pack");
}
