package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.ColorComponent;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.components.PositionComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/8/2016.
 */
public class DrawingSystem extends EntitySystem{
    private Family family;
    private ImmutableArray<Entity> entities;
    private GameEntity player;
    public static Batch batch;
    //ShapeRenderer shapes = new ShapeRenderer();

    public DrawingSystem(int priority, GameEntity p, Batch b) {
        super(priority);
        player = p;
        batch = b;
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.all(PositionComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        ImageComponent img;
        PositionComponent pos;

        //player's glow
        if (!player.getDisposed()) {
            pos = pm.get(player);
            ColorComponent col = colm.get(player);
            if (col.color == 'r')
                batch.setColor(Color.RED);
            else if (col.color == 'b')
                batch.setColor(Color.BLUE);
            else if (col.color == 'g')
                batch.setColor(Color.GREEN);
            else if (col.color == 'y')
                batch.setColor(Color.YELLOW);
            else if (col.color == 'p')
                batch.setColor(Color.PINK);
            else if (col.color == 'v')
                batch.setColor(Color.PURPLE);
            else if (col.color == 'o')
                batch.setColor(Color.ORANGE);
            batch.begin();
            batch.draw(ImageComponent.atlas.findRegion("PlayerGlow"), pos.x - ((80 - pos.width) / 2), pos.y - ((80 - pos.height) / 2), pos.originX, pos.originY, 80, 80, 1, 1, 0);
            batch.end();
            batch.setColor(Color.WHITE);
        }


        for (Entity e : entities) {
            pos = pm.get(e);
            //debug box---

            if (cm.has(e)) {
                /*
                shapes.begin(ShapeRenderer.ShapeType.Line);
                shapes.setColor(Color.BLUE);
                if (poim.get(e) != null && poim.get(e).isPoisoned)
                    shapes.setColor(Color.FOREST);
                if (hm.get(e) != null && hm.get(e).invincible)
                    shapes.setColor(Color.WHITE);
                if (fm.get(e) != null && fm.get(e).isFrozen)
                    shapes.setColor(Color.CYAN);
                if (bm.get(e) != null && bm.get(e).isBouncing)
                    shapes.setColor(Color.PINK);
                shapes.polygon(cm.get(e).boundingBox.getTransformedVertices());
                shapes.end();
                */
            }

            //---

            if (!pos.drawable)
                continue;


            if (!im.has(e)) {
                batch.begin();
                batch.draw(ImageComponent.atlas.findRegion("GoldWisp"), pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, pos.rotation);
                batch.end();
            } else {
                img = im.get(e);
                batch.begin();
                if (irm.has(e) && irm.get(e).gotItem) {
                    if (pim.has(e)) {
                        if (img.rotate)
                            batch.draw(ImageComponent.atlas.findRegion("PlayerShipGet"), pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, pos.rotation);
                        else
                            batch.draw(ImageComponent.atlas.findRegion("PlayerShipGet"), pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, 0);
                    } else {
                        batch.setColor(Color.YELLOW);
                        if (img.rotate)
                            batch.draw(img.texRegion, pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, pos.rotation);
                        else
                            batch.draw(img.texRegion, pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, 0);
                        batch.setColor(Color.WHITE);
                    }
                } else if (hm.has(e) && hm.get(e).invincible) {
                    batch.setColor(Color.RED);
                    if (img.rotate)
                        batch.draw(img.texRegion, pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, pos.rotation);
                    else
                        batch.draw(img.texRegion, pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, 0);
                    batch.setColor(Color.WHITE);
                } else if (fm.has(e) && fm.get(e).isFrozen) {
                    batch.setColor(Color.CYAN);
                    if (img.rotate)
                        batch.draw(img.texRegion, pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, pos.rotation);
                    else
                        batch.draw(img.texRegion, pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, 0);
                    batch.setColor(Color.WHITE);
                } else if (aim.has(e) && bm.has(e) && bm.get(e).isBouncing) {
                    batch.setColor(Color.PINK);
                    if (img.rotate)
                        batch.draw(img.texRegion, pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, pos.rotation);
                    else
                        batch.draw(img.texRegion, pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, 0);
                    batch.setColor(Color.WHITE);
                } else {
                    if (img.rotate)
                        batch.draw(img.texRegion, pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, pos.rotation);
                    else
                        batch.draw(img.texRegion, pos.x, pos.y, pos.originX, pos.originY, pos.width, pos.height, 1, 1, 0);
                }
                batch.end();
            }
            batch.begin();
            batch.draw(ImageComponent.atlas.findRegion("MouseIcon"), Gdx.input.getX() - 6, 894 - Gdx.input.getY());
            batch.end();

        }

    }

    public Family getFamily() {
        return family;
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    public void setPlayer(GameEntity p) {
        player = p;
    }
}
