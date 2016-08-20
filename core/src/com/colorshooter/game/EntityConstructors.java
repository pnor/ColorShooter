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
    PLAYER Related -----------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generatePlayer(float x, float y) {
        GameEntity player = new GameEntity("player");

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

        PlayerInputComponent controller = pim.get(player);
        controller.forward = Input.Keys.W;
        controller.back = Input.Keys.S;
        controller.right = Input.Keys.D;
        controller.left = Input.Keys.A;
        controller.shoot = Input.Keys.E;
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
        move.speedPerSecond = 300f;
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
        collision.boundingBox.setOrigin(collision.boundingBox.getBoundingRectangle().getWidth() / 1.5f, collision.boundingBox.getBoundingRectangle().getHeight() / 1.5f);
        collision.collisionReaction = 0;
        collision.rotateBox = true;

        BouncingComponent bounce = bm.get(player);
        bounce.isBouncing = false;
        bounce.bounceDuration = 2f;

        PoisonComponent pois = poim.get(player);
        pois.poisonDuration = 10f;
        pois.isPoisoned = false;

        FrozenComponent frozen = fm.get(player);
        frozen.frozenDuration = 3f;
        frozen.isFrozen = false;

        HealthComponent heal = hm.get(player);
        heal.maxHealth = 150;
        heal.health = 150;
        heal.invinciblityDuration = 0.6f;
        heal.isAlive = true;
        heal.tag = 'p';
        heal.deathAction = 3;

        ShootComponent sho = shm.get(player);
        sho.attackDelay = 0.2f;

        player.add(new PointsComponent());
        pom.get(player).points = -15000;

        return player;
    }

    /*
    In-Animate Related -------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generateWall(float x, float y, float width, float height) {
        GameEntity wall = new GameEntity("wall");

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

    public static GameEntity generateLaser(float x, float y, float rotation, TextureRegion texReg, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity("laser");

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
        col.boundingBox.setOrigin(col.boundingBox.getBoundingRectangle().getWidth() / 2f, col.boundingBox.getBoundingRectangle().getHeight() / 2f);
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
        GameEntity laser = new GameEntity("explosion laser");

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
                    anim.animations = new ArrayList<ArrayList<TextureRegion>>();
                    anim.animations.add(new ArrayList<TextureRegion>());
                    anim.baseTextureRegion = ImageComponent.atlas.findRegion("DeathExplosion");
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion"));
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion2"));
                    anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion3"));
                    anim.currentAnimation = 0;
                    anim.currentIndex = 0;
                    anim.animationTime = 0.2f;

                    DamageComponent dam = dm.get(e);
                    dam.damage = 20;
                    dam.tag = 'n';

                    em.get(e).targetTime = 0.1f;
                    em.get(e).event = new GameEvent() {
                        @Override
                        public void event(GameEntity e, Engine engine) {
                            if (dm.get(e).damage >= 4)
                                dm.get(e).damage -= 4;
                        }
                    };

                    lfm.get(e).endTime = 1f;
                }
            }
        };

        return laser;
    }

    public static GameEntity generateExplosion(float x, float y) {
        GameEntity explosion = new GameEntity("explosion");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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

    public static GameEntity generateBigExplosion(float x, float y) {
        GameEntity explosion = new GameEntity("explosion");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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

    public static GameEntity generatePlayerExplosion(float x, float y) {
        GameEntity explosion = new GameEntity("explosion");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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

    public static GameEntity generateMine(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity mine = new GameEntity("mine");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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
                pos.width = 200;
                pos.height = 200;
                pos.x = pos.originX + pos.x - pos.width / 2;
                pos.y = pos.originY + pos.y  - pos.height / 2;
                PositionSystem.setOrigins(pm.get(e));

                AnimationComponent anim = am.get(e);
                anim.animate = true;
                anim.repeat = true;
                anim.animations = new ArrayList<ArrayList<TextureRegion>>();
                anim.animations.add(new ArrayList<TextureRegion>());
                anim.baseTextureRegion = ImageComponent.atlas.findRegion("BigExplosion");
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("BigExplosion"));
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("BigExplosion2"));
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("BigExplosion3"));
                anim.currentAnimation = 0;
                anim.currentIndex = 0;
                anim.animationTime = 0.2f;

                DamageComponent dam = dm.get(e);
                dam.damage = 45;
                dam.tag = 'e';

                em.get(e).targetTime = 0.05f;
                em.get(e).event = new GameEvent() {
                    @Override
                    public void event(GameEntity e, Engine engine) {
                        if ( dm.get(e).damage >= 9)
                            dm.get(e).damage -= 9;
                    }
                };

                lfm.get(e).endTime = 1.5f;
            }
        };

        return mine;
    }

    public static GameEntity generateRicochetLaser(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity("ricochet");

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
        move.speedPerSecond = 400;

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(col.boundingBox.getBoundingRectangle().getWidth() / 2f, col.boundingBox.getBoundingRectangle().getHeight() / 2f);
        col.collisionReaction = 3;
        col.rotateBox = true;

        DamageComponent dam = dm.get(laser);
        dam.damage = 5;
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
                    mm.get(e).speedPerSecond += 300f;
                    dm.get(e).damage += 2;
                }
            };
        } else if (temp == 1) {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation -= 3f;
                    mm.get(e).speedPerSecond += 300f;
                    dm.get(e).damage += 2;
                }
            };
        } else if (temp == 2) {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation += 1f;
                    mm.get(e).speedPerSecond += 270f;
                    dm.get(e).damage += 3;
                }
            };
        } else {
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    pm.get(e).rotation -= 1f;
                    mm.get(e).speedPerSecond += 260f;
                    dm.get(e).damage += 4;
                }
            };
        }

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 1.2f;

        return laser;
    }

    public static GameEntity generatePiercingArrow(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity("pierce arrow");

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
        pos.width = img.texRegion.getRegionWidth();
        pos.height = img.texRegion.getRegionHeight();
        pos.rotation = rotation;
        PositionSystem.setOrigins(pos);

        MovementComponent move = mm.get(laser);
        move.speedPerSecond = 1500f;

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("PierceArrow1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PierceArrow1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PierceArrow2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.1f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 20;
        if (hurtsPlayer == 0)
            dam.tag = 'e';
        else if (hurtsPlayer == 1)
            dam.tag = 'p';
        else
            dam.tag = 'n';

        LifetimeComponent life = lfm.get(laser);
        life.endTime = 0.6f;

        return laser;
    }

    public static GameEntity generateHomingMissile(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity missile = new GameEntity("missile");

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
        col.boundingBox.setOrigin(col.boundingBox.getBoundingRectangle().getWidth() / 2f, col.boundingBox.getBoundingRectangle().getHeight() / 2f);
        col.collisionReaction = 5;
        col.rotateBox = true;

        MovementComponent move = mm.get(missile);
        move.speedPerSecond = 400f;

        AnimationComponent anim = am.get(missile);
        anim.animate = true;
        anim.repeat = true;
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Missle1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Missle1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Missle2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.1f;

        DamageComponent dam = dm.get(missile);
        dam.damage = 20;
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
                    float currentDistance = 999f;
                    for (int i = 1; i < enemies.size(); i++) {
                        currentDistance = MovementSystem.getDistance(pos, pm.get(enemies.get(0)).x + pm.get(enemies.get(0)).originX, pm.get(enemies.get(0)).y + pm.get(enemies.get(0)).originY);
                        if (currentDistance <= smallestDistance) {
                            smallestIndex = i;
                            smallestDistance = currentDistance;
                        }
                    }
                    PositionSystem.lookAt(pos, pm.get(enemies.get(smallestIndex)).x + pm.get(enemies.get(smallestIndex)).originX, pm.get(enemies.get(smallestIndex)).y + pm.get(enemies.get(smallestIndex)).originY, cm.get(e));
                }

                mm.get(e).speedPerSecond += 100;
                if (mm.get(e).speedPerSecond >= 1050) {
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
                    anim.animations = new ArrayList<ArrayList<TextureRegion>>();
                    anim.animations.add(new ArrayList<TextureRegion>());
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
        GameEntity laser = new GameEntity("pink player laser");

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
        col.boundingBox.setOrigin(col.boundingBox.getBoundingRectangle().getWidth() / 2f, col.boundingBox.getBoundingRectangle().getHeight() / 2f);
        col.collisionReaction = 4;
        col.rotateBox = true;

        DamageComponent dam = dm.get(laser);
        dam.damage = 13;
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
    Enemy Related -------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generateEnemyExplosionLaser(float x, float y, float rotation) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity("explosion laser");

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
                anim.animations = new ArrayList<ArrayList<TextureRegion>>();
                anim.animations.add(new ArrayList<TextureRegion>());
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
        GameEntity laser = new GameEntity("swirl");

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
        col.boundingBox.setOrigin(col.boundingBox.getBoundingRectangle().getWidth() / 2f, col.boundingBox.getBoundingRectangle().getHeight() / 2f);
        col.collisionReaction = 7;
        col.rotateBox = true;

        AnimationComponent anim = am.get(laser);
        anim.animate = true;
        anim.repeat = true;
        anim.animations = new ArrayList<ArrayList<TextureRegion>>();
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("BEnemyProjectile1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BEnemyProjectile1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BEnemyProjectile2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 20;
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
        GameEntity laser = new GameEntity("ghost laser");

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new CollisionComponent());
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

        CollisionComponent col = cm.get(laser);
        col.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        col.boundingBox.setOrigin(col.boundingBox.getBoundingRectangle().getWidth() / 2f, col.boundingBox.getBoundingRectangle().getHeight() / 2f);
        col.collisionReaction = 5;
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

    public static GameEntity generateWispProjectile(float x, float y, float rotation, TextureRegion texReg, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity("laser");

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
        GameEntity laser = new GameEntity("poison wisp attack");

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
        dam.damage = 12;
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

        lfm.get(laser).endTime = 7f;

        return laser;
    }

    public static GameEntity generateShockWave(float x, float y, float rotation, int hurtsPlayer) {
        //hurtsPlayer: 0 -> hurts enemy  1 -> hurts player 2 -> hurts all
        GameEntity laser = new GameEntity("shock wave");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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
        GameEntity missile = new GameEntity("missile");

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
        col.boundingBox.setOrigin(col.boundingBox.getBoundingRectangle().getWidth() / 2f, col.boundingBox.getBoundingRectangle().getHeight() / 2f);
        col.collisionReaction = 0;
        col.rotateBox = true;

        MovementComponent move = mm.get(missile);
        move.speedPerSecond = 25f;

        AnimationComponent anim = am.get(missile);
        anim.animate = true;
        anim.repeat = true;
        anim.animations.add(new ArrayList<TextureRegion>());
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
                    anim.animations = new ArrayList<ArrayList<TextureRegion>>();
                    anim.animations.add(new ArrayList<TextureRegion>());
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
        GameEntity laser = new GameEntity("bubble attack");

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
        anim.animations = new ArrayList<ArrayList<TextureRegion>>();
        anim.animations.add(new ArrayList<TextureRegion>());
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
        GameEntity laser = new GameEntity("fire laser");

        laser.add(new PositionComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new EventComponent());
        laser.add(new AnimationComponent());
        laser.add(new DamageComponent());

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
        anim.animations = new ArrayList<ArrayList<TextureRegion>>();
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Fire1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Fire1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Fire2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 10;
        dam.tag = 'p';

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

                im.get(e).texRegion = ImageComponent.atlas.findRegion("DeathExplosion");

                pm.get(e).width = 100;
                pm.get(e).height = 100;
                PositionSystem.setOrigins(pm.get(e));

                AnimationComponent anim = am.get(e);
                anim.animate = true;
                anim.repeat = true;
                anim.animations = new ArrayList<ArrayList<TextureRegion>>();
                anim.animations.add(new ArrayList<TextureRegion>());
                anim.baseTextureRegion = ImageComponent.atlas.findRegion("DeathExplosion");
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion"));
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion2"));
                anim.animations.get(0).add(ImageComponent.atlas.findRegion("DeathExplosion3"));
                anim.currentAnimation = 0;
                anim.currentIndex = 0;
                anim.animationTime = 0.2f;

                DamageComponent dam = dm.get(e);
                dam.damage = 18;
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
        GameEntity laser = new GameEntity("laser");

        laser.add(new PositionComponent());
        laser.add(new DamageComponent());
        laser.add(new ImageComponent());
        laser.add(new MovementComponent());
        laser.add(new CollisionComponent());
        laser.add(new LifetimeComponent());
        laser.add(new EventComponent());

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
        col.boundingBox.setOrigin(col.boundingBox.getBoundingRectangle().getWidth() / 2f, col.boundingBox.getBoundingRectangle().getHeight() / 2f);
        col.collisionReaction = 9;
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
        GameEntity laser = new GameEntity("laser");

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
        anim.animations = new ArrayList<ArrayList<TextureRegion>>();
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Thunder1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Thunder1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Thunder2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        DamageComponent dam = dm.get(laser);
        dam.damage = 13;
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
        item.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (hm.get(e).maxHealth >= 3)
                    hm.get(e).maxHealth -= 2;
            }
        };

        return laser;
    }


    /*
    Enemy ------------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generateEnemy(float x, float y) {
        GameEntity enemy = new GameEntity("enemy");

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AnimationComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 120f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("UFO1");

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("UFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO3"));
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
        AI.awarenessRadius = 200f;
        AI.AIType = 'r';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'l';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 50;
        health.health = 50;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.3f;

        bm.get(enemy).bounceDuration = 2f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 100;

        return enemy;
    }

    public static GameEntity generateFasterEnemy(float x, float y) {
        GameEntity enemy = new GameEntity("enemy");

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AnimationComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 240f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("UFO1");

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("UFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("UFO3"));
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
        AI.awarenessRadius = 200f;
        AI.AIType = 'r';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'l';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 50;
        health.health = 50;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.3f;

        bm.get(enemy).bounceDuration = 2f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 777;

        return enemy;
    }

    public static GameEntity generateBlueEnemy(float x, float y) {
        GameEntity enemy = new GameEntity("blue enemy");

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AnimationComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 160f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("BlueUFO1");

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("BlueUFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BlueUFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BlueUFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("BlueUFO3"));
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
        AI.awarenessRadius = 300;
        AI.AIType = 'l';
        AI.targetTime = 4f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 's';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 45;
        health.health = 45;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 2.5f;

        bm.get(enemy).bounceDuration = 3.5f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 250;

        return enemy;
    }

    public static GameEntity generateRedEnemy(float x, float y) {
        GameEntity enemy = new GameEntity("red enemy");

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AnimationComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 130f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("RedUFO1");

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("RedUFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedUFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedUFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedUFO3"));
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
        AI.awarenessRadius = 370f;
        AI.AIType = 'e';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'e';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 65;
        health.health = 65;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.8f;

        bm.get(enemy).bounceDuration = 2f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 200;

        return enemy;
    }

    public static GameEntity generateGreenEnemy(float x, float y) {
        GameEntity enemy = new GameEntity("green enemy");

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AnimationComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new EventComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 150f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("GreenUFO1");

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("GreenUFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenUFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenUFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenUFO3"));
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
        AI.awarenessRadius = 250f;
        AI.AIType = 'a';
        AI.targetTime = 1f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'l';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 50;
        health.health = 30;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.2f;

        EventComponent event = em.get(enemy);
        event.repeat = true;
        event.targetTime = 1.5f;
        event.ticking = true;
        event.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                hm.get(e).health += 20;
                im.get(e).texRegion = ImageComponent.atlas.findRegion("GhostUFO1");
            }
        };

        bm.get(enemy).bounceDuration = 1.5f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 300;

        return enemy;
    }

    public static GameEntity generatePinkEnemy(float x, float y) {
        GameEntity enemy = new GameEntity("pink enemy");

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AnimationComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 100f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("PinkUFO1");

        AnimationComponent anim = am.get(enemy);
        anim.animate = true;
        anim.repeat = true;
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("PinkUFO1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PinkUFO1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PinkUFO2"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("PinkUFO3"));
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
        AI.awarenessRadius = 250f;
        AI.AIType = 'a';
        AI.targetTime = 1f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'p';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 90;
        health.health = 90;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 1.5f;

        bm.get(enemy).bounceDuration = 0.7f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 450;

        return enemy;
    }

    public static GameEntity generateGhostUFO(float x, float y) {
        GameEntity enemy = new GameEntity("ghost ufo");

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
        health.maxHealth = 250;
        health.health = 250;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.2f;

        bm.get(enemy).bounceDuration = 2f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 400;

        return enemy;
    }

    public static GameEntity generateWisp(float x, float y) {
        GameEntity enemy = new GameEntity("wisp");

        enemy.add(new PositionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new HealthComponent());
        enemy.add(new DamageComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 80f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("Wisp");

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 200f;
        AI.AIType = 'r';
        AI.targetTime = 4f;
        AI.targetRotation = position.rotation;
        AI.shoots = false;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 50;
        health.health = 50;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 0;

        DamageComponent dam = dm.get(enemy);
        dam.damage = 20;
        dam.tag = 'p';

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 100;

        return enemy;
    }

    public static GameEntity generatePoisonWisp(float x, float y) {
        GameEntity enemy = new GameEntity("poison wisp");

        enemy.add(new PositionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new HealthComponent());
        enemy.add(new DamageComponent());
        enemy.add(new CollisionComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 90f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("PoisonWisp");

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
        collision.collisionReaction = 8;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 400f;
        AI.AIType = 'r';
        AI.targetTime = 5f;
        AI.targetRotation = position.rotation;
        AI.shoots = false;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 90;
        health.health = 90;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 0;

        DamageComponent dam = dm.get(enemy);
        dam.damage = 8;
        dam.tag = 'p';

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 200;

        return enemy;
    }

    public static GameEntity generateBigWisp(float x, float y) {
        GameEntity enemy = new GameEntity("big wisp");

        enemy.add(new PositionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new HealthComponent());
        enemy.add(new DamageComponent());
        enemy.add(new EventComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 50f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("BigWisp1");

        PositionComponent position = pm.get(enemy);
        position.x = x;
        position.y = y;
        position.height = img.texRegion.getRegionHeight();
        position.width = img.texRegion.getRegionWidth();
        PositionSystem.setOrigins(position);
        position.rotation = (float) Math.random() * 360;

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 450f;
        AI.AIType = 'r';
        AI.targetTime = 10f;
        AI.targetRotation = position.rotation;
        AI.shoots = false;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 190;
        health.health = 190;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 0;

        DamageComponent dam = dm.get(enemy);
        dam.damage = 10;
        dam.tag = 'p';

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
                    anim.animations.add(new ArrayList<TextureRegion>());
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

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 550;

        return enemy;
    }

    public static GameEntity generateEnemyShipRed(float x, float y) {
        GameEntity enemy = new GameEntity("enemy ship red");

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 180f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("EnemyShip");
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
        AI.projectileType = 'f';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 90;
        health.health = 90;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.9f;

        bm.get(enemy).bounceDuration = 0.6f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 600;

        return enemy;
    }

    public static GameEntity generateEnemyShipBlue(float x, float y) {
        GameEntity enemy = new GameEntity("enemy ship blue");

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 50f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("EnemyShipIce");
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
        AI.awarenessRadius = 450f;
        AI.AIType = 'r';
        AI.targetTime = 5f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'c';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 80;
        health.health = 80;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 3f;

        bm.get(enemy).bounceDuration = 4f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 600;

        return enemy;
    }

    public static GameEntity generateEnemyShipYellow(float x, float y) {
        GameEntity enemy = new GameEntity("enemy ship yellow");

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new BouncingComponent());

        MovementComponent mov = mm.get(enemy);
        mov.speedPerSecond = 160f;
        mov.move = true;

        ImageComponent img = im.get(enemy);
        img.texRegion = ImageComponent.atlas.findRegion("EnemyShipThunder");
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
        AI.awarenessRadius = 450f;
        AI.AIType = 'r';
        AI.targetTime = 5f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 't';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 80;
        health.health = 80;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.8f;

        bm.get(enemy).bounceDuration = 4f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 770;

        return enemy;
    }

    public static GameEntity generateTurret(float x, float y) {
        GameEntity enemy = new GameEntity("turret");

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

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 800f;
        AI.AIType = 'f';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'l';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 150;
        health.health = 150;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.3f;

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 300;

        return enemy;
    }

    public static GameEntity generateBossWisp(float x, float y) {
        GameEntity enemy = new GameEntity("boss wisp");

        enemy.add(new PositionComponent());
        enemy.add(new MovementComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new HealthComponent());
        enemy.add(new DamageComponent());
        enemy.add(new EventComponent());
        enemy.add(new CollisionComponent());

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
        AI.awarenessRadius = 450f;
        AI.AIType = 'r';
        AI.targetTime = 10f;
        AI.targetRotation = position.rotation;

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 1200;
        health.health = 1200;
        health.invinciblityDuration = 0.3f;
        health.tag = 'e';
        health.deathAction = 0;

        DamageComponent dam = dm.get(enemy);
        dam.damage = 10;
        dam.tag = 'p';

        EventComponent eve = em.get(enemy);
        eve.targetTime = 3f;
        eve.repeat = true;
        eve.ticking = true;
        eve.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (mm.get(e).speedPerSecond == 50f) {
                    PositionComponent pos = pm.get(e);
                    mm.get(e).speedPerSecond = 150f;

                    for (int i = 0; i < 12; i ++) {
                        engine.addEntity(generatePoisonWispProjectile(pos.x + pos.originX, pos.y + pos.originY, i * 30));
                    }
                    engine.addEntity(generatePoisonWisp(pos.x + pos.originX + 10, pos.y + pos.originY));
                    engine.addEntity(generatePoisonWisp(pos.x + pos.originX + 10, pos.y + pos.originY + 20));

                } else {
                    mm.get(e).speedPerSecond = 50f;
                }
            }
        };

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 2500;

        return enemy;
    }

    public static GameEntity generateBossUFO(float x, float y) {
        GameEntity enemy = new GameEntity("boss enemy");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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
                        engine.addEntity(generateWeakLaser(pos.x + pos.originX, pos.y + pos.originY, i * 40,ImageComponent.atlas.findRegion("EnemyLaser"), 1));
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
        pom.get(enemy).points = 5000;

        return enemy;
    }

    public static GameEntity generateBossTurret(float x, float y) {
        GameEntity enemy = new GameEntity("turret");

        enemy.add(new PositionComponent());
        enemy.add(new CollisionComponent());
        enemy.add(new ImageComponent());
        enemy.add(new AIComponent());
        enemy.add(new ShootComponent());
        enemy.add(new HealthComponent());
        enemy.add(new EventComponent());

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

        AIComponent AI = aim.get(enemy);
        AI.awarenessRadius = 800f;
        AI.AIType = 'f';
        AI.targetTime = 2f;
        AI.targetRotation = position.rotation;
        AI.shoots = true;
        AI.projectileType = 'h';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 1200;
        health.health = 1200;
        health.invinciblityDuration = 0.09f;
        health.tag = 'e';
        health.deathAction = 1;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 1f;

        EventComponent ev =  em.get(enemy);
        ev.targetTime = 10f;
        ev.ticking = true;
        ev.repeat = true;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                if (aim.get(e).projectileType == 'h') {
                    shm.get(e).attackDelay = 0.3f;
                    aim.get(e).projectileType = 'l';
                } else if (aim.get(e).projectileType == 'l') {
                    shm.get(e).attackDelay = 0.7f;
                    aim.get(e).projectileType = 'c';
                } else if (aim.get(e).projectileType == 'c') {
                    shm.get(e).attackDelay = 1f;
                    aim.get(e).projectileType = 'h';
                }
            }
        };

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 300;

        return enemy;
    }

    public static GameEntity generateBossShip(float x, float y) {
        GameEntity enemy = new GameEntity("boss ship");

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
        mov.speedPerSecond = 250f;
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
        AI.projectileType = 't';

        HealthComponent health = hm.get(enemy);
        health.isAlive = true;
        health.maxHealth = 1900;
        health.health = 1900;
        health.invinciblityDuration = 0.15f;
        health.tag = 'e';
        health.deathAction = 2;

        ShootComponent shoot = shm.get(enemy);
        shoot.attackDelay = 0.42f;

        bm.get(enemy).bounceDuration = 0.3f;

        EventComponent ev = em.get(enemy);
        ev.repeat = true;
        ev.ticking = true;
        ev.targetTime = 3.5f;
        ev.event = new GameEvent() {
            @Override
            public void event(GameEntity e, Engine engine) {
                PositionComponent pos = pm.get(e);
                AIComponent ai = aim.get(e);

                if (ai.projectileType == 't') {
                    for (int i = 0; i < 6; i++) {
                        engine.addEntity(generateThunderLaser(pos.x + pos.originX, pos.y + pos.originY, i * 60, 1));
                    }
                    ai.projectileType = 'f';
                    ai.awarenessRadius = 300;
                    shm.get(e).attackDelay = 0.4f;
                    mm.get(e).speedPerSecond = 250f;
                    ai.AIType = 'x';
                } else if (ai.projectileType == 'f') {
                    for (int i = 0; i < 9; i++) {
                        engine.addEntity(generateThunderLaser(pos.x + pos.originX, pos.y + pos.originY, i * 0, 1));
                    }
                    ai.projectileType = 'c';
                    ai.AIType = 'e';
                    ai.awarenessRadius = 300;
                    shm.get(e).attackDelay = 0.3f;
                } else if (ai.projectileType == 'c') {
                    for (int i = 0; i < 12; i++) {
                        engine.addEntity(generateThunderLaser(pos.x + pos.originX, pos.y + pos.originY, i * 30, 1));
                    }
                    ai.projectileType = 't';
                    ai.AIType = 'x';
                    ai.awarenessRadius = 400;
                    shm.get(e).attackDelay = 0.42f;
                    mm.get(e).speedPerSecond = 250f;
                }
            }
        };

        enemy.add(new PointsComponent());
        pom.get(enemy).points = 10000;

        return enemy;
    }


    /*
   Items -----------------------------------------------------------------------
   -----------------------------------------------------------------------------
    */
    public static GameEntity generateHealingItem(float x, float y) {
        GameEntity healer = new GameEntity("health");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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
                hm.get(e).health += hm.get(e).maxHealth / 2;
            }
        };

        return healer;
    }

    public static GameEntity generateMaxHealingItem(float x, float y) {
        GameEntity healer = new GameEntity("max health");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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
                hm.get(e).health = hm.get(e).maxHealth;
                hm.get(e).maxHealth += 10;
            }
        };

        return healer;
    }

    public static GameEntity generateSuperShootUp(float x, float y) {
        GameEntity shot = new GameEntity("super shoot up");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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
                if (shm.get(e).attackDelay >= 0.125f)
                    shm.get(e).attackDelay -= 0.11f;
            }
        };

        return shot;
    }

    public static GameEntity generateShootUp(float x, float y) {
        GameEntity shot = new GameEntity("shoot up");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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
                if (shm.get(e).attackDelay >= 0.09f)
                    shm.get(e).attackDelay -= 0.025f;
            }
        };

        return shot;
    }

    public static GameEntity generateSpeedUp(float x, float y) {
        GameEntity speed = new GameEntity("Speed Up");

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
        anim.animations.add(new ArrayList<TextureRegion>());
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
                if (mm.get(e).speedPerSecond <= 1000f)
                    mm.get(e).speedPerSecond += 30f;
            }
        };

        return speed;
    }

    public static GameEntity generateColorPowerUp(float x, float y, String color) {
        GameEntity power = new GameEntity("color");

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
            anim.animations.add(new ArrayList<TextureRegion>());
            anim.baseTextureRegion = ImageComponent.atlas.findRegion("RedPower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedPower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("RedPower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    colm.get(e).color = 'r';
                }
            };
        } else if (color.equals("Blue")) {
            img.texRegion = ImageComponent.atlas.findRegion("BluePower1");
            anim.animations.add(new ArrayList<TextureRegion>());
            anim.baseTextureRegion = ImageComponent.atlas.findRegion("BluePower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("BluePower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("BluePower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    colm.get(e).color = 'b';
                }
            };

        } else if (color.equals("Green")) {
            img.texRegion = ImageComponent.atlas.findRegion("GreenPower1");
            anim.animations.add(new ArrayList<TextureRegion>());
            anim.baseTextureRegion = ImageComponent.atlas.findRegion("GreenPower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenPower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("GreenPower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    colm.get(e).color = 'g';
                }
            };
        } else if (color.equals("Yellow")) {
            img.texRegion = ImageComponent.atlas.findRegion("YellowPower1");
            anim.animations.add(new ArrayList<TextureRegion>());
            anim.baseTextureRegion = ImageComponent.atlas.findRegion("YellowPower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("YellowPower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("YellowPower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    colm.get(e).color = 'y';
                }
            };
        } else if (color.equals("Pink")) {
            img.texRegion = ImageComponent.atlas.findRegion("PinkPower1");
            anim.animations.add(new ArrayList<TextureRegion>());
            anim.baseTextureRegion = ImageComponent.atlas.findRegion("PinkPower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("PinkPower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("PinkPower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    colm.get(e).color = 'p';
                }
            };
        } else if (color.equals("Purple")) {
            img.texRegion = ImageComponent.atlas.findRegion("PurplePower1");
            anim.animations.add(new ArrayList<TextureRegion>());
            anim.baseTextureRegion = ImageComponent.atlas.findRegion("PurplePower1");
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("PurplePower1"));
            anim.animations.get(0).add(ImageComponent.atlas.findRegion("PurplePower2"));
            item.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    colm.get(e).color = 'v';
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
        life.endTime = 7f;

        return power;
    }

    /*
    Obstacles -------------------------------------------------------------
    --------------------------------------------------------------------------------
     */
    public static GameEntity generateFloatingShock(float x, float y, float width, float height, int hurtsPlayer) {
        GameEntity shock = new GameEntity("random shock");

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
        move.speedPerSecond = 60f;

        AnimationComponent anim = am.get(shock);
        anim.animate = true;
        anim.repeat = true;
        anim.animations.add(new ArrayList<TextureRegion>());
        anim.baseTextureRegion = ImageComponent.atlas.findRegion("Spark1");
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Spark1"));
        anim.animations.get(0).add(ImageComponent.atlas.findRegion("Spark2"));
        anim.currentAnimation = 0;
        anim.currentIndex = 0;
        anim.animationTime = 0.2f;

        DamageComponent dam = dm.get(shock);
        dam.damage = 20;
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

    public static GameEntity generateFloatingPoison(float x, float y, float width, float height) {
        GameEntity pois = new GameEntity("random poison");

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
        move.speedPerSecond = 40f;

        CollisionComponent collision = cm.get(pois);
        collision.boundingBox = new Polygon(new float[] {
                pos.x, pos.y,
                pos.x + pos.width, pos.y,
                pos.x + pos.width, pos.y + pos.height,
                pos.x, pos.y + pos.height
        });
        CollisionSystem.setBoundingBoxLocation(collision, 0, 0, pos.rotation);
        collision.boundingBox.setOrigin(pos.x + pos.originX, pos.y + pos.originY);
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
        GameEntity spawn = new GameEntity("spawn");

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
        } else if (spawned.equals("GhostUFO")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateGhostUFO(p.x, p.y));
                }
            };
        } else if (spawned.equals("PinkUFO")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generatePinkEnemy(p.x, p.y));
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
        } else if (spawned.equals("FastEnemy")) {
            event.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateFasterEnemy(p.x, p.y));
                }
            };
        }
        return spawn;
    }

    public static GameEntity generateRandomEnemySpawnPoint(float x, float y, float freq, Engine engine) {
        GameEntity spawn = new GameEntity("spawn");

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
                int i = (int) (Math.random() * 11);

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
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generatePinkEnemy(p.x, p.y));
                } else if (i == 5) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateRedEnemy(p.x, p.y));
                } else if (i == 6) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateWisp(p.x, p.y));
                } else if (i == 7) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generatePoisonWisp(p.x, p.y));
                } else if (i == 8) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateBigWisp(p.x, p.y));
                } else if (i == 9) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipRed(p.x, p.y));
                } else if (i == 10) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipBlue(p.x, p.y));
                }
            }
        };

        return spawn;
    }

    public static GameEntity generateRandomUFOSpawnPoint(float x, float y, float freq, boolean includeGhost, Engine engine) {
        GameEntity spawn = new GameEntity("spawn");

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
                    int i = (int) (Math.random() * 7);

                    if (i == 0) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateEnemy(p.x, p.y));
                    } else if (i == 1) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateBlueEnemy(p.x, p.y));
                    } else if (i == 2) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateGreenEnemy(p.x, p.y));
                    } else if (i == 4) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generatePinkEnemy(p.x, p.y));
                    } else if (i == 5) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateRedEnemy(p.x, p.y));
                    } else if (i == 6) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateGhostUFO(p.x, p.y));
                    }
                }
            };
        } else {
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
                    } else if (i == 4) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generatePinkEnemy(p.x, p.y));
                    } else if (i == 5) {
                        PositionComponent p = pm.get(e);
                        engine.addEntity(generateRedEnemy(p.x, p.y));
                    }
                }
            };
        }

        return spawn;
    }

    public static GameEntity generateRandomWispSpawnPoint(float x, float y, float freq, boolean includeBigWisp, Engine engine) {
        GameEntity spawn = new GameEntity("spawn");

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
        GameEntity spawn = new GameEntity("spawn");

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
                int i = (int) (Math.random() * 2);

                if (i == 0) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipRed(p.x, p.y));
                } else if (i == 1) {
                    PositionComponent p = pm.get(e);
                    engine.addEntity(generateEnemyShipBlue(p.x, p.y));
                }
            }
        };

        return spawn;
    }

    public static GameEntity generateItemSpawnPoint(float x, float y, String spawned, float freq, Engine engine) {
        GameEntity spawn = new GameEntity("spawn");

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
        }

        return spawn;
    }

    public static GameEntity generateRandomItemSpawnPoint(float x, float y, float freq, Engine engine) {
        GameEntity spawn = new GameEntity("spawn");

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
        GameEntity spawn = new GameEntity("spawn");

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
        GameEntity spawn = new GameEntity("spawn");

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
}
