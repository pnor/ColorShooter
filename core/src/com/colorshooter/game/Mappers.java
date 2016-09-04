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
    public final static ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    //Movement
    public final static ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);
    //Image
    public final static ComponentMapper<ImageComponent> im = ComponentMapper.getFor(ImageComponent.class);
    //Animation
    public final static ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    //Collision
    public final static ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);
    //Bouncing
    public final static ComponentMapper<BouncingComponent> bm = ComponentMapper.getFor(BouncingComponent.class);
    //Poison
    public final static ComponentMapper<PoisonComponent> poim = ComponentMapper.getFor(PoisonComponent.class);
    //Frozen
    public final static ComponentMapper<FrozenComponent> fm = ComponentMapper.getFor(FrozenComponent.class);
    //Health
    public final static ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    //Damage
    public final static ComponentMapper<DamageComponent> dm = ComponentMapper.getFor(DamageComponent.class);
    //Shooting
    public final static ComponentMapper<ShootComponent> shm = ComponentMapper.getFor(ShootComponent.class);
    //Player Input
    public final static ComponentMapper<PlayerInputComponent> pim = ComponentMapper.getFor(PlayerInputComponent.class);
    //AI
    public final static ComponentMapper<AIComponent> aim = ComponentMapper.getFor(AIComponent.class);
    //Lifetime
    public final static ComponentMapper<LifetimeComponent> lfm = ComponentMapper.getFor(LifetimeComponent.class);
    //Time-Based Events
    public final static ComponentMapper<EventComponent> em = ComponentMapper.getFor(EventComponent.class);
    //Items
    public final static ComponentMapper<ItemComponent> itm = ComponentMapper.getFor(ItemComponent.class);
    //ItemsReceivable
    public final static ComponentMapper<ItemReceivableComponent> irm = ComponentMapper.getFor(ItemReceivableComponent.class);
    //Color
    public final static ComponentMapper<ColorComponent> colm = ComponentMapper.getFor(ColorComponent.class);
    //Points
    public final static ComponentMapper<PointsComponent> pom = ComponentMapper.getFor(PointsComponent.class);
}
