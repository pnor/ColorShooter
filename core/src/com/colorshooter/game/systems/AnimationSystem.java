package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.colorshooter.game.components.AnimationComponent;
import com.colorshooter.game.components.ImageComponent;

import static com.colorshooter.game.Mappers.am;
import static com.colorshooter.game.Mappers.im;

/**
 * Created by pnore_000 on 8/1/2016.
 */
public class AnimationSystem extends EntitySystem{
    private ImmutableArray<Entity> entities;
    private Family family;

    public AnimationSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.all(AnimationComponent.class, ImageComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        AnimationComponent anim;
        ImageComponent img;

        for (Entity e : entities) {
            anim = am.get(e);
            img = im.get(e);
            float frameDuration = 0f;

            if (anim.animations.size() <= 0 || anim.animationTime <= 0)
                continue;

            anim.currentTime +=dt;

            if (anim.animate) {
                if (anim.currentTime >= anim.animationTime / anim.animations.get(anim.currentAnimation).size()) {
                    anim.currentTime = 0f;
                    img.texRegion = anim.animations.get(anim.currentAnimation).get(anim.currentIndex);
                    anim.currentIndex += 1;
                    if (anim.currentIndex >= anim.animations.get(anim.currentAnimation).size())
                        anim.currentIndex = 0;
                    if (anim.currentIndex == 0 && !anim.repeat) {
                        anim.animate = false;
                        img.texRegion = anim.baseTextureRegion;
                    }
                }
            }
        }
    }
}
