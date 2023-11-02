package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.util.random.DiceRoller;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.item.ItemStack;
import com.bartlomiejpluta.demo.world.weapon.Ammunition;
import com.bartlomiejpluta.demo.world.weapon.ThrowingWeapon;
import com.bartlomiejpluta.demo.world.weapon.Weapon;
import lombok.NonNull;
import org.joml.Vector2i;

import java.util.Arrays;

public class Player extends Creature {
   public static final int EQUIPMENT_SIZE = 6 * 6;
   private final Item[] equipment = new Item[EQUIPMENT_SIZE];
   private int interactionCooldown = 0;

   public Player(@NonNull Character entity) {
      super(entity);
      reset();
   }

   public void reset() {
      var data = DB.dao.levels.find(1);
      this.maxHp = DiceRoller.roll(data.getMaxHp());
      this.hp = this.maxHp;

      alive = true;
      changeCharacterSet(A.charsets.luna.$);
      setScale(1f);
      setSpeed(4f);
      setAnimationSpeed(1f);
      setBlocking(true);

      Arrays.fill(equipment, null);
   }

   public void interact() {
      if (interactionCooldown > 0) {
         return;
      }

      var coords = getCoordinates().add(getFaceDirection().vector, new Vector2i());
      var entities = getLayer().getEntities();
      for (var i = 0; i < entities.size(); ++i) {
         var entity = entities.get(i);

         if (entity.getCoordinates().equals(coords)) {

            // Use some map object which player is looking at
            if(entity instanceof MapObject object) {
               object.triggerInteraction();
               return;
            }

            // Interact with friend creature which player is looking at
            if(entity instanceof Friend friend) {
               friend.interact(this).thenApply(o -> interactionCooldown = INTERACTION_COOLDOWN);
               return;
            }
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
               runner.getGuiManager().openLootWindow(enemy);
               interactionCooldown = INTERACTION_COOLDOWN;
               return;
            }
         }
      }
   }

   public boolean pushItemToEquipment(@NonNull Item item) {
      if (item instanceof Ammunition ammo) {
         if (getAmmunition() != null && ammo.getId().equals(getAmmunition().getId())) {
            getAmmunition().increase(ammo.getCount());
            return true;
         }
      }

      if (item instanceof ThrowingWeapon weapons) {
         if (getWeapon() instanceof ThrowingWeapon currentWeapons && currentWeapons.getId().equals(weapons.getId())) {
            currentWeapons.increase(weapons.getCount());
            return true;
         }
      }

      if (item instanceof ItemStack stack) {
         return pushItemStackToEquipment(stack);
      }

      return pushSingleItemToEquipment(item);
   }

   private boolean pushItemStackToEquipment(@NonNull ItemStack items) {
      var availableSlot = -1;
      for (int i = 0; i < equipment.length; ++i) {
         if (equipment[i] instanceof ItemStack stack && stack.getId().equals(items.getId())) {
            stack.increase(items.getCount());
            return true;
         }

         if (availableSlot == -1 && equipment[i] == null) {
            availableSlot = i;
         }
      }

      if (availableSlot > -1) {
         equipment[availableSlot] = items;
         return true;
      }

      return false;
   }

   private boolean pushSingleItemToEquipment(@NonNull Item item) {
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

   @Override
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
   public void setWeapon(Weapon newWeapon) {
      var currentWeapon = getWeapon();

      if (newWeapon == null) {
         disarmWeapon(currentWeapon);
         return;
      }

      if (currentWeapon instanceof ThrowingWeapon currentWeapons && newWeapon instanceof ThrowingWeapon weapons && currentWeapons.getId().equals(weapons.getId())) {
         updateStackableWeapon(currentWeapons, weapons);
         return;
      }

      changeWeaponForWeapon(currentWeapon, newWeapon);
   }

   private void disarmWeapon(Weapon currentWeapon) {
      if (!(currentWeapon instanceof ThrowingWeapon)) {
         pushItemToEquipment(currentWeapon);
      }

      super.setWeapon(null);
   }

   private void updateStackableWeapon(ThrowingWeapon currentWeapon, ThrowingWeapon newWeapon) {
      currentWeapon.increase(newWeapon.getCount());
      removeItemFromEquipment(newWeapon);
   }

   private void changeWeaponForWeapon(Weapon currentWeapon, Weapon newWeapon) {
      super.setWeapon(newWeapon);
      removeItemFromEquipment(newWeapon);

      if (currentWeapon != null) {
         pushItemToEquipment(currentWeapon);
      }
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
      return "Luna";
   }

   @Override
   public int getDialogNameColor() {
      return 0x00AA00;
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