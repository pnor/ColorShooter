package com.colorshooter.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by pnore_000 on 8/13/2016.
 */
public class HUDActor extends Actor{
    private TextureRegion tex;

    public HUDActor(TextureRegion t) {
        super();
        tex = t;
        setSize(tex.getRegionWidth(), tex.getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(tex, getX(), getY(), getWidth(), getHeight());
    }

    public void setTex(TextureRegion t) {
        tex = t;
    }

    public TextureRegion getTex() {
        return tex;
    }
}
