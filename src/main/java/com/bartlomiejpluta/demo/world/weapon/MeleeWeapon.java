package com.bartlomiejpluta.demo.world.weapon;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.lib.animation.AnimationRunner;
import com.bartlomiejpluta.base.lib.animation.RandomAnimationsRunner;
import com.bartlomiejpluta.base.util.random.DiceRoller;
import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.event.HitEvent;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2i;

import java.util.Random;

public class MeleeWeapon implements Weapon {
   private final Random random = new Random();
   private final Context context;
   private final DiceRoller roller;
   private final AnimationRunner animation;
   private final String sound;
   @Getter
   private final Icon icon;
   @Getter
   private final String name;
   @Getter
   private final int cooldown;

   public MeleeWeapon(@NonNull String id) {
      this(DB.dao.melee_weapon.find(id));
   }

   public MeleeWeapon(@NonNull DB.model.MeleeWeaponModel template) {
      this.context = ContextHolder.INSTANCE.getContext();
      this.name = template.getName();
      this.roller = DiceRoller.of(template.getDamage());
      this.cooldown = template.getCooldown();
      this.animation = new RandomAnimationsRunner(2).nRange(0, 2f).nScale(0.2f, 0.15f).uAnimationSpeed(0.5f, 1f).nRotation(0, 10).offset(0, -10).uDelay(250, 500).with(A.animations.get(template.getAnimation()).uid);
      this.sound = A.sounds.get(template.getSound()).uid;
      var icons = template.getIcon().split(",");
      this.icon = context.createIcon(A.iconsets.get(icons[0]).uid, Integer.valueOf(icons[1]), Integer.valueOf(icons[2]));
   }

   @Override
   public boolean attack(Creature attacker) {
      var facingNeighbour = attacker.getCoordinates().add(attacker.getFaceDirection().vector, new Vector2i());
      for (var entity : attacker.getLayer().getEntities()) {
         if (entity.getCoordinates().equals(facingNeighbour) && entity.isBlocking() && entity instanceof Creature) {
            var character = (Creature) entity;
            var damage = roller.roll();
            character.hit(attacker, damage);
            animation.run(context, character.getLayer(), character);
            context.playSound(sound);
            context.fireEvent(new HitEvent(attacker, character, damage));
            return true;
         }
      }

      return false;
   }
}