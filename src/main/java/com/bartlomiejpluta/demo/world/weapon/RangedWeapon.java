package com.bartlomiejpluta.demo.world.weapon;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.move.Movable;
import com.bartlomiejpluta.base.lib.animation.AnimationRunner;
import com.bartlomiejpluta.base.lib.animation.BulletAnimationRunner;
import com.bartlomiejpluta.base.lib.animation.SimpleAnimationRunner;
import com.bartlomiejpluta.base.util.random.DiceRoller;
import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.event.HitEvent;
import com.bartlomiejpluta.demo.world.item.BaseItem;
import lombok.Getter;
import lombok.NonNull;

public class RangedWeapon extends BaseItem implements Weapon {
   private final Context context;
   private final BulletAnimationRunner animation;
   private final String sound;
   private final AnimationRunner punchAnimation;
   private final String punchSound;
   private final AnimationRunner missAnimation;
   private final String missSound;

   @Getter
   private final String name;

   @Getter
   private final String type;

   @Getter
   private final DiceRoller dmgRoller;

   @Getter
   private final DiceRoller rangeRoller;

   @Getter
   private final int cooldown;

   public RangedWeapon(@NonNull String id) {
      this(DB.dao.ranged_weapon.find(id));
   }

   public RangedWeapon(@NonNull DB.model.RangedWeaponModel template) {
      super(template.getIcon());

      this.context = ContextHolder.INSTANCE.getContext();
      this.name = template.getName();
      this.type = template.getType();
      this.dmgRoller = DiceRoller.of(template.getDamage());
      this.rangeRoller = DiceRoller.of(template.getRange());
      this.cooldown = template.getCooldown();
      this.animation = new BulletAnimationRunner(A.animations.get(template.getAnimation()).uid).infinite().offset(0, -15).onHit(this::onHit).onMiss(this::onMiss).speed(7f).animationSpeed(4f).scale(0.6f);
      this.sound = A.sounds.get(template.getSound()).uid;
      this.punchAnimation = new SimpleAnimationRunner(A.animations.get(template.getPunchAnimation()).uid);
      this.punchSound = A.sounds.get(template.getPunchSound()).uid;
      this.missAnimation = new SimpleAnimationRunner(A.animations.get(template.getMissAnimation()).uid).scale(0.4f);
      this.missSound = A.sounds.get(template.getMissSound()).uid;
   }

   private void onHit(Movable attacker, Entity target) {
      if (target.isBlocking() && target instanceof Creature character) {
         var namedAttacker = (Creature) attacker;
         var damage = dmgRoller.roll();
         character.hit(namedAttacker, damage);
         punchAnimation.run(context, character.getLayer(), character);
         context.playSound(punchSound);
         context.fireEvent(new HitEvent(namedAttacker, character, damage));
      }
   }

   private void onMiss(Movable attacker, Animation animation) {
      missAnimation.run(context, ((Creature) attacker).getLayer(), animation.getPosition());
      context.playSound(missSound);
   }

   @Override
   public boolean attack(Creature attacker) {
      var ammunition = attacker.getAmmunition();

      if (ammunition == null || !ammunition.getAppliesTo().equals(type)) {
         return false;
      }

      var direction = attacker.getFaceDirection();
      context.playSound(sound);
      animation.range(rangeRoller.roll()).direction(direction).rotation(direction.xAngle - 180).run(context, attacker.getLayer(), attacker);
      return true;
   }

   @Override
   public void use(Creature creature) {
      creature.setWeapon(this);
   }

   @Override
   public String usageName() {
      return "Equip";
   }
}