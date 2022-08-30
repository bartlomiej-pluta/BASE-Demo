package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.weapon.Ammunition;
import com.bartlomiejpluta.demo.world.weapon.Weapon;
import lombok.NonNull;
import org.joml.Vector2i;

public class Player extends Creature {
   private final Item[] equipment = new Item[4 * 4];
   private int interactionCooldown = 0;

   public Player(@NonNull Character entity) {
      super(entity);
      this.hp = 500;
      this.maxHp = 500;
   }

   public void interact() {
      if (interactionCooldown > 0) {
         return;
      }

      var coords = getCoordinates().add(getFaceDirection().vector, new Vector2i());
      var entities = getLayer().getEntities();
      for (var i = 0; i < entities.size(); ++i) {
         var entity = entities.get(i);

         // Use some map object which player is looking at
         if (entity.getCoordinates().equals(coords) && entity instanceof MapObject object) {
            object.triggerInteraction();
            return;
         }

         if (entity.getCoordinates().equals(getCoordinates())) {

            // Pick some item from the ground
            if (entity instanceof Item item) {
               pushItemToEquipment(item);
               getLayer().removeEntity(item);
               interactionCooldown = INTERACTION_COOLDOWN;
               return;
            }

            // Search the enemy corpse
            if (entity instanceof Enemy enemy && !enemy.isAlive()) {
               runner.openLootWindow(enemy);
               interactionCooldown = INTERACTION_COOLDOWN;
               return;
            }
         }
      }
   }

   public boolean pushItemToEquipment(@NonNull Item item) {
      for (int i = 0; i < equipment.length; ++i) {
         if (equipment[i] == null) {
            equipment[i] = item;
            return true;
         }
      }

      return false;
   }

   public Item getEquipmentItem(int index) {
      return equipment[index];
   }

   public void removeItemFromEquipment(@NonNull Item item) {
      for (int i = 0; i < equipment.length; ++i) {
         if (equipment[i] == item) {
            equipment[i] = null;
            return;
         }
      }
   }

   public void dropItemFromEquipment(@NonNull Item item) {
      removeItemFromEquipment(item);
      item.setCoordinates(getCoordinates());
      getLayer().addEntity(item);

      if (item == getWeapon()) {
         setWeapon(null);
      }

      if (item == getAmmunition()) {
         setAmmunition(null);
      }
   }

   @Override
   public void setWeapon(Weapon weapon) {
      var currentWeapon = getWeapon();

      if (weapon != null) {
         removeItemFromEquipment(weapon);
      }

      if (currentWeapon != null) {
         pushItemToEquipment(currentWeapon);
      }

      super.setWeapon(weapon);
   }

   @Override
   public void setAmmunition(Ammunition ammunition) {
      var currentAmmo = getAmmunition();

      if (currentAmmo != null && currentAmmo.getId().equals(ammunition.getId())) {
         currentAmmo.increase(ammunition.getCount());
         removeItemFromEquipment(ammunition);
         return;
      }

      if (ammunition != null) {
         removeItemFromEquipment(ammunition);
      }

      if (currentAmmo != null) {
         pushItemToEquipment(currentAmmo);
      }

      super.setAmmunition(ammunition);
   }

   @Override
   public void die() {
      super.die();
      runner.returnToStartMenu();
   }

   @Override
   public String getName() {
      return "Player";
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      if (interactionCooldown > 0) {
         interactionCooldown = (int) Math.max(0, interactionCooldown - dt * 1000);
      }
   }

   private static final int INTERACTION_COOLDOWN = 300;
}