package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.weapon.Weapon;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Creature extends NamedCharacter {
   private static final Logger log = LoggerFactory.getLogger(Creature.class);

   protected int attackCooldown = 0;

   @Getter
   protected boolean alive = true;

   @Getter
   protected boolean immortal = false;

   @Getter
   protected int maxHp;

   @Getter
   protected int hp;

   @Getter
   @Setter
   private Weapon weapon;

   @Getter
   private NamedCharacter lastAttacker;

   public Creature(@NonNull Character entity) {
      super(entity);
   }

   public void attack() {
      if (weapon == null) {
         return;
      }

      if (attackCooldown >= weapon.getCooldown()) {
         if (weapon.attack(this)) {
            attackCooldown = 0;
         }
      }
   }

   public void hit(NamedCharacter source, int dmg) {
      this.lastAttacker = source;

      if (immortal) {
         return;
      }

      hp -= dmg;
   }

   public void useEquipmentItem(Item item) {
      if (item instanceof Weapon weapon) {
         setWeapon(weapon);
      }
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      if (weapon != null && attackCooldown < weapon.getCooldown()) {
         attackCooldown += (int) (dt * 1000f);
      }

      if (hp <= 0 && alive && getLayer() != null) {
         alive = false;
         die();
      }
   }

   protected void die() {
      if (isMoving()) {
         abortMove();
      }
   }

   public abstract String getName();
}