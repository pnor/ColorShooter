package com.colorshooter.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.colorshooter.game.components.*;
import com.colorshooter.game.systems.DamageSystem;

/**
 * Created by pnore_000 on 7/1/2016.
 */
public class Mappers {
    //Position
    public static ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    //Movement
    public static ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);
    //Image
    public static ComponentMapper<ImageComponent> im = ComponentMapper.getFor(ImageComponent.class);
    //Animation
    public static ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    //Collision
    public static ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);
    //Bouncing
    public static ComponentMapper<BouncingComponent> bm = ComponentMapper.getFor(BouncingComponent.class);
    //Poison
    public static ComponentMapper<PoisonComponent> poim = ComponentMapper.getFor(PoisonComponent.class);
    //Frozen
    public static ComponentMapper<FrozenComponent> fm = ComponentMapper.getFor(FrozenComponent.class);
    //Health
    public static ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    //Damage
    public static ComponentMapper<DamageComponent> dm = ComponentMapper.getFor(DamageComponent.class);
    //Shooting
    public static ComponentMapper<ShootComponent> shm = ComponentMapper.getFor(ShootComponent.class);
    //Player Input
    public static ComponentMapper<PlayerInputComponent> pim = ComponentMapper.getFor(PlayerInputComponent.class);
    //AI
    public static ComponentMapper<AIComponent> aim = ComponentMapper.getFor(AIComponent.class);
    //Lifetime
    public static ComponentMapper<LifetimeComponent> lfm = ComponentMapper.getFor(LifetimeComponent.class);
    //Time-Based Events
    public static ComponentMapper<EventComponent> em = ComponentMapper.getFor(EventComponent.class);
    //Items
    public static ComponentMapper<ItemComponent> itm = ComponentMapper.getFor(ItemComponent.class);
    //Color
    public static ComponentMapper<ColorComponent> colm = ComponentMapper.getFor(ColorComponent.class);
    //Points
    public static ComponentMapper<PointsComponent> pom = ComponentMapper.getFor(PointsComponent.class);
}
