package com.colorshooter.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.colorshooter.game.components.*;
import com.colorshooter.game.systems.*;

import java.util.ArrayList;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/5/2016.
 */
public class EntityConstructors {

    /*
    PLAYER -----------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generatePlayer(float x, float y) {
        GameEntity player = new GameEntity();

        player.add(new PlayerInputComponent());
        player.add(new PositionComponent());
        player.add(new MovementComponent());
        player.add(new ImageComponent());
        player.add(new CollisionComponent());
        player.add(new BouncingComponent());
        player.add(new HealthComponent());
        player.add(new ShootComponent());
        player.add(new ColorComponent());
        player.add(new PoisonComponent());
        player.add(new FrozenComponent());
        player.add(new ItemReceivableComponent());

        PlayerInputComponent controller = pim.get(player);
        controller.forward = Input.Keys.W;
        controller.back = Input.Keys.S;
        controller.right = Input.Keys.D;
        controller.left = Input.Keys.A;
        controller.shoot = Input.Keys.SPACE;
        controller.sprint = Input.Keys.SHIFT_LEFT;

        ImageComponent image = im.get(player);
        image.texRegion = ImageComponent.atlas.findRegion("PlayerShip");
        image.rotate = true;

        PositionComponent position = pm.get(player);
        position.x = x;
        position.y = y;
        position.height = image.texRegion.getRegionHeight();
        position.width = image.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        MovementComponent move = mm.get(player);
        move.speedPerSecond = 320f;
        move.move = false;
        position.rotation = 0f;

        ColorComponent col = colm.get(player);
        col.color = 'x';

        CollisionComponent collision = cm.get(player);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        collision.boundingBox.setOrigin(pm.get(player).x + pm.get(player).originX, pm.get(player).y + pm.get(player).originY);
        collision.collisionReaction = 0;
        collision.rotateBox = true;

        BouncingComponent bounce = bm.get(player);
        bounce.isBouncing = false;
        bounce.bounceDuration = 1.3f;

        PoisonComponent pois = poim.get(player);
        pois.poisonDuration = 10f;
        pois.isPoisoned = false;

        FrozenComponent frozen = fm.get(player);
        frozen.frozenDuration = 2f;
        frozen.isFrozen = false;

        ItemReceivableComponent itemR = irm.get(player);
        itemR.endTime = 0.3f;

        HealthComponent heal = hm.get(player);
        heal.maxHealth = 200;
        heal.health = 200;
        heal.invinciblityDuration = 0.6f;
        heal.isAlive = true;
        heal.tag = 'p';
        heal.deathAction = 3;

        ShootComponent sho = shm.get(player);
        sho.attackDelay = 0.2f;

        return player;
    }

    /*
    Base Entity Constructors -------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    private static GameEntity generateGenericEnemy(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AnimationComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        mm.get(enemy).move = true;

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 1f;

        PositionComponent pos = pm.get(enemy);
        pos.x = x;
        pos.y = y;
        pos.rotation = (float) Math.random() * 360;

        cm.get(enemy).collisionReaction = 1;

        AIComponent AI = aim.get(enemy);
        AI.pushing = false;
        AI.stopDistance = 90f;
        AI.targetRotation = pm.get(enemy).rotation;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.deathAction = 1;
        health.tag = 'e';
        health.invinciblityDuration = 0.09f;

        bm.get(enemy).bounceDuration = 2f;

        enemy.add(new PointsComponent());

        return enemy;
    }

    private static GameEntity generateGenericWisp(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new HealthComponent());
        enemy.add(new DamageComponent());

        MovementComponent mov = mm.get(enemy);
        mov.move = true;

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.rotation = (float) Math.random() * 360;

        AIComponent AI = aim.get(enemy);
        AI.pushing = true;
        AI.targetRotation = position.rotation;
        AI.shoots = false;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 0;

        DamageComponent dam = dm.get(enemy);
        dam.tag = 'p';

        enemy.add(new PointsComponent());

        return enemy;
    }

    private static GameEntity generateGenericShip(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.move = true;

        im.get(enemy).rotate = true;

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.rotation = (float) Math.random() * 360;

        CollisionComponent collision = cm.get(enemy);
        collision.collisionReaction = 1;
        collision.rotateBox = true;

        AIComponent AI = aim.get(enemy);
        AI.pushing = false;
        AI.stopDistance = 50f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        bm.get(enemy).bounceDuration = 1f;

        enemy.add(new PointsComponent());

        return enemy;
    }


    /*
    In-Animate Related -------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generateWall(float x, float y, float width, float height) {
        GameEntity wall = new GameEntity();

        wall.add(new PositionComponent());
        wall.add(new CollisionComponent());

        PositionComponent position = pm.get(wall);
        position.x = x;
        position.y = y;
        position.height = height;
        position.width = width;
        PositionSystem.setOrigins(position);
        position.rotation = 0f;

        CollisionComponent collision = cm.get(wall);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        collision.boundingBox.setOrigin(position.width / 2, position.height / 2);
        collision.collisionReaction = 1;

        return wall;
    }

    public static GameEntity generateObject(float x, float y, float width, float height, TextureRegion t, boolean randomlyRotate) {
        GameEntity obj = new GameEntity();

        obj.add(new PositionComponent());
        obj.add(new CollisionComponent());
        obj.add(new ImageComponent());

        PositionComponent position = pm.get(obj);
        position.x = x;
        position.y = y;
        position.height = height;
        position.width = width;
        PositionSystem.setOrigins(position);
        if (randomlyRotate)
            position.rotation = (float) (Math.random() * 360);
        else
            position.rotation = 0;


        ImageComponent img = im.get(obj);
        img.texRegion = t;
        img.rotate = true;

        CollisionComponent collision = cm.get(obj);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        collision.boundingBox.setOrigin(position.width / 2 + position.x, position.height / 2 + position.y);
        collision.collisionReaction = 1;
        collision.rotateBox = true;
        collision.unmovable = true;

        return obj;
    }

    public static GameEntity generatePushableObject(float x, float y, float width, float height, TextureRegion t) {
        GameEntity obj = new GameEntity();

        obj.add(new PositionComponent());
        obj.add(new CollisionComponent());
        obj.add(new ImageComponent());

        PositionComponent position = pm.get(obj);
        position.x = x;
        position.y = y;
        position.height = height;
        position.width = width;
        PositionSystem.setOrigins(position);
        position.rotation = (float) (Math.random() * 360);

        ImageComponent img = im.get(obj);
        img.texRegion = t;
        img.rotate = true;

        CollisionComponent collision = cm.get(obj);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        collision.boundingBox.setOrigin(position.width / 2 + position.x, position.height / 2 + position.y);
        collision.collisionReaction = 1;
        collision.rotateBox = true;
        collision.unmovable = false;

        return obj;
    }

    public static GameEntity generateMovingObject(float x, float y, float width, float height, TextureRegion t, float speed) {
        GameEntity obj = new GameEntity();

        obj.add(new PositionComponent());
        obj.add(new CollisionComponent());
        obj.add(new ImageComponent());
        obj.add(new AIComponent());
        obj.add(new MovementComponent());

        PositionComponent position = pm.get(obj);
        position.x = x;
        position.y = y;
        position.height = height;
        position.width = width;
        PositionSystem.setOrigins(position);
        position.rotation = (float) (Math.random() * 360);

        ImageComponent img = im.get(obj);
        img.texRegion = t;

        CollisionComponent collision = cm.get(obj);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        collision.boundingBox.setOrigin(position.width / 2 + position.x, position.height / 2 + position.y);
        collision.collisionReaction = 1;
        collision.unmovable = true;

        MovementComponent mov = mm.get(obj);
        mov.speedPerSecond = speed;
        mov.move = true;

        AIComponent ai = aim.get(obj);
        ai.AIType = 'w';
        ai.shoots = false;
        ai.targetTime = 5f;
        ai.gradualTurning = false;

        return obj;
    }

    public static GameEntity generateLaser(float x, float y, float rotation, TextureRegion texReg, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new CollisionComponent());
        laser.add(new LifetimeComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = texReg;
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 1000f;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 5;
        col.rotateBox = true;

        DamageComponent dam = dm.get(laser);
        dam.damage = 10;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 0.5f;

        return laser;
    }
    /*
    Player Related -------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generateExplosionLaser(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new EventComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("EnemyProjectile");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 100f;


        DamageComponent dam = dm.get(laser);
        dam.damage = 1;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.05f;
        ev.ticking = true;
        ev.repeat = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (mm.get(e).speedPerSecond < 1000f) {
                    mm.get(e).speedPerSecond += 150f;
                    dm.get(e).damage += 1;
                } else {
                    e.remove(CollisionComponent.class);
                    e.remove(MovementComponent.class);
                    e.add(new AnimationComponent());
                    e.add(new LifetimeComponent());

                    im.get(e).texRegion = ImageComponent.atlas.findRegion("DeathExplosion");

                    PositionComponent pos = pm.get(e);
                    pos.width = 130;
                    pos.height = 130;
                    PositionSystem.setOrigins(pm.get(e));

                    AnimationComponent anim = am.get(e);
                    anim.animate = true;
                    anim.repeat = true;


                    anim.baseTextureRegion = ImageComponent.atlas.findRegion("DeathExplosion");
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion"));
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion2"));
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion3"));
                    anim.currentAnimation = 0;
                    anim.currentIndex = 0;
                    anim.animationTime = 0.2f;

                    DamageComponent dam = dm.get(e);
                    dam.damage = 61;
                    dam.tag = 'e';

                    em.get(e).targetTime = 0.025f;
                    em.get(e).repeat = true;
                    em.get(e).event = new GameEvent() {
                        @Override
                        public void event(GameEntity e, Engine engine) {
                            if (dm.get(e).damage >= 10)
                                dm.get(e).damage -= 10;
                        }
                    };

                    lfm.get(e).endTime = 1f;
                }
            }
        };

        return laser;
    }

    public static GameEntity generateExplosion(float x, float y) {
        GameEntity explosion = new GameEntity();

        explosion.add(new PositionComponent());
        explosion.add(new ImageComponent());
        explosion.add(new AnimationComponent());
        explosion.add(new DamageComponent());
        explosion.add(new LifetimeComponent());
        explosion.add(new EventComponent());

        ImageComponent img = im.get(explosion);
        img.texRegion = ImageComponent.atlas.findRegion("DeathExplosion");

        AnimationComponent anim = am.get(explosion);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("DeathExplosion");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion3"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent pos = pm.get(explosion);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        PositionSystem.setOrigins(pos);
        pos.rotation = 0f;

        DamageComponent dam = dm.get(explosion);
        dam.damage = 25;
        dam.tag = 'n';

        LifetimeComponent life = lfm.get(explosion);
        life.endTime = 1f;

        EventComponent ev = em.get(explosion);
        ev.targetTime = 0.1f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if ( dm.get(e).damage >= 5)
                    dm.get(e).damage -= 5;
            }
        };

        return explosion;
    }

    public static GameEntity generateBlueExplosion(float x, float y) {
        GameEntity explosion = new GameEntity();

        explosion.add(new PositionComponent());
        explosion.add(new ImageComponent());
        explosion.add(new AnimationComponent());
        explosion.add(new DamageComponent());
        explosion.add(new LifetimeComponent());
        explosion.add(new EventComponent());

        ImageComponent img = im.get(explosion);
        img.texRegion = ImageComponent.atlas.findRegion("FinalDeathExplosion");

        AnimationComponent anim = am.get(explosion);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("FinalDeathExplosion");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("FinalDeathExplosion"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("FinalDeathExplosion2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("FinalDeathExplosion3"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent pos = pm.get(explosion);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        PositionSystem.setOrigins(pos);
        pos.rotation = 0f;

        DamageComponent dam = dm.get(explosion);
        dam.damage = 10;
        dam.tag = 'n';

        LifetimeComponent life = lfm.get(explosion);
        life.endTime = 1f;

        EventComponent ev = em.get(explosion);
        ev.targetTime = 0.1f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if ( dm.get(e).damage >= 5)
                    dm.get(e).damage -= 5;
            }
        };

        return explosion;
    }

    public static GameEntity generateBigExplosion(float x, float y) {
        GameEntity explosion = new GameEntity();

        explosion.add(new PositionComponent());
        explosion.add(new ImageComponent());
        explosion.add(new AnimationComponent());
        explosion.add(new DamageComponent());
        explosion.add(new LifetimeComponent());
        explosion.add(new EventComponent());

        ImageComponent img = im.get(explosion);
        img.texRegion = ImageComponent.atlas.findRegion("BigExplosion");

        AnimationComponent anim = am.get(explosion);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("BigExplosion");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BigExplosion"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BigExplosion2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BigExplosion3"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent pos = pm.get(explosion);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth() + 50;
        pos.height = img.texRegion.getRegionHeight() + 50;
        PositionSystem.setOrigins(pos);
        pos.rotation = 0f;

        DamageComponent dam = dm.get(explosion);
        dam.damage = 15;
        dam.tag = 'n';

        LifetimeComponent life = lfm.get(explosion);
        life.endTime = 1f;

        EventComponent ev = em.get(explosion);
        ev.targetTime = 0.1f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if ( dm.get(e).damage >= 5)
                    dm.get(e).damage -= 5;
            }
        };

        return explosion;
    }

    public static GameEntity generatePlayerExplosion(float x, float y) {
        GameEntity explosion = new GameEntity();

        explosion.add(new PositionComponent());
        explosion.add(new ImageComponent());
        explosion.add(new AnimationComponent());
        explosion.add(new DamageComponent());
        explosion.add(new LifetimeComponent());
        explosion.add(new EventComponent());

        ImageComponent img = im.get(explosion);
        img.texRegion = ImageComponent.atlas.findRegion("PlayerExplosion");

        AnimationComponent anim = am.get(explosion);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("PlayerExplosion");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PlayerExplosion"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PlayerExplosion2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PlayerExplosion3"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent pos = pm.get(explosion);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        PositionSystem.setOrigins(pos);
        pos.rotation = 0f;

        DamageComponent dam = dm.get(explosion);
        dam.damage = 10;
        dam.tag = 'e';

        LifetimeComponent life = lfm.get(explosion);
        life.endTime = 1f;

        EventComponent ev = em.get(explosion);
        ev.targetTime = 0.1f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if ( dm.get(e).damage >= 5)
                    dm.get(e).damage -= 5;
            }
        };

        return explosion;
    }

    public static GameEntity generateMine(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity mine = new GameEntity();

        mine.add(new PositionComponent());
        mine.add(new ImageComponent());
        mine.add(new AnimationComponent());
        mine.add(new EventComponent());

        ImageComponent img = im.get(mine);
        img.texRegion = ImageComponent.atlas.findRegion("GMine1");
        img.rotate = true;

        PositionComponent pos = pm.get(mine);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        AnimationComponent anim = am.get(mine);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("GMine1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("GMine1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("GMine2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.5f;

        EventComponent ev = em.get(mine);
        ev.targetTime = 1.5f;
        ev.ticking = true;
        ev.repeat = false;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                e.add(new DamageComponent());
                e.add(new LifetimeComponent());

                im.get(e).texRegion = ImageComponent.atlas.findRegion("BigExplosion");

                PositionComponent pos = pm.get(e);
                pos.width = 220;
                pos.height = 220;
                pos.x = pos.originX + pos.x - pos.width / 2;
                pos.y = pos.originY + pos.y  - pos.height / 2;
                PositionSystem.setOrigins(pm.get(e));

                AnimationComponent anim = am.get(e);
                anim.animate = true;
                anim.repeat = true;


                anim.baseTextureRegion = ImageComponent.atlas.findRegion("BigExplosion");
                anim.animations.get(0).set(0, ImageComponent.atlas.findRegion("BigExplosion"));
                anim.animations.get(0).set(1, ImageComponent.atlas.findRegion("BigExplosion2"));
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("BigExplosion3"));
                anim.currentAnimation = 0;
                anim.currentIndex = 0;
                anim.animationTime = 0.2f;

                DamageComponent dam = dm.get(e);
                dam.damage = 91;
                dam.tag = 'e';

                em.get(e).targetTime = 0.025f;
                em.get(e).repeat = true;
                em.get(e).event = new GameEvent() {
                    @Override
                    public void event(GameEntity e, Engine engine) {
                        if ( dm.get(e).damage >= 15)
                            dm.get(e).damage -= 15;
                    }
                };

                lfm.get(e).endTime = 1.5f;
            }
        };

        return mine;
    }

    public static GameEntity generateRicochetLaser(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new CollisionComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("YellowLaser");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 800;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 3;
        col.rotateBox = true;

        DamageComponent dam = dm.get(laser);
        dam.damage = 3;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        EventComponent ev = em.get(laser);
        ev.repeat = true;
        ev.ticking = true;
        ev.targetTime = 0.1f;
        int temp = (int) (Math.random() * 4);
        if (temp == 0) {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation += 3f;
                    mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 20;
                    dm.get(e).damage += 6;
                }
            };
        } else if (temp == 1) {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation -= 3f;
                    mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 20;
                    dm.get(e).damage += 5;
                }
            };
        } else if (temp == 2) {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation += 1f;
                    mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 20;
                    dm.get(e).damage += 3;
                }
            };
        } else {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation -= 1f;
                    mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 20;
                    dm.get(e).damage += 5;
                }
            };
        }

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 3.2f;

        return laser;
    }

    public static GameEntity generatePiercingArrow(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new AnimationComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("PierceArrow1");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth() + 5;
        pos.height = img.texRegion.getRegionHeight() + 5;
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 1500f;

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("PierceArrow1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PierceArrow1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PierceArrow2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.1f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 26;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 1f;

        return laser;
    }

    public static GameEntity generateHomingMissile(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity missile = new GameEntity();

        missile.add(new PositionComponent());
        missile.add(new DamageComponent());
        missile.add(new ImageComponent());
        missile.add(new AnimationComponent());
        missile.add(new CollisionComponent());
        missile.add(new MovementComponent());
        missile.add(new EventComponent());

        ImageComponent img = im.get(missile);
        img.texRegion = ImageComponent.atlas.findRegion("Missle1");
        img.rotate = true;

        PositionComponent pos = pm.get(missile);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        CollisionComponent col = cm.get(missile);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 5;
        col.rotateBox = true;

        MovementComponent move = mm.get(missile);
        move.speedPerSecond = 400f;

        AnimationComponent anim = am.get(missile);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Missle1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Missle1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Missle2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.1f;

        DamageComponent dam = dm.get(missile);
        dam.damage = 18;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        EventComponent ev = em.get(missile);
        ev.targetTime = 0.1f;
        ev.ticking = true;
        ev.repeat = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                ImmutableArray<Entity> enemies = engine.getEntitiesFor(Family.all(AIComponent.class, PositionComponent.class).get());
                PositionComponent pos = pm.get(e);
                if (enemies.size() != 0) {
                    int smallestIndex = 0;
                    float smallestDistance = MovementSystem.getDistance(pos, pm.get(enemies.get(0)).x + pm.get(enemies.get(0)).originX, pm.get(enemies.get(0)).y + pm.get(enemies.get(0)).originY);
                    float currentDistance = 9999f;
                    for (int i = 0; i < enemies.size(); i++) {
                        currentDistance = Math.abs(MovementSystem.getDistance(pos, pm.get(enemies.get(i)).x + pm.get(enemies.get(i)).originX, pm.get(enemies.get(i)).y + pm.get(enemies.get(i)).originY));
                        if (currentDistance <= smallestDistance) {
                            smallestIndex = i;
                            smallestDistance = currentDistance;
                        }
                    }
                    PositionSystem.lookAt(pos, pm.get(enemies.get(smallestIndex)).x + pm.get(enemies.get(smallestIndex)).originX, pm.get(enemies.get(smallestIndex)).y + pm.get(enemies.get(smallestIndex)).originY, null);
                }

                mm.get(e).speedPerSecond += 80;
                if (mm.get(e).speedPerSecond >= 1150) {
                    e.remove(CollisionComponent.class);
                    e.remove(MovementComponent.class);
                    e.add(new AnimationComponent());
                    e.add(new LifetimeComponent());

                    im.get(e).texRegion = ImageComponent.atlas.findRegion("DeathExplosion");

                    pm.get(e).width = 80;
                    pm.get(e).height = 80;
                    PositionSystem.setOrigins(pm.get(e));

                    AnimationComponent anim = am.get(e);
                    anim.animate = true;
                    anim.repeat = true;


                    anim.baseTextureRegion = ImageComponent.atlas.findRegion("DeathExplosion");
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion"));
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion2"));
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion3"));
                    anim.currentAnimation = 0;
                    anim.currentIndex = 0;
                    anim.animationTime = 0.2f;

                    DamageComponent dam = dm.get(e);
                    dam.damage = 23;
                    dam.tag = 'n';

                    em.get(e).targetTime = 0.1f;
                    em.get(e).event = new GameEvent() {
                        @Override
                        public void event(GameEntity e, Engine engine) {
                            if ( dm.get(e).damage >= 2)
                                dm.get(e).damage -= 1;
                        }
                    };

                    lfm.get(e).endTime = 0.7f;
                }
            }
        };

        return missile;
    }

    public static GameEntity generatePinkLaser(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new CollisionComponent());
        laser.add(new LifetimeComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("PinkLaser");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 1500f;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 4;
        col.rotateBox = true;

        DamageComponent dam = dm.get(laser);
        dam.damage = 15;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 0.5f;

        return laser;
    }

    public static GameEntity generateOrangeArrow(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("OrangeDart");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 100;

        DamageComponent dam = dm.get(laser);
        dam.damage = 25;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.1f;
        ev.repeat = true;
        ev.ticking = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 6;
                pm.get(e).rotation += 36f;
            }
        };

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 4f;

        return laser;
    }

    public static GameEntity generateOrangeArrowSeed(float x, float y, float rotation) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = 1;
        pos.height = 1;
        pos.rotation = rotation;
        pos.drawable = false;
        PositionSystem.setOrigins(pos);

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.1f;
        ev.currentTime = 0.2f;
        ev.repeat = false;
        ev.ticking = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                PositionComponent pos= pm.get(e);
                for (int i = 0; i < 12; i++) {
                    engine.addEntity(generateOrangeArrow(pos.x + pos.originX, pos.y + pos.originY,i * 30, 0));
                }
            }
        };

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 0.15f;

        return laser;
    }

    public static GameEntity generateWhiteBeam(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("WhiteBeam");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 1000f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 40;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 0.25f;

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.24f;
        ev.ticking = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                PositionComponent pos = pm.get(e);
                for (int i = 0; i < 9; i++) {
                    engine.addEntity(generateCurveWhiteBeam(pos.x + pos.originX, pos.y + pos.originY, i * 40, 0));
                }
            }
        };

        return laser;
    }

    public static GameEntity generateCurveWhiteBeam(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());
        laser.add(new AnimationComponent());
        laser.add(new CollisionComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("Laser");
        img.rotate = true;

        AnimationComponent anim = am.get(laser);
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Laser");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Laser"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("LightBlueLaser"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PinkLaser"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("YellowLaser"));
        anim.animate = true;
        anim.animationTime = 0.3f;
        anim.repeat =  true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 80;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 0;
        col.unmovable = true;
        col.rotateBox = true;

        DamageComponent dam = dm.get(laser);
        dam.damage = 15;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.1f;
        ev.repeat = true;
        ev.ticking = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                pm.get(e).rotation += 20;
                mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 3;
            }
        };

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 1f;

        return laser;
    }


    /*
    Enemy Related -------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generateEnemyExplosionLaser(float x, float y, float rotation) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new EventComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("Sparkle");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 600f;

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.5f;
        ev.ticking = true;
        ev.repeat = false;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                e.remove(CollisionComponent.class);
                e.remove(MovementComponent.class);
                e.add(new AnimationComponent());
                e.add(new LifetimeComponent());
                e.add(new DamageComponent());

                im.get(e).texRegion = ImageComponent.atlas.findRegion("DeathExplosion");

                pm.get(e).width = 100;
                pm.get(e).height = 100;
                PositionSystem.setOrigins(pm.get(e));

                AnimationComponent anim = am.get(e);
                anim.animate = true;
                anim.repeat = true;


                anim.baseTextureRegion = ImageComponent.atlas.findRegion("DeathExplosion");
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion"));
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion2"));
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion3"));
                anim.currentAnimation = 0;
                anim.currentIndex = 0;
                anim.animationTime = 0.2f;

                DamageComponent dam = dm.get(e);
                dam.damage = 20;
                dam.tag = 'p';

                em.get(e).targetTime = 0.1f;
                em.get(e).repeat = true;
                em.get(e).event = new GameEvent() {
                    @Override
                    public void event(GameEntity e, Engine engine) {
                        if ( dm.get(e).damage >= 8)
                            dm.get(e).damage -= 8;
                    }
                };

                lfm.get(e).endTime = 1f;
            }
        };

        return laser;
    }

    public static GameEntity generateSwirl(float x, float y, float rotation) {
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new EventComponent());
        laser.add(new AnimationComponent());
        laser.add(new DamageComponent());
        laser.add(new CollisionComponent());
        laser.add(new LifetimeComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("BEnemyProjectile1");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 100f;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 7;
        col.rotateBox = true;
        col.unmovable = true;

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;


        anim.baseTextureRegion = ImageComponent.atlas.findRegion("BEnemyProjectile1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BEnemyProjectile1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BEnemyProjectile2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 5;
        dam.tag = 'p';

        lfm.get(laser).endTime = 8f;

        return laser;
    }

    public static GameEntity generateWeakLaser(float x, float y, float rotation, TextureRegion texReg, int hurtsPlayer) {
        GameEntity laser = generateLaser(x, y, rotation, texReg, hurtsPlayer);
        dm.get(laser).damage = 4;

        return laser;
    }

    public static GameEntity generateEnemyPinkLaser(float x, float y, float rotation, int hurtsPlayer) {
        GameEntity laser = generateLaser(x, y, rotation, ImageComponent.atlas.findRegion("PEnemyProjectile"), hurtsPlayer);
        dm.get(laser).damage = 9;
        cm.get(laser).collisionReaction = 7;
        mm.get(laser).speedPerSecond = 180f;
        lfm.get(laser).endTime = 1.8f;

        laser.add(new EventComponent());
        laser.add(new HealthComponent());

        HealthComponent health = hm.get(laser);
        health.maxHealth = 10;
        health.health = 10;
        health.deathAction = 0;
        health.tag = 'e';

        EventComponent ev = em.get(laser);
        ev.repeat = true;
        ev.ticking = true;
        ev.targetTime = 0.1f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(PlayerInputComponent.class, PositionComponent.class).get());
                PositionComponent pos = pm.get(e);
                if (players.size() == 1) {
                    PositionSystem.lookAt(pos, pm.get(players.get(0)).x + pm.get(players.get(0)).originX, pm.get(players.get(0)).y + pm.get(players.get(0)).originY, cm.get(e));
                    if (CollisionSystem.checkCollision(cm.get(e), cm.get(players.get(0))))
                        e.setDisposed(true);
                    return;
                }
                if (players.size() != 0) {
                    int smallestIndex = 0;
                    float smallestDistance = MovementSystem.getDistance(pos, pm.get(players.get(0)).x + pm.get(players.get(0)).originX, pm.get(players.get(0)).y + pm.get(players.get(0)).originY);
                    float currentDistance = 0f;
                    for (int i = 1; i < players.size(); i++) {
                        currentDistance = MovementSystem.getDistance(pos, pm.get(players.get(0)).x + pm.get(players.get(0)).originX, pm.get(players.get(0)).y + pm.get(players.get(0)).originY);
                        if (currentDistance < smallestDistance) {
                            smallestIndex = i;
                            smallestDistance = currentDistance;
                        }
                    }
                    PositionSystem.lookAt(pos, pm.get(players.get(smallestIndex)).x + pm.get(players.get(smallestIndex)).originX, pm.get(players.get(smallestIndex)).y + pm.get(players.get(smallestIndex)).originY, cm.get(e));
                    if (CollisionSystem.checkCollision(cm.get(e), cm.get(players.get(smallestIndex))))
                        e.setDisposed(true);
                }
            }
        };

        return laser;
    }

    public static GameEntity generateGhostLaser(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("Laser");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 1000f;


        DamageComponent dam = dm.get(laser);
        dam.damage = 15;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 0.5f;

        return laser;
    }

    public static GameEntity generateWispProjectile(float x, float y, float rotation, TextureRegion texReg, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = texReg;
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 80f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 10;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 10f;

        return laser;
    }

    public static GameEntity generatePoisonWispProjectile(float x, float y, float rotation) {
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new EventComponent());
        laser.add(new AnimationComponent());
        laser.add(new DamageComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("Poison");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 10f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 18;
        dam.tag = 'p';

        EventComponent ev = em.get(laser);
        ev.repeat = true;
        ev.ticking = true;
        ev.targetTime = 0.2f;
        if ((int)  (Math.random() * 2) == 0) {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation += 3f + (float) (Math.random() * 20);
                    mm.get(e).speedPerSecond += 20f;

                }
            };
        } else {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation -= 3f+ (float) (Math.random() * 20);
                    mm.get(e).speedPerSecond += 20f;
                }
            };
        }

        lfm.get(laser).endTime = 12f;

        return laser;
    }

    public static GameEntity generateShockWave(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new AnimationComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("Spark1");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 500f;

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Spark1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Spark1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Spark2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 35;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 3f;

        EventComponent ev = em.get(laser);
        ev.repeat = true;
        ev.ticking = true;
        ev.targetTime = 0.1f;
        if ((int)  (Math.random() * 2) == 0) {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation += 5f;
                    mm.get(e).speedPerSecond += 10f;
                }
            };
        } else {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation -= 5f;
                    mm.get(e).speedPerSecond += 10f;
                }
            };
        }

        return laser;
    }

    public static GameEntity generateEnemyHomingMissile(float x, float y, float rotation) {
        GameEntity missile = new GameEntity();

        missile.add(new PositionComponent());
        missile.add(new DamageComponent());
        missile.add(new ImageComponent());
        missile.add(new AnimationComponent());
        missile.add(new CollisionComponent());
        missile.add(new MovementComponent());
        missile.add(new EventComponent());
        missile.add(new HealthComponent());
        missile.add(new BouncingComponent());

        ImageComponent img = im.get(missile);
        img.texRegion = ImageComponent.atlas.findRegion("Missle1");
        img.rotate = true;

        PositionComponent pos = pm.get(missile);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth() + 5;
        pos.height = img.texRegion.getRegionHeight() + 5;
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        CollisionComponent col = cm.get(missile);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 0;
        col.rotateBox = true;

        MovementComponent move = mm.get(missile);
        move.speedPerSecond = 25f;

        AnimationComponent anim = am.get(missile);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Missle1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Missle1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Missle2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.1f;

        DamageComponent dam = dm.get(missile);
        dam.damage = 25;
        dam.tag = 'n';

        HealthComponent heal = hm.get(missile);
        heal.maxHealth = 80;
        heal.health = 80;
        heal.tag = 'e';
        heal.invinciblityDuration = 0.04f;
        heal.deathAction = 1;

        bm.get(missile).bounceDuration = 20f;

        EventComponent ev = em.get(missile);
        ev.targetTime = 0.1f;
        ev.ticking = true;
        ev.repeat = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(PlayerInputComponent.class, PositionComponent.class).get());
                PositionComponent pos = pm.get(e);

                if (players.size() != 0) {
                    int smallestIndex = 0;
                    float smallestDistance = MovementSystem.getDistance(pos, pm.get(players.get(0)).x + pm.get(players.get(0)).originX, pm.get(players.get(0)).y + pm.get(players.get(0)).originY);
                    float currentDistance = 0f;
                    for (int i = 1; i < players.size(); i++) {
                        currentDistance = MovementSystem.getDistance(pos, pm.get(players.get(0)).x + pm.get(players.get(0)).originX, pm.get(players.get(0)).y + pm.get(players.get(0)).originY);
                        if (currentDistance < smallestDistance) {
                            smallestIndex = i;
                            smallestDistance = currentDistance;
                        }
                    }
                    PositionSystem.lookAt(pos, pm.get(players.get(smallestIndex)).x + pm.get(players.get(smallestIndex)).originX, pm.get(players.get(smallestIndex)).y + pm.get(players.get(smallestIndex)).originY, cm.get(e));
                    if (CollisionSystem.checkCollision(cm.get(e), cm.get(players.get(smallestIndex))))
                        e.setDisposed(true);
                }

                mm.get(e).speedPerSecond += 25;
                if (mm.get(e).speedPerSecond >= 500) {
                    e.remove(CollisionComponent.class);
                    e.remove(MovementComponent.class);
                    e.remove(BouncingComponent.class);
                    e.add(new AnimationComponent());
                    e.add(new LifetimeComponent());

                    im.get(e).texRegion = ImageComponent.atlas.findRegion("DeathExplosion");

                    pm.get(e).width = 80;
                    pm.get(e).height = 80;
                    PositionSystem.setOrigins(pm.get(e));

                    AnimationComponent anim = am.get(e);
                    anim.animate = true;
                    anim.repeat = true;


                    anim.baseTextureRegion = ImageComponent.atlas.findRegion("DeathExplosion");
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion"));
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion2"));
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion3"));
                    anim.currentAnimation = 0;
                    anim.currentIndex = 0;
                    anim.animationTime = 0.2f;

                    DamageComponent dam = dm.get(e);
                    dam.damage = 23;
                    dam.tag = 'p';

                    em.get(e).targetTime = 0.1f;
                    em.get(e).event = new GameEvent() {
                        @Override
                        public void event(GameEntity e, Engine engine) {
                            if ( dm.get(e).damage >= 2)
                                dm.get(e).damage -= 1;
                        }
                    };

                    lfm.get(e).endTime = 0.7f;
                }
            }
        };

        missile.add(new PointsComponent());
        pom.get(missile).points = 50;

        return missile;
    }

    public static GameEntity generateBubbleAttack(float x, float y, float rotation) {
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());
        laser.add(new HealthComponent());
        laser.add(new EventComponent());
        laser.add(new AnimationComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("YBubble");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("YBubble");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("YBubble"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PBubble"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 30f;


        DamageComponent dam = dm.get(laser);
        dam.damage = 20;
        dam.tag = 'p';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 20f;

        HealthComponent health = hm.get(laser);
        health.maxHealth = 100;
        health.health = 100;
        health.deathAction = 1;
        health.tag = 'e';

        EventComponent ev = em.get(laser);
        ev.repeat = true;
        ev.ticking = true;
        ev.targetTime = 0.1f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(PlayerInputComponent.class, PositionComponent.class).get());
                PositionComponent pos = pm.get(e);
                PositionComponent pos2;

                if (players.size() != 0) {
                    int smallestIndex = 0;
                    float smallestDistance = MovementSystem.getDistance(pos, pm.get(players.get(0)).x + pm.get(players.get(0)).originX, pm.get(players.get(0)).y + pm.get(players.get(0)).originY);
                    float currentDistance = 0f;
                    for (int i = 1; i < players.size(); i++) {
                        currentDistance = MovementSystem.getDistance(pos, pm.get(players.get(0)).x + pm.get(players.get(0)).originX, pm.get(players.get(0)).y + pm.get(players.get(0)).originY);
                        if (currentDistance < smallestDistance) {
                            smallestIndex = i;
                            smallestDistance = currentDistance;
                        }
                    }
                    PositionSystem.lookAt(pos, pm.get(players.get(smallestIndex)).x + pm.get(players.get(smallestIndex)).originX, pm.get(players.get(smallestIndex)).y + pm.get(players.get(smallestIndex)).originY, cm.get(e));
                    pos2 = pm.get(players.get(smallestIndex));
                    boolean  collide = CollisionSystem.checkCollision(
                            new float[] {
                                    pos.x, pos.y,
                                    pos.x + pos.width, pos.y,
                                    pos.x + pos.width, pos.y + pos.height,
                                    pos.x, pos.y + pos.height },
                            new float[] {
                                    pos2.x, pos2.y,
                                    pos2.x + pos2.width, pos2.y,
                                    pos2.x + pos2.width, pos2.y + pos2.height,
                                    pos2.x, pos2.y + pos2.height} );
                    if (collide) {
                        if (bm.has(players.get(smallestIndex)) && poim.has(players.get(smallestIndex))) {
                            bm.get(players.get(smallestIndex)).isBouncing = true;
                            poim.get(players.get(smallestIndex)).isPoisoned = true;
                        }
                        e.setDisposed(true);
                    }
                }
            }
        };

        laser.add(new PointsComponent());
        pom.get(laser).points = 10;

        return laser;
    }

    public static GameEntity generateFireLaser(float x, float y, float rotation) {
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new EventComponent());
        laser.add(new AnimationComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("Fire1");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Fire1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Fire1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Fire2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 500f;

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.5f;
        ev.ticking = true;
        ev.repeat = false;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                e.remove(CollisionComponent.class);
                e.remove(MovementComponent.class);
                e.add(new LifetimeComponent());
                e.add(new DamageComponent());

                im.get(e).texRegion = ImageComponent.atlas.findRegion("DeathExplosion");

                pm.get(e).width = 100;
                pm.get(e).height = 100;
                PositionSystem.setOrigins(pm.get(e));

                AnimationComponent anim = am.get(e);
                anim.animate = true;
                anim.repeat = true;


                anim.baseTextureRegion = ImageComponent.atlas.findRegion("DeathExplosion");
                anim.animations.get(0).set(0, ImageComponent.atlas.findRegion("DeathExplosion"));
                anim.animations.get(0).set(1, ImageComponent.atlas.findRegion("DeathExplosion2"));
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion3"));
                anim.currentAnimation = 0;
                anim.currentIndex = 0;
                anim.animationTime = 0.2f;

                DamageComponent dam = dm.get(e);
                dam.damage = 38;
                dam.tag = 'p';

                em.get(e).targetTime = 0.1f;
                em.get(e).event = new GameEvent() {
                    @Override
                    public void event(GameEntity e, Engine engine) {
                        if ( dm.get(e).damage >= 8)
                            dm.get(e).damage -= 8;
                    }
                };

                lfm.get(e).endTime = 0.3f;
            }
        };

        return laser;
    }

    public static GameEntity generateIceLaser(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new CollisionComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());
        laser.add(new AnimationComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("Ice");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 600f;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 9;
        col.rotateBox = true;

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;


        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Ice");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Ice"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Ice2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 10;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 2f;

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.1f;
        ev.repeat = true;
        ev.ticking = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (mm.get(e).speedPerSecond >= 70f)
                mm.get(e).speedPerSecond -= 70f;
            }
        };

        return laser;
    }

    public static GameEntity generateThunderLaser(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());
        laser.add(new AnimationComponent());
        laser.add(new ItemComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("Thunder1");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 50f;

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;


        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Thunder1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Thunder1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Thunder2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 18;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 2f;

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.1f;
        ev.repeat = true;
        ev.ticking = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
            mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 2;
        }
        };

        ItemComponent item = itm.get(laser);
        item.toggleGetItem = false;
        item.useableByEnemy = false;
        item.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (hm.get(e).maxHealth >= 5)
                    hm.get(e).maxHealth -= 4;
            }
        };

        return laser;
    }

    public static GameEntity generateGreenArrow(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("GreenDart");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 300f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 25;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 4f;

        return laser;
    }

    public static GameEntity generateBlueArrow(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("BlueDart");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 20;

        DamageComponent dam = dm.get(laser);
        dam.damage = 40;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.1f;
        ev.repeat = true;
        ev.ticking = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 3;
            }
        };

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 3f;

        return laser;
    }

    public static GameEntity generateWhiteArrow(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new CollisionComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("WhiteDart");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 600f;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 11;
        col.rotateBox = true;
        col.unmovable = true;

        DamageComponent dam = dm.get(laser);
        dam.damage = 15;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 6f;

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.1f;
        ev.repeat = true;
        ev.ticking = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (mm.get(e).speedPerSecond >= 100f)
                    mm.get(e).speedPerSecond -= 55f;
            }
        };

        return laser;
    }

    public static GameEntity generateGhostArrow(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());
        laser.add(new CollisionComponent());
        laser.add(new HealthComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("GhostDart");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 5;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 11;
        col.unmovable = true;
        col.rotateBox = true;

        HealthComponent heal = hm.get(laser);
        heal.deathAction = 0;
        heal.maxHealth = 15;
        heal.health = 15;
        heal.invinciblityDuration = 0.09f;
        heal.tag = 'e';

        DamageComponent dam = dm.get(laser);
        dam.damage = 35;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.1f;
        ev.repeat = true;
        ev.ticking = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 5;
                pm.get(e).rotation += 35f;
            }
        };

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 8f;

        return laser;
    }

    public static GameEntity generateCoreLaser(float x, float y, float rotation) {
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new EventComponent());
        laser.add(new AnimationComponent());
        laser.add(new DamageComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("Thunder1");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;


        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Thunder1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Thunder1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Thunder2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.07f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 20;
        dam.tag = 'p';

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 600f;

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.1f;
        ev.ticking = true;
        ev.repeat = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                MovementComponent move = mm.get(e);
                move.speedPerSecond -= move.speedPerSecond / 10f;
                pm.get(e).rotation += 3 * (move.speedPerSecond / 100);
                if (move.speedPerSecond <= 100f) {
                    e.remove(CollisionComponent.class);
                    e.remove(MovementComponent.class);
                    e.add(new LifetimeComponent());

                    im.get(e).texRegion = ImageComponent.atlas.findRegion("Spark1");

                    pm.get(e).width = 96;
                    pm.get(e).height = 96;
                    PositionSystem.setOrigins(pm.get(e));

                    AnimationComponent anim = am.get(e);
                    anim.animate = true;
                    anim.repeat = true;


                    anim.baseTextureRegion = ImageComponent.atlas.findRegion("Spark1");
                    anim.animations.get(0).set(0, ImageComponent.atlas.findRegion("Spark1"));
                    anim.animations.get(0).set(1, ImageComponent.atlas.findRegion("Spark2"));
                    anim.currentAnimation = 0;
                    anim.currentIndex = 0;
                    anim.animationTime = 0.15f;

                    DamageComponent dam = dm.get(e);
                    dam.damage = 55;
                    dam.tag = 'p';

                    em.get(e).targetTime = 0.15f;
                    em.get(e).event = new GameEvent() {
                        @Override
                        public void event(GameEntity e, Engine engine) {
                            pm.get(e).rotation += 90;
                            if (dm.get(e).damage >= 5)
                                dm.get(e).damage -= 3;
                        }
                    };

                    lfm.get(e).endTime = 0.5f;
                }
            }
        };

        return laser;
    }

    public static GameEntity generateBlueBeam(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new CollisionComponent());
        laser.add(new LifetimeComponent());
        laser.add(new AnimationComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("BlueBeam");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 800f;

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("BlueBeam");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BlueBeam"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BlueBeam2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 7;
        col.unmovable = true;
        col.rotateBox = true;

        DamageComponent dam = dm.get(laser);
        dam.damage = 60;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 2f;

        return laser;
    }

    public static GameEntity generateRedSpinner(float x, float y, float rotation) {
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new EventComponent());
        laser.add(new AnimationComponent());
        laser.add(new DamageComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("RedSpin");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;


        anim.baseTextureRegion = ImageComponent.atlas.findRegion("RedSpin");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedSpin"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedSpin2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedSpin3"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 20;
        dam.tag = 'p';

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 100f;

        EventComponent ev = em.get(laser);
        ev.targetTime = .1f;
        ev.ticking = true;
        ev.repeat = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 4;
                if (mm.get(e).speedPerSecond >= 2000f) {
                    PositionComponent pos = pm.get(e);
                    for (int i = 0; i < 8; i++) {
                        engine.addEntity(generateFireLaser(pos.x + pos.originX, pos.y + pos.originY, i * 45));
                    }
                    ((GameEntity) e).setDisposed(true);
                }
            }
        };

        return laser;
    }

    public static GameEntity generateGreenBeam(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new CollisionComponent());
        laser.add(new LifetimeComponent());
        laser.add(new AnimationComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("GreenBeam");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 450f;

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("GreenBeam");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenBeam"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenBeam2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        col.collisionReaction = 0;
        col.unmovable = true;
        col.rotateBox = true;

        DamageComponent dam = dm.get(laser);
        dam.damage = 30;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 3f;

        return laser;
    }

    public static GameEntity generateTurretlaser(float x, float y, float rotation) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity();

        laser.add(new PositionComponent());
        laser.add(new ImageComponent());
        laser.add(new EventComponent());

        ImageComponent img = im.get(laser);
        img.texRegion = ImageComponent.atlas.findRegion("WhiteBeam");
        img.rotate = true;

        PositionComponent pos = pm.get(laser);
        pos.x = x; pos.y = y;
        pos.width = 20;
        pos.height = 10;
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        EventComponent ev = em.get(laser);
        ev.targetTime = 0.5f;
        ev.ticking = true;
        ev.repeat = false;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                e.add(new MovementComponent());
                e.add(new AnimationComponent());
                e.add(new LifetimeComponent());
                e.add(new DamageComponent());

                ImageComponent img = im.get(e);

                img.texRegion = ImageComponent.atlas.findRegion("Laser");

                pm.get(e).width = img.texRegion.getRegionWidth();
                pm.get(e).height = img.texRegion.getRegionHeight();
                PositionSystem.setOrigins(pm.get(e));

                AnimationComponent anim = am.get(e);
                anim.animate = true;
                anim.repeat = true;
                anim.baseTextureRegion = ImageComponent.atlas.findRegion("Laser");
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("Laser"));
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("LightBlueLaser"));
                anim.currentAnimation = 0;
                anim.currentIndex = 0;
                anim.animationTime = 0.2f;

                DamageComponent dam = dm.get(e);
                dam.damage = 20;
                dam.tag = 'p';

                MovementComponent mov = mm.get(e);
                mov.speedPerSecond = 100f;

                em.get(e).targetTime = 0.1f;
                em.get(e).repeat = true;
                em.get(e).event = new GameEvent() {
                    @Override
                    public void event(GameEntity e, Engine engine) {
                        mm.get(e).speedPerSecond += mm.get(e).speedPerSecond / 2;
                    }
                };

                lfm.get(e).endTime = 3f;
            }
        };

        return laser;
    }

    /*
    Enemy ------------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generateEnemy(float x, float y) {
        GameEntity enemy = generateGenericEnemy(x, y);

        mm.get(enemy).speedPerSecond = 120f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("UFO1");

        AnimationComponent anim = am.get(enemy);
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("UFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO3"));

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 200f;
        AI.AIType = 'r';
        AI.targetTime = 2f;
        AI.shoots = true;
        AI.projectileType = 'l';

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 50;
        health.health = 50;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.3f;

        pom.get(enemy).points = 100;

        return enemy;
    }

    public static GameEntity generateFasterEnemy(float x, float y) {
        GameEntity enemy = generateGenericEnemy(x, y);

        mm.get(enemy).speedPerSecond = 240f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("UFO1");

        AnimationComponent anim = am.get(enemy);
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("UFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO3"));

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 200f;
        AI.AIType = 'r';
        AI.targetTime = 2f;
        AI.shoots = true;
        AI.projectileType = 'l';

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 50;
        health.health = 50;

        shm.get(enemy).attackDelay = 0.3f;

        pom.get(enemy).points = 777;

        return enemy;
    }

    public static GameEntity generateBlueEnemy(float x, float y) {
        GameEntity enemy = generateGenericEnemy(x, y);

        mm.get(enemy).speedPerSecond = 160f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("BlueUFO1");

        AnimationComponent anim = am.get(enemy);
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("BlueUFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BlueUFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BlueUFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BlueUFO3"));
        anim.animationTime = 1f;

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 300;
        AI.AIType = 'l';
        AI.targetTime = 4f;
        AI.shoots = true;
        AI.projectileType = 's';

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 45;
        health.health = 45;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 2.5f;
        shoot.isAttacking = true;

        pom.get(enemy).points = 250;

        return enemy;
    }

    public static GameEntity generateRedEnemy(float x, float y) {
        GameEntity enemy = generateGenericEnemy(x, y);

        mm.get(enemy).speedPerSecond = 130f;

        ImageComponent img = im.get(enemy);
        im.get(enemy).texRegion = ImageComponent.atlas.findRegion("RedUFO1");

        AnimationComponent anim = am.get(enemy);
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("RedUFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedUFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedUFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedUFO3"));

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 370f;
        AI.AIType = 'e';
        AI.targetTime = 2f;
        AI.shoots = true;
        AI.projectileType = 'e';

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 65;
        health.health = 65;

        shm.get(enemy).attackDelay = 0.8f;

        pom.get(enemy).points = 200;

        return enemy;
    }

    public static GameEntity generateGreenEnemy(float x, float y) {
        GameEntity enemy = generateGenericEnemy(x, y);

        enemy.add(new EventComponent());

        mm.get(enemy).speedPerSecond = 150f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("GreenUFO1");

        AnimationComponent anim = am.get(enemy);
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("GreenUFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenUFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenUFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenUFO3"));

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 230f;
        AI.stopDistance = 0f;
        AI.AIType = 'a';
        AI.targetTime = 1f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'l';

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 70;
        health.health = 70;

        shm.get(enemy).attackDelay = 0.6f;

        EventComponent event = em.get(enemy);
        event.repeat = true;
        event.targetTime = 4f;
        event.ticking = true;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                PositionComponent pos = pm.get(e);
                hm.get(e).health += 70;
                im.get(e).texRegion = ImageComponent.atlas.findRegion("GhostUFO1");
                for (int i = 0; i < 4; i++) {
                    engine.addEntity(generateGreenArrow(pos.x + pos.originX, pos.y + pos.originY, i * 90, 1));
                }
            }
        };

        pom.get(enemy).points = 300;

        return enemy;
    }

    public static GameEntity generateYellowEnemy(float x, float y) {
        GameEntity enemy = generateGenericEnemy(x, y);

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 250f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("YellowUFO1");

        AnimationComponent anim = am.get(enemy);
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("YellowUFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("YellowUFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("YellowUFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("YellowUFO3"));

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 350f;
        AI.AIType = 'l';
        AI.targetTime = 1f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 't';

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 85;
        health.health = 85;

        shm.get(enemy).attackDelay = 0.8f;

        pom.get(enemy).points = 400;

        return enemy;
    }

    public static GameEntity generateGhostUFO(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 30f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("GhostUFO1");

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 400f;
        AI.AIType = 'r';
        AI.targetTime = 15f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'g';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 180;
        health.health = 180;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 4;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.2f;

        bm.get(enemy).bounceDuration = 2f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 700;

        return enemy;
    }

    public static GameEntity generateMiniGhostUFO(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());
        enemy.add(new DamageComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 290f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("GhostUFO1");

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight() / 2;
        position.width = img.texRegion.getRegionWidth() / 2;
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 180f;
        AI.AIType = 'r';
        AI.targetTime = 0.7f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'g';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 40;
        health.health = 40;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 0;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.35f;

        DamageComponent dam = dm.get(enemy);
        dam.damage = 5;
        dam.tag = 'p';

        bm.get(enemy).bounceDuration = 5f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 250;

        return enemy;
    }

    public static GameEntity generateFinalEnemy(float x, float y) {
        GameEntity enemy = generateGenericEnemy(x, y);

        enemy.add(new EventComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 100f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("FinalUFO");

        AnimationComponent anim = am.get(enemy);
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("FinalUFO");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("FinalUFO"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("FinalUFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("FinalUFO3"));

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.collisionReaction = 7;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 250f;
        AI.AIType = 'w';
        AI.targetTime = 4f;
        AI.shoots = false;

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 170;
        health.health = 170;
        health.deathAction = 5;

        EventComponent event = em.get(enemy);
        event.repeat = true;
        event.targetTime = 5f;
        event.ticking = true;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                PositionComponent pos = pm.get(e);

                mm.get(e).speedPerSecond = MathUtils.clamp((float) Math.random() * 200f + 150f, 150f, 360f);
                aim.get(e).targetTime =  MathUtils.clamp((float) Math.random() * 3.6f + 0.3f, 0.3f, 4f);

                im.get(e).texRegion = ImageComponent.atlas.findRegion("GhostUFO1");

                for (int i = 0; i < 6; i++) {
                    engine.addEntity(generateGhostArrow(pos.x + pos.originX, pos.y + pos.originY, i * 60, 1));
                }

                if (bm.get(e).isBouncing)
                    bm.get(e).isBouncing = false;
            }
        };

        pom.get(enemy).points = 830;

        return enemy;
    }

    public static GameEntity generateWisp(float x, float y) {
        GameEntity enemy = generateGenericWisp(x, y);

        enemy.add(new AnimationComponent());

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.animationTime = 0.3f;
        anim.baseTextureRegion = ImageComponent.atlas.findRegion(("Wisp"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Wisp"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Wisp2"));

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 80f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("Wisp");

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 200f;
        AI.AIType = 'r';
        AI.targetTime = 4f;

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 50;
        health.health = 50;

        DamageComponent dam = dm.get(enemy);
        dam.damage = 20;

        pom.get(enemy).points = 100;

        return enemy;
    }

    public static GameEntity generatePoisonWisp(float x, float y) {
        GameEntity enemy = generateGenericWisp(x, y);

        enemy.add(new AnimationComponent());
        enemy.add(new CollisionComponent());

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.animationTime = 0.3f;
        anim.baseTextureRegion = ImageComponent.atlas.findRegion(("PoisonWisp"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PoisonWisp"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PoisonWisp2"));

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 100f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("PoisonWisp");

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.collisionReaction = 8;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 400f;
        AI.AIType = 'r';
        AI.targetTime = 5f;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 90;
        health.health = 90;

        DamageComponent dam = dm.get(enemy);
        dam.damage = 8;

        pom.get(enemy).points = 200;

        return enemy;
    }

    public static GameEntity generateBigWisp(float x, float y) {
        GameEntity enemy = generateGenericWisp(x, y);

        enemy.add(new EventComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 50f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("BigWisp1");

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 450f;
        AI.AIType = 'r';
        AI.targetTime = 10f;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 190;
        health.health = 190;

        DamageComponent dam = dm.get(enemy);
        dam.damage = 25;

        EventComponent eve = em.get(enemy);
        eve.targetTime = 4f;
        eve.repeat = true;
        eve.ticking = true;
        eve.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (mm.get(e).speedPerSecond == 70f) {
                    PositionComponent pos = pm.get(e);
                    mm.get(e).speedPerSecond = 180f;
                    im.get(e).texRegion = ImageComponent.atlas.findRegion("BigWisp0");
                    aim.get(e).AIType = 'f';

                    e.add(new AnimationComponent());
                    AnimationComponent anim = am.get(e);
                    anim.animate = true;
                    anim.repeat = true;
                    anim.baseTextureRegion = ImageComponent.atlas.findRegion("BigWisp0");
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("BigWisp0"));
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("BigWisp3"));
                    anim.currentAnimation = 0;
                    anim.currentIndex = 0;
                    anim.animationTime = 0.6f;

                    for (int i = 0; i < 12; i ++) {
                        engine.addEntity(generateWispProjectile(pos.x + pos.originX, pos.y + pos.originY, i * 30, ImageComponent.atlas.findRegion("WispProjectile"), 1));
                    }

                } else {
                    mm.get(e).speedPerSecond = 70f;
                    im.get(e).texRegion = ImageComponent.atlas.findRegion("BigWisp1");
                    aim.get(e).AIType = 'r';
                    e.remove(AnimationComponent.class);
                }
            }
        };

        pom.get(enemy).points = 550;

        return enemy;
    }

    public static GameEntity generateSwirlWisp(float x, float y) {
        GameEntity enemy = generateGenericWisp(x, y);

        enemy.add(new AnimationComponent());
        enemy.add(new EventComponent());
        enemy.add(new ItemComponent());
        enemy.add(new ItemReceivableComponent());

        ItemComponent item = itm.get(enemy);
        item.toggleGetItem = true;
        item.disposeAfterUse = false;
        item.useableByEnemy = false;
        item.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (mm.get(e).speedPerSecond >= 150f)
                    mm.get(e).speedPerSecond -= 2.5f;
            }
        };

        ItemReceivableComponent itemR = irm.get(enemy);
        itemR.endTime = 0.45f;

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 100f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("SwirlWisp1");

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("SwirlWisp1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("SwirlWisp1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("SwirlWisp2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.3f;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 550f;
        AI.AIType = 'r';
        AI.targetTime = 10f;

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 250;
        health.health = 250;
        health.deathAction = 5;

        EventComponent ev = em.get(enemy);
        ev.ticking = true;
        ev.repeat = true;
        ev.targetTime = 4f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                PositionComponent pos = pm.get(e);
                pos.x += (float) (Math.random() * 800) - 400;
                pos.y += (float) (Math.random() * 800) - 400;
            }
        };

        DamageComponent dam = dm.get(enemy);
        dam.damage = 45;

        pom.get(enemy).points = 1200;

        return enemy;
    }

    public static GameEntity generateEnemyShipRed(float x, float y) {
        GameEntity enemy = generateGenericShip(x, y);

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 280f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("EnemyShip");

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 450f;
        AI.stopDistance = 300f;
        AI.AIType = 'r';
        AI.targetTime = 5f;
        AI.projectileType = 'f';

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 90;
        health.health = 90;

        shm.get(enemy).attackDelay = 0.9f;

        bm.get(enemy).bounceDuration = 0.6f;

        pom.get(enemy).points = 600;

        return enemy;
    }

    public static GameEntity generateEnemyShipBlue(float x, float y) {
        GameEntity enemy = generateGenericShip(x, y);

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 200f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("EnemyShipIce");

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 450f;
        AI.stopDistance = 200f;
        AI.AIType = 'r';
        AI.targetTime = 5f;
        AI.shoots = true;
        AI.projectileType = 'c';

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 80;
        health.health = 80;

        shm.get(enemy).attackDelay = 3f;

        bm.get(enemy).bounceDuration = 4f;

        pom.get(enemy).points = 600;

        return enemy;
    }

    public static GameEntity generateEnemyShipYellow(float x, float y) {
        GameEntity enemy = generateGenericShip(x, y);

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 300f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("EnemyShipThunder");

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 700f;
        AI.stopDistance = 500f;
        AI.AIType = 'f';
        AI.targetTime = 5f;
        AI.projectileType = 't';

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 80;
        health.health = 80;

        shm.get(enemy).attackDelay = 0.43f;

        bm.get(enemy).bounceDuration = 4f;

        pom.get(enemy).points = 770;

        return enemy;
    }

    public static GameEntity generateEnemyShipGold(float x, float y) {
        GameEntity enemy = generateGenericShip(x, y);

        enemy.add(new EventComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 290f;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("EnemyShipGold");

        PositionComponent position = pm.get(enemy);
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 450f;
        AI.stopDistance = 240f;
        AI.AIType = 'f';
        AI.targetTime = 2f;
        AI.projectileType = 'f';

        HealthComponent health = hm.get(enemy);
        health.maxHealth = 360;
        health.health = 360;
        health.deathAction = 2;

        shm.get(enemy).attackDelay = 0.8f;

        bm.get(enemy).bounceDuration = 2f;

        EventComponent ev = em.get(enemy);
        ev.repeat = true;
        ev.ticking = true;
        ev.targetTime = 7f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                PositionComponent pos = pm.get(e);
                AIComponent ai = aim.get(e);

                if (ai.projectileType == 'f') {
                    for (int i = 0; i < 4; i++) {
                        engine.addEntity(generateFireLaser(pos.x + pos.originX, pos.y + pos.originY, pos.rotation - 80 + i * 40));
                    }
                    ai.projectileType = 't';
                    ai.awarenessRadius = 300;
                    shm.get(e).attackDelay = 0.4f;
                    mm.get(e).speedPerSecond = 225f;
                    ai.AIType = 'r';
                } else if (ai.projectileType == 't') {
                    for (int i = 0; i < 6; i++) {
                        engine.addEntity(generateOrangeArrow(pos.x + pos.originX, pos.y + pos.originY, i * 60, 1));
                    }
                    ai.projectileType = 'u';
                    ai.AIType = 'e';
                    ai.awarenessRadius = 300;
                    shm.get(e).attackDelay = 0.5f;
                } else if (ai.projectileType == 'u') {
                    for (int i = 0; i < 6; i++) {
                        engine.addEntity(generateOrangeArrow(pos.x + pos.originX, pos.y + pos.originY, i * 60, 1));
                    }
                    ai.projectileType = 'f';
                    ai.AIType = 'x';
                    ai.awarenessRadius = 400;
                    shm.get(e).attackDelay = 0.42f;
                }
            }
        };

        pom.get(enemy).points = 2000;

        return enemy;
    }

    public static GameEntity generateTurret(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("Turret");
        img.rotate = true;


        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x +10, position.y + 10,
                position.x + position.width -10, position.y + 10,
                position.x + position.width - 10, position.y + position.height - 10,
                position.x + 10, position.y + position.height - 10
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.collisionReaction = 1;
        collision.unmovable = true;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 400f;
        AI.AIType = 'f';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = '4';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 500;
        health.health = 500;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.4f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 5000;

        return enemy;
    }

    public static GameEntity generateYellowTurret(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("YellowTurret");
        img.rotate = true;


        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x +10, position.y + 10,
                position.x + position.width -10, position.y + 10,
                position.x + position.width - 10, position.y + position.height - 10,
                position.x + 10, position.y + position.height - 10
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.collisionReaction = 1;
        collision.unmovable = true;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 300f;
        AI.AIType = 'f';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = '5';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 700;
        health.health = 700;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.7f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 10000;

        return enemy;
    }

    public static GameEntity generateBlueTurret(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new AnimationComponent());

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("BlueTurret");
        img.rotate = true;


        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("BlueTurret");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BlueTurret"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BlueTurret2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.3f;

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x +10, position.y + 10,
                position.x + position.width -10, position.y + 10,
                position.x + position.width - 10, position.y + position.height - 10,
                position.x + 10, position.y + position.height - 10
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.collisionReaction = 1;
        collision.unmovable = true;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 1400f;
        AI.AIType = 'f';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = '3';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 1800;
        health.health = 1800;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 2;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 1.3f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 15000;

        return enemy;
    }


    public static GameEntity generateCore(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AnimationComponent());
        enemy.add(new AIComponent());
        enemy.add(new HealthComponent());
        enemy.add(new EventComponent());
        enemy.add(new ShootComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 50f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("Core");

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Core");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Core"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Core2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.collisionReaction = 1;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 250f;
        AI.AIType = 'l';
        AI.targetTime = 8f;
        AI.targetRotation = position.rotation;
        AI.shoots = false;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 1f;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 500;
        health.health = 500;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 2;

        EventComponent event = em.get(enemy);
        event.repeat = true;
        event.targetTime = 5f;
        event.ticking = true;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                PositionComponent pos = pm.get(e);
                if ((int) (Math.random() * 2) == 1) {
                    for (int i = 0; i < 8; i++) {
                        engine.addEntity(generateCoreLaser(pos.x + pos.originX, pos.y + pos.originY, i * 45));
                    }
                }
            }
        };

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 5500;

        return enemy;
    }

    public static GameEntity generatePodShip(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 100f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("PodShip");
        img.rotate = true;


        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.collisionReaction = 1;
        collision.rotateBox = true;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 300f;
        AI.AIType = 'r';
        AI.targetTime = 5f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'e';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 150;
        health.health = 150;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.9f;

        bm.get(enemy).bounceDuration = 2f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 1000;

        return enemy;
    }

    //Bonus Enemies

    public static GameEntity generateGoldWisp(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new HealthComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 390f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("GoldWisp");

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 200f;
        AI.AIType = 'w';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = false;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 70;
        health.health = 70;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 3;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 2777;

        return enemy;
    }

    public static GameEntity generatePlatinumWisp(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new HealthComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 520f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("PlatinumWisp");

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 200f;
        AI.AIType = 'w';
        AI.targetTime = 1.5f;
        AI.targetRotation = position.rotation;
        AI.shoots = false;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 230;
        health.health = 230;
        health.invinciblityDuration = 0.1f;
        health.tag = 'e';
        health.deathAction = 5;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 25777;

        return enemy;
    }

    //bosses

    public static GameEntity generateBossWisp(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new HealthComponent());
        enemy.add(new DamageComponent());
        enemy.add(new EventComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new AnimationComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 50f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("BossWisp");

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("BossWisp");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BossWisp"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BossWisp2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.7f;

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.collisionReaction = 8;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 650f;
        AI.AIType = 'r';
        AI.targetTime = 10f;
        AI.targetRotation = position.rotation;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 2000;
        health.health = 2000;
        health.invinciblityDuration = 0.2f;
        health.tag = 'e';
        health.deathAction = 2;

        DamageComponent dam = dm.get(enemy);
        dam.damage = 45;
        dam.tag = 'p';

        EventComponent eve = em.get(enemy);
        eve.targetTime = 6f;
        eve.repeat = true;
        eve.ticking = true;
        eve.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (mm.get(e).speedPerSecond >= 29f && mm.get(e).speedPerSecond <= 81f) {
                    PositionComponent pos = pm.get(e);
                    mm.get(e).speedPerSecond = 185f;

                    for (int i = 0; i < 12; i ++) {
                        engine.addEntity(generatePoisonWispProjectile(pos.x + pos.originX, pos.y + pos.originY, i * 30));
                    }
                    engine.addEntity(generatePoisonWisp(pos.x + pos.originX + 10, pos.y + pos.originY));
                    engine.addEntity(generatePoisonWisp(pos.x + pos.originX + 10, pos.y + pos.originY + 20));
                    engine.addEntity(generatePoisonWisp(pos.x + pos.originX - 10, pos.y + pos.originY + 20));

                } else {
                    mm.get(e).speedPerSecond = ((float) Math.random() * 50) + 30f;
                    em.get(e).targetTime = ((float) Math.random() * 7) + 1.5f;
                }
            }
        };

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 70000;

        return enemy;
    }

    public static GameEntity generateBossUFO(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AnimationComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());
        enemy.add(new EventComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 70f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("BossUFO1");

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("BossUFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BossUFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BossUFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BossUFO3"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 1f;

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.collisionReaction = 1;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 350f;
        AI.AIType = 'a';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'l';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 2200;
        health.health = 2200;
        health.invinciblityDuration = 0.15f;
        health.tag = 'e';
        health.deathAction = 2;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.22f;

        bm.get(enemy).bounceDuration = 0.01f;

        EventComponent ev = em.get(enemy);
        ev.repeat = true;
        ev.ticking = true;
        ev.targetTime = 8f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                PositionComponent pos = pm.get(e);
                AIComponent ai = aim.get(e);

                im.get(e).texRegion = ImageComponent.atlas.findRegion("BossUFOActive");

                if (ai.projectileType == 'l') {
                    for (int i = 0; i < 9; i++) {
                        engine.addEntity(generateShockWave(pos.x + pos.originX, pos.y + pos.originY, i * 40, 1));
                    }
                    ai.projectileType = 'h';
                    ai.AIType ='f';
                    ai.awarenessRadius = 650f;
                    shm.get(e).attackDelay = 1.2f;
                    mm.get(e).speedPerSecond = 40f;
                } else if (ai.projectileType == 'h') {
                    for (int i = 0; i < 9; i++) {
                        engine.addEntity(generateGhostLaser(pos.x + pos.originX, pos.y + pos.originY, i * 40, 1));
                    }
                    ai.projectileType = 'm';
                    shm.get(e).attackDelay = 1f;
                    mm.get(e).speedPerSecond = 30f;
                } else if (ai.projectileType == 'm') {
                    for (int i = 0; i < 6; i++) {
                        engine.addEntity(generateEnemyExplosionLaser(pos.x + pos.originX, pos.y + pos.originY, i * 60));
                    }
                    ai.projectileType = 'e';
                    ai.AIType = 'a';
                    ai.awarenessRadius = 400f;
                    shm.get(e).attackDelay = 0.8f;
                    mm.get(e).speedPerSecond = 150f;
                } else if (ai.projectileType == 'e') {
                    ai.projectileType = 'b';
                    ai.AIType = 'w';
                    ai.targetTime = 3f;
                    shm.get(e).attackDelay = 0.5f;
                    mm.get(e).speedPerSecond = 330f;
                }else if (ai.projectileType == 'b') {
                    ai.projectileType = 'c';
                    ai.AIType = 'a';
                    ai.awarenessRadius = 400f;
                    shm.get(e).attackDelay = 0.8f;
                    mm.get(e).speedPerSecond = 100f;
                } else if (ai.projectileType == 'c') {
                    for (int i = 0; i < 9; i++) {
                        engine.addEntity(generateShockWave(pos.x + pos.originX, pos.y + pos.originY, i * 40 + 20, 1));
                    }
                    for (int i = 0; i < 12; i++) {
                        engine.addEntity(generateFireLaser(pos.x + pos.originX, pos.y + pos.originY, i * 30 + 15));
                    }
                    for (int i = 0; i < 6; i++) {
                        engine.addEntity(generateIceLaser(pos.x + pos.originX, pos.y + pos.originY, i * 60, 1));
                    }
                    ai.projectileType = 'l';
                    ai.AIType = 'r';
                    ai.awarenessRadius = 650f;
                    shm.get(e).attackDelay = 0.22f;
                    mm.get(e).speedPerSecond = 220f;
                }
            }
        };

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 25000;

        return enemy;
    }

    public static GameEntity generateBossTurret(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new EventComponent());
        enemy.add(new AnimationComponent());

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("BossTurret");
        img.rotate = true;

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("BossTurret");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BossTurret"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BossTurret2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.3f;

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x +10, position.y + 10,
                position.x + position.width -10, position.y + 10,
                position.x + position.width - 10, position.y + position.height - 10,
                position.x + 10, position.y + position.height - 10
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.unmovable = true;
        collision.collisionReaction = 1;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 1800f;
        AI.AIType = 'f';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = '1';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 5000;
        health.health = 5000;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 2;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 1.4f;

        EventComponent ev =  em.get(enemy);
        ev.targetTime = 8f;
        ev.ticking = true;
        ev.repeat = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (aim.get(e).projectileType == '1') {
                    shm.get(e).attackDelay = 1f;
                    aim.get(e).projectileType = '2';
                } else if (aim.get(e).projectileType == '2') {
                    shm.get(e).attackDelay = 1.6f;
                    aim.get(e).projectileType = '1';
                }
            }
        };

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 150000;

        return enemy;
    }

    public static GameEntity generateBossShip(float x, float y) {
        GameEntity enemy = new GameEntity();

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());
        enemy.add(new EventComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 320f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("BossEnemyShip");
        img.rotate = true;

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        CollisionComponent collision = cm.get(enemy);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, position.rotation);
        collision.boundingBox.setOrigin(position.x + position.originX, position.y + position.originY);
        collision.collisionReaction = 1;
        collision.rotateBox = true;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 400f;
        AI.AIType = 'x';
        AI.targetTime = 0.6f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'u';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 1350;
        health.health = 1350;
        health.invinciblityDuration = 0.15f;
        health.tag = 'e';
        health.deathAction = 2;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.6f;

        bm.get(enemy).bounceDuration = 2f;

        EventComponent ev = em.get(enemy);
        ev.repeat = true;
        ev.ticking = true;
        ev.targetTime = 7f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                PositionComponent pos = pm.get(e);
                AIComponent ai = aim.get(e);

                if (ai.projectileType == 'u') {
                    for (int i = 0; i < 6; i++) {
                        engine.addEntity(generateGreenArrow(pos.x + pos.originX, pos.y + pos.originY, i * 60, 1));
                    }
                    ai.projectileType = 'f';
                    ai.awarenessRadius = 300;
                    shm.get(e).attackDelay = 0.29f;
                    mm.get(e).speedPerSecond = 250f;
                    ai.AIType = 'x';
                } else if (ai.projectileType == 'f') {
                    for (int i = 0; i < 12; i++) {
                        engine.addEntity(generateOrangeArrow(pos.x + pos.originX, pos.y + pos.originY,i * 30, 1));
                    }
                    ai.projectileType = 'W';
                    ai.AIType = 'x';
                    ai.awarenessRadius = 400;
                    shm.get(e).attackDelay = 2.7f;
                } else if (ai.projectileType == 'W') {
                    for (int i = 0; i < 4; i++) {
                        engine.addEntity(generateWhiteArrow(pos.x + pos.originX, pos.y + pos.originY, i * 90, 1));
                    }
                    ai.projectileType = 'B';
                    ai.AIType = 'x';
                    ai.awarenessRadius = 400;
                    shm.get(e).attackDelay = 0.55f;
                    mm.get(e).speedPerSecond = 250f;
                    shm.get(e).isAttacking = true;
                } else if (ai.projectileType == 'B') {
                    for (int i = 0; i < 8; i++) {
                        engine.addEntity(generateBlueArrow(pos.x + pos.originX, pos.y + pos.originY, i * 45, 1));
                    }
                    ai.projectileType = 'u';
                    ai.AIType = 'x';
                    ai.awarenessRadius = 400;
                    shm.get(e).attackDelay = 0.6f;
                    mm.get(e).speedPerSecond = 250f;
                }
            }
        };

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 50000;

        return enemy;
    }


    /*
   Items -----------------------------------------------------------------------
   -----------------------------------------------------------------------------
    */
    public static GameEntity generateHealingItem(float x, float y) {
        GameEntity healer = new GameEntity();

        healer.add(new PositionComponent());
        healer.add(new ItemComponent());
        healer.add(new ImageComponent());
        healer.add(new AnimationComponent());
        healer.add(new LifetimeComponent());

        ImageComponent img = im.get(healer);
        img.texRegion = ImageComponent.atlas.findRegion("Life1");

        AnimationComponent anim = am.get(healer);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Life1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Life1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Life2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent pos = pm.get(healer);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = true;

        LifetimeComponent life = lfm.get(healer);
        life.endTime = 9f;

        ItemComponent item = itm.get(healer);
        item.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                hm.get(e).health += 70;
            }
        };

        return healer;
    }

    public static GameEntity generateMaxHealingItem(float x, float y) {
        GameEntity healer = new GameEntity();

        healer.add(new PositionComponent());
        healer.add(new ItemComponent());
        healer.add(new ImageComponent());
        healer.add(new AnimationComponent());
        healer.add(new LifetimeComponent());

        ImageComponent img = im.get(healer);
        img.texRegion = ImageComponent.atlas.findRegion("MaxLife");

        AnimationComponent anim = am.get(healer);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("MaxLife");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("MaxLife"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("MaxLife2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent pos = pm.get(healer);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = true;

        LifetimeComponent life = lfm.get(healer);
        life.endTime = 9f;

        ItemComponent item = itm.get(healer);
        item.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                hm.get(e).maxHealth += 5;
                hm.get(e).health = hm.get(e).maxHealth;
            }
        };

        return healer;
    }

    public static GameEntity generateSuperShootUp(float x, float y) {
        GameEntity shot = new GameEntity();

        shot.add(new PositionComponent());
        shot.add(new ItemComponent());
        shot.add(new ImageComponent());
        shot.add(new AnimationComponent());
        shot.add(new LifetimeComponent());

        ImageComponent img = im.get(shot);
        img.texRegion = ImageComponent.atlas.findRegion("SuperShootUp");

        AnimationComponent anim = am.get(shot);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("SuperShootUp");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("SuperShootUp"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("SuperShootUp2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent pos = pm.get(shot);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = true;

        LifetimeComponent life = lfm.get(shot);
        life.endTime = 6f;

        ItemComponent item = itm.get(shot);
        item.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (shm.has(e)) {
                    if (shm.get(e).attackDelay > 0.15f)
                        shm.get(e).attackDelay -= 0.11f;
                    else
                        shm.get(e).attackDelay -= shm.get(e).attackDelay / 5;
                }
            }
        };

        return shot;
    }

    public static GameEntity generateShootUp(float x, float y) {
        GameEntity shot = new GameEntity();

        shot.add(new PositionComponent());
        shot.add(new ItemComponent());
        shot.add(new ImageComponent());
        shot.add(new AnimationComponent());
        shot.add(new LifetimeComponent());

        ImageComponent img = im.get(shot);
        img.texRegion = ImageComponent.atlas.findRegion("ShootUp");

        AnimationComponent anim = am.get(shot);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("ShootUp");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("ShootUp"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("ShootUp2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent pos = pm.get(shot);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = true;

        LifetimeComponent life = lfm.get(shot);
        life.endTime = 6f;

        ItemComponent item = itm.get(shot);
        item.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (shm.has(e)) {
                    if (shm.get(e).attackDelay >= 0.09f)
                        shm.get(e).attackDelay -= 0.025f;
                }
            }
        };

        return shot;
    }

    public static GameEntity generateSpeedUp(float x, float y) {
        GameEntity speed = new GameEntity();

        speed.add(new PositionComponent());
        speed.add(new ItemComponent());
        speed.add(new ImageComponent());
        speed.add(new AnimationComponent());
        speed.add(new LifetimeComponent());

        ImageComponent img = im.get(speed);
        img.texRegion = ImageComponent.atlas.findRegion("SpeedUp");

        AnimationComponent anim = am.get(speed);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("SpeedUp");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("SpeedUp"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("SpeedUp2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent pos = pm.get(speed);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = true;

        LifetimeComponent life = lfm.get(speed);
        life.endTime = 6f;

        ItemComponent item = itm.get(speed);
        item.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (mm.has(e)) {
                    if (mm.get(e).speedPerSecond <= 200f)
                        mm.get(e).speedPerSecond += 60f;
                    else if (mm.get(e).speedPerSecond <= 650f)
                        mm.get(e).speedPerSecond += 15f;
                }
            }
        };

        return speed;
    }

    public static GameEntity generateColorPowerUp(float x, float y, String color) {
        GameEntity power = new GameEntity();

        power.add(new PositionComponent());
        power.add(new ItemComponent());
        power.add(new ImageComponent());
        power.add(new AnimationComponent());
        power.add(new LifetimeComponent());


        ImageComponent img = im.get(power);
        ItemComponent item = itm.get(power);
        AnimationComponent anim = am.get(power);
        anim.animate = true;
        anim.repeat = true;
        anim.animationTime = 0.2f;

        if (color.equals("Red")) {
            img.texRegion = ImageComponent.atlas.findRegion("RedPower1");

            anim.baseTextureRegion = ImageComponent.atlas.findRegion("RedPower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedPower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedPower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    if (colm.has(e))
                        colm.get(e).color = 'r';
                }
            };
        } else if (color.equals("Blue")) {
            img.texRegion = ImageComponent.atlas.findRegion("BluePower1");

            anim.baseTextureRegion = ImageComponent.atlas.findRegion("BluePower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("BluePower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("BluePower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    if (colm.has(e))
                        colm.get(e).color = 'b';
                }
            };

        } else if (color.equals("Green")) {
            img.texRegion = ImageComponent.atlas.findRegion("GreenPower1");

            anim.baseTextureRegion = ImageComponent.atlas.findRegion("GreenPower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenPower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenPower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    if (colm.has(e))
                        colm.get(e).color = 'g';
                }
            };
        } else if (color.equals("Yellow")) {
            img.texRegion = ImageComponent.atlas.findRegion("YellowPower1");

            anim.baseTextureRegion = ImageComponent.atlas.findRegion("YellowPower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("YellowPower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("YellowPower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    if (colm.has(e))
                        colm.get(e).color = 'y';
                }
            };
        } else if (color.equals("Pink")) {
            img.texRegion = ImageComponent.atlas.findRegion("PinkPower1");

            anim.baseTextureRegion = ImageComponent.atlas.findRegion("PinkPower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("PinkPower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("PinkPower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    if (colm.has(e))
                        colm.get(e).color = 'p';
                }
            };
        } else if (color.equals("Purple")) {
            img.texRegion = ImageComponent.atlas.findRegion("PurplePower1");

            anim.baseTextureRegion = ImageComponent.atlas.findRegion("PurplePower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("PurplePower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("PurplePower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    if (colm.has(e))
                        colm.get(e).color = 'v';
                }
            };
        } else if (color.equals("Orange")) {
            img.texRegion = ImageComponent.atlas.findRegion("OrangePower1");

            anim.baseTextureRegion = ImageComponent.atlas.findRegion("OrangePower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("OrangePower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("OrangePower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    if (colm.has(e))
                        colm.get(e).color = 'o';
                }
            };
        } else if (color.equals("White")) {
            img.texRegion = ImageComponent.atlas.findRegion("WhitePower1");

            anim.baseTextureRegion = ImageComponent.atlas.findRegion("WhitePower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("WhitePower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("WhitePower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    if (colm.has(e))
                        colm.get(e).color = 'w';
                }
            };
        }

        PositionComponent pos = pm.get(power);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);

        LifetimeComponent life = lfm.get(power);
        life.endTime = 10f;

        return power;
    }

    public static GameEntity generateDoubleUp(float x, float y) {
        GameEntity healer = new GameEntity();

        healer.add(new PositionComponent());
        healer.add(new ItemComponent());
        healer.add(new ImageComponent());
        healer.add(new AnimationComponent());
        healer.add(new LifetimeComponent());

        ImageComponent img = im.get(healer);
        img.texRegion = ImageComponent.atlas.findRegion("DoubleUp");

        AnimationComponent anim = am.get(healer);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("DoubleUp");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("DoubleUp"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("DoubleUp2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        PositionComponent pos = pm.get(healer);
        pos.x = x;
        pos.y = y;
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = true;

        LifetimeComponent life = lfm.get(healer);
        life.endTime = 9f;

        ItemComponent item = itm.get(healer);
        item.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
            if (pim.has(e))
                engine.addEntity(generateGhostPlayerShip(pm.get(e).x, pm.get(e).y));
            }
        };

        return healer;
    }

    public static GameEntity generateGhostPlayerShip(float x, float y) {
        GameEntity player = new GameEntity();

        player.add(new PlayerInputComponent());
        player.add(new PositionComponent());
        player.add(new MovementComponent());
        player.add(new ImageComponent());
        player.add(new HealthComponent());
        player.add(new ShootComponent());
        player.add(new CollisionComponent());

        PlayerInputComponent controller = pim.get(player);
        controller.forward = Input.Keys.W;
        controller.back = Input.Keys.S;
        controller.right = Input.Keys.D;
        controller.left = Input.Keys.A;
        controller.shoot = Input.Keys.SPACE;
        controller.sprint = Input.Keys.SHIFT_LEFT;

        ImageComponent image = im.get(player);
        image.texRegion = ImageComponent.atlas.findRegion("GhostPlayerShip");
        image.rotate = true;

        PositionComponent position = pm.get(player);
        position.x = x;
        position.y = y;
        position.height = image.texRegion.getRegionHeight();
        position.width = image.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);

        CollisionComponent collision = cm.get(player);
        collision.boundingBox = new Polygon(new float[] {
                position.x, position.y,
                position.x + position.width, position.y,
                position.x + position.width, position.y + position.height,
                position.x, position.y + position.height
        });
        collision.boundingBox.setOrigin(pm.get(player).x + pm.get(player).originX, pm.get(player).y + pm.get(player).originY);
        collision.collisionReaction = 10;
        collision.rotateBox = true;

        MovementComponent move = mm.get(player);
        move.speedPerSecond = 160f;
        move.move = false;
        position.rotation = 0f;

        HealthComponent heal = hm.get(player);
        heal.maxHealth = 150;
        heal.health = 150;
        heal.invinciblityDuration = 0.35f;
        heal.isAlive = true;
        heal.tag = 'p';
        heal.deathAction = 3;

        ShootComponent sho = shm.get(player);
        sho.attackDelay = 0.2f;

        return player;
    }

    /*
    Obstacles -------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generateFloatingShock(float x, float y, float width, float height, float speed, int hurtsPlayer) {
        GameEntity shock = new GameEntity();

        shock.add(new PositionComponent());
        shock.add(new ImageComponent());
        shock.add(new AnimationComponent());
        shock.add(new MovementComponent());
        shock.add(new DamageComponent());
        shock.add(new AIComponent());

        ImageComponent img = im.get(shock);
        img.texRegion = ImageComponent.atlas.findRegion("Spark1");
        img.rotate = false;

        PositionComponent pos = pm.get(shock);
        pos.x = x; pos.y = y;
        pos.width = width;
        pos.height = height;
        pos.rotation = 0;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(shock);
        move.speedPerSecond = speed;

        AnimationComponent anim = am.get(shock);
        anim.animate = true;
        anim.repeat = true;

        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Spark1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Spark1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Spark2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        DamageComponent dam = dm.get(shock);
        dam.damage = 35;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        AIComponent ai = aim.get(shock);
        ai.targetTime = 4f;
        ai.shoots = false;
        ai.AIType = 'w';
        ai.gradualTurning = false;

        return shock;
    }

    public static GameEntity generateFloatingPoison(float x, float y, float width, float height, float speed) {
        GameEntity pois = new GameEntity();

        pois.add(new PositionComponent());
        pois.add(new ImageComponent());
        pois.add(new MovementComponent());
        pois.add(new AIComponent());
        pois.add(new CollisionComponent());

        ImageComponent img = im.get(pois);
        img.texRegion = ImageComponent.atlas.findRegion("GEnemyProjectile");
        img.rotate = false;

        PositionComponent pos = pm.get(pois);
        pos.x = x; pos.y = y;
        pos.width = width;
        pos.height = height;
        pos.rotation = 0;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(pois);
        move.speedPerSecond = speed;

        CollisionComponent collision = cm.get(pois);
        collision.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, pos.rotation);
        collision.boundingBox.setOrigin(pos.width / 2 + pos.x, pos.height / 2 + pos.y);
        collision.collisionReaction = 8;
        collision.rotateBox = false;


        AIComponent ai = aim.get(pois);
        ai.targetTime = 4f;
        ai.shoots = false;
        ai.AIType = 'w';
        ai.gradualTurning = false;

        return pois;
    }

    /*
    Spawn Point -----------------------------------------------------------------
    -----------------------------------------------------------------------------
     */
    public static GameEntity generateEnemySpawnPoint(float x, float y, String spawned, float freq, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        if (spawned.equals("UFO")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemy(p.x, p.y));
                }
            };
        } else if (spawned.equals("BlueUFO")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateBlueEnemy(p.x, p.y));
                }
            };
        } else if (spawned.equals("GreenUFO")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateGreenEnemy(p.x, p.y));
                }
            };
        } else if (spawned.equals("YellowUFO")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateYellowEnemy(p.x, p.y));
                }
            };
        } else if (spawned.equals("GhostUFO")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateGhostUFO(p.x, p.y));
                }
            };
        } else if (spawned.equals("PinkUFO")) {//!!!
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateFasterEnemy(p.x, p.y));
                }
            };
        } else if (spawned.equals("RedUFO")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateRedEnemy(p.x, p.y));
                }
            };
        }  else if (spawned.equals("Wisp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateWisp(p.x, p.y));
                }
            };
        } else if (spawned.equals("PoisonWisp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generatePoisonWisp(p.x, p.y));
                }
            };
        } else if (spawned.equals("BigWisp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateBigWisp(p.x, p.y));
                }
            };
        } else if (spawned.equals("SwirlWisp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateSwirlWisp(p.x, p.y));
                }
            };
        } else if (spawned.equals("EnemyShipRed")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipRed(p.x, p.y));
                }
            };
        } else if (spawned.equals("EnemyShipBlue")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipBlue(p.x, p.y));
                }
            };
        } else if (spawned.equals("EnemyShipYellow")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipYellow(p.x, p.y));
                }
            };
        } else if (spawned.equals("EnemyShipGold")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipGold(p.x, p.y));
                }
            };
        } else if (spawned.equals("FastEnemy")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateFasterEnemy(p.x, p.y));
                }
            };
        }  else if (spawned.equals("GoldWisp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateGoldWisp(p.x, p.y));
                }
            };
        } else if (spawned.equals("PlatinumWisp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generatePlatinumWisp(p.x, p.y));
                }
            };
        } else if (spawned.equals("FinalUFO")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateFinalEnemy(p.x, p.y));
                }
            };
        } else if (spawned.equals("Core")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateCore(p.x, p.y));
                }
            };
        } else if (spawned.equals("PodShip")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generatePodShip(p.x, p.y));
                }
            };
        }
        return spawn;
    }

    public static GameEntity generateRandomEnemySpawnPoint(float x, float y, float freq, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                int i = (int) (Math.random() * 12);

                if (i == 0) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemy(p.x, p.y));
                } else if (i == 1) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateBlueEnemy(p.x, p.y));
                } else if (i == 2) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateGreenEnemy(p.x, p.y));
                } else if (i == 3) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateGhostUFO(p.x, p.y));
                } else if (i == 4) {
                    if ((int) Math.random() == 1) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateEnemyShipGold(p.x, p.y));
                    }
                } else if (i == 5) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateRedEnemy(p.x, p.y));
                } else if (i == 6) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateYellowEnemy(p.x, p.y));
                } else if (i == 7) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateWisp(p.x, p.y));
                } else if (i == 8) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generatePoisonWisp(p.x, p.y));
                } else if (i == 9) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateBigWisp(p.x, p.y));
                } else if (i == 10) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipRed(p.x, p.y));
                } else if (i == 11) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipBlue(p.x, p.y));
                }
            }
        };

        return spawn;
    }

    public static GameEntity generateRandomUFOSpawnPoint(float x, float y, float freq, boolean includeGhost, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        if (includeGhost) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    int i = (int) (Math.random() * 6);

                    if (i == 0) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateEnemy(p.x, p.y));
                    } else if (i == 1) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateBlueEnemy(p.x, p.y));
                    } else if (i == 2) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateGreenEnemy(p.x, p.y));
                    } else if (i == 3) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateRedEnemy(p.x, p.y));
                    } else if (i == 4) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateYellowEnemy(p.x, p.y));
                    } else if (i == 5) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateGhostUFO(p.x, p.y));
                    }
                }
            };
        } else {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    int i = (int) (Math.random() * 5);

                    if (i == 0) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateEnemy(p.x, p.y));
                    } else if (i == 1) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateBlueEnemy(p.x, p.y));
                    } else if (i == 2) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateGreenEnemy(p.x, p.y));
                    } else if (i == 3) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateRedEnemy(p.x, p.y));
                    } else if (i == 4) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateYellowEnemy(p.x, p.y));
                    }
                }
            };
        }

        return spawn;
    }

    public static GameEntity generateRandomWispSpawnPoint(float x, float y, float freq, boolean includeBigWisp, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        if (includeBigWisp) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    int i = (int) (Math.random() * 3);

                    if (i == 0) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateWisp(p.x, p.y));
                    } else if (i == 1) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateBigWisp(p.x, p.y));
                    } else if (i == 2) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generatePoisonWisp(p.x, p.y));
                    }
                }
            };
        } else {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    int i = (int) (Math.random() * 2);

                    if (i == 0) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateWisp(p.x, p.y));
                    } else if (i == 1) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generatePoisonWisp(p.x, p.y));
                    }
                }
            };
        }

        return spawn;
    }

    public static GameEntity generateRandomShipSpawnPoint(float x, float y, float freq, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                int i = (int) (Math.random() * 3);

                if (i == 0) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipRed(p.x, p.y));
                } else if (i == 1) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipBlue(p.x, p.y));
                } else if (i == 2) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipGold(p.x, p.y));
                }
            }
        };

        return spawn;
    }

    public static GameEntity generateItemSpawnPoint(float x, float y, String spawned, float freq, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        if (spawned.equals("Health")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateHealingItem(p.x, p.y));
                }
            };
        } else if (spawned.equals("MaxHealth")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateMaxHealingItem(p.x, p.y));
                }
            };
        } else if (spawned.equals("SpeedUp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateSpeedUp(p.x, p.y));
                }
            };
        } else if (spawned.equals("Red")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Red"));
                }
            };
        } else if (spawned.equals("Green")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Green"));
                }
            };
        } else if (spawned.equals("Blue")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Blue"));
                }
            };
        } else if (spawned.equals("Yellow")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Yellow"));
                }
            };
        } else if (spawned.equals("Pink")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Pink"));
                }
            };
        } else if (spawned.equals("Purple")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Purple"));
                }
            };
        } else if (spawned.equals("Orange")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Orange"));
                }
            };
        }  else if (spawned.equals("White")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "White"));
                }
            };
        } else if (spawned.equals("SuperShootUp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateSuperShootUp(p.x, p.y));
                }
            };
        } else if (spawned.equals("ShootUp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateShootUp(p.x, p.y));
                }
            };
        } else if (spawned.equals("DoubleUp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateDoubleUp(p.x, p.y));
                }
            };
        }

        return spawn;
    }

    public static GameEntity generateRandomItemSpawnPoint(float x, float y, float freq, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
            int i = (int) (Math.random() * 10);

            if (i == 0) {
                PositionComponent p = pm.get(e);
                engine.addEntity(generateHealingItem(p.x, p.y));
            } else if (i == 1) {
                PositionComponent p = pm.get(e);
                engine.addEntity(generateMaxHealingItem(p.x, p.y));
            }else if (i == 2) {
                PositionComponent p = pm.get(e);
                engine.addEntity(generateShootUp(p.x, p.y));
            } else if (i == 3) {
                PositionComponent p = pm.get(e);
                engine.addEntity(generateSpeedUp(p.x, p.y));
            } else if (i == 4) {
                PositionComponent p = pm.get(e);
                engine.addEntity(generateColorPowerUp(p.x, p.y, "Red"));
            } else if (i == 5) {
                PositionComponent p = pm.get(e);
                engine.addEntity(generateColorPowerUp(p.x, p.y, "Green"));
            } else if (i == 6) {
                PositionComponent p = pm.get(e);
                engine.addEntity(generateColorPowerUp(p.x, p.y, "Blue"));
            } else if (i == 7) {
                PositionComponent p = pm.get(e);
                engine.addEntity(generateColorPowerUp(p.x, p.y, "Yellow"));
            } else if (i == 8) {
                PositionComponent p = pm.get(e);
                engine.addEntity(generateColorPowerUp(p.x, p.y, "Pink"));
            } else if (i == 9) {
                PositionComponent p = pm.get(e);
                engine.addEntity(generateColorPowerUp(p.x, p.y, "Purple"));
            }
            }
        };

        return spawn;
    }

    public static GameEntity generateRandomColorSpawnPoint(float x, float y, float freq, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                int i = (int) (Math.random() * 6);

                if (i == 0) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Red"));
                } else if (i == 1) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Green"));
                } else if (i == 2) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Blue"));
                } else if (i == 3) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Yellow"));
                } else if (i == 4) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Pink"));
                } else if (i == 5) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Purple"));
                }
            }
        };

        return spawn;
    }

    public static GameEntity generateRandomPowerUp(float x, float y, float freq, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {

                int i = (int) (Math.random() * 4);

                if (i == 0) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateHealingItem(p.x, p.y));
                } else if (i == 1) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateShootUp(p.x, p.y));
                } else if (i == 2) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateSpeedUp(p.x, p.y));
                } else if (i == 3) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateMaxHealingItem(p.x, p.y));
                }
            }
        };

        return spawn;
    }

    public static GameEntity generateMovingItemSpawnPoint(float x, float y, String spawned, float freq, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());
        spawn.add(new MovementComponent());
        spawn.add(new AIComponent());

        MovementComponent move = mm.get(spawn);
        move.speedPerSecond = 400f;

        AIComponent ai = aim.get(spawn);
        ai.AIType = 'w';
        ai.targetTime = 3f;
        ai.shoots = false;
        ai.gradualTurning = false;

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        if (spawned.equals("Health")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateHealingItem(p.x, p.y));
                }
            };
        } else if (spawned.equals("MaxHealth")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateMaxHealingItem(p.x, p.y));
                }
            };
        } else if (spawned.equals("SpeedUp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateSpeedUp(p.x, p.y));
                }
            };
        } else if (spawned.equals("Red")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Red"));
                }
            };
        } else if (spawned.equals("Green")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Green"));
                }
            };
        } else if (spawned.equals("Blue")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Blue"));
                }
            };
        } else if (spawned.equals("Yellow")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Yellow"));
                }
            };
        } else if (spawned.equals("Pink")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Pink"));
                }
            };
        } else if (spawned.equals("Purple")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Purple"));
                }
            };
        } else if (spawned.equals("Orange")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Orange"));
                }
            };
        } else if (spawned.equals("White")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "White"));
                }
            };
        } else if (spawned.equals("SuperShootUp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateSuperShootUp(p.x, p.y));
                }
            };
        } else if (spawned.equals("ShootUp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateShootUp(p.x, p.y));
                }
            };
        } else if (spawned.equals("DoubleUp")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateDoubleUp(p.x, p.y));
                }
            };
        }

        return spawn;
    }

    public static GameEntity generateMovingRandomPowerUp(float x, float y, float freq, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());
        spawn.add(new MovementComponent());
        spawn.add(new AIComponent());

        MovementComponent move = mm.get(spawn);
        move.speedPerSecond = 300f;

        AIComponent ai = aim.get(spawn);
        ai.AIType = 'w';
        ai.targetTime = 3f;
        ai.shoots = false;

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {

                int i = (int) (Math.random() * 4);

                if (i == 0) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateHealingItem(p.x, p.y));
                } else if (i == 1) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateShootUp(p.x, p.y));
                } else if (i == 2) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateSpeedUp(p.x, p.y));
                } else if (i == 3) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateMaxHealingItem(p.x, p.y));
                }
            }
        };

        return spawn;
    }

    public static GameEntity generateMovingRandomColorSpawnPoint(float x, float y, float freq, Engine engine) {
        GameEntity spawn = new GameEntity();

        spawn.add(new PositionComponent());
        spawn.add(new EventComponent());
        spawn.add(new AIComponent());
        spawn.add(new MovementComponent());

        MovementComponent move = mm.get(spawn);
        move.speedPerSecond = 300f;

        AIComponent ai = aim.get(spawn);
        ai.AIType = 'w';
        ai.targetTime = 3f;
        ai.shoots = false;

        PositionComponent pos = pm.get(spawn);
        pos.x = x;
        pos.y = y;
        pos.width = 10f;
        pos.height = 10f;
        pos.rotation = 0f;
        PositionSystem.setOrigins(pos);
        pos.drawable = false;

        EventComponent event = em.get(spawn);
        event.ticking = true;
        event.repeat = true;
        event.targetTime = freq;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                int i = (int) (Math.random() * 6);

                if (i == 0) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Red"));
                } else if (i == 1) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Green"));
                } else if (i == 2) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Blue"));
                } else if (i == 3) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Yellow"));
                } else if (i == 4) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Pink"));
                } else if (i == 5) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateColorPowerUp(p.x, p.y, "Purple"));
                }
            }
        };

        return spawn;
    }
}
