package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.weapon.Weapon;
import lombok.NonNull;
import org.joml.Vector2i;

public class Player extends Creature {
   private final Item[] equipment = new Item[4 * 4];
   private int pickingItemToEquipmentCooldown = 0;

   public Player(@NonNull Character entity) {
      super(entity);
      this.hp = 500;
      this.maxHp = 500;
   }

   public void interact() {
      var coords = getCoordinates().add(getFaceDirection().vector, new Vector2i());
      var entities = getLayer().getEntities();
      for (var i = 0; i < entities.size(); ++i) {
         var entity = entities.get(i);

         if (entity.getCoordinates().equals(coords) && entity instanceof MapObject object) {
            object.interact(this);
            return;
         }

         if (pickingItemToEquipmentCooldown == 0 && entity.getCoordinates().equals(getCoordinates()) && entity instanceof Item item) {
            pushItemToEquipment(item);
            getLayer().removeEntity(item);
            pickingItemToEquipmentCooldown = PICKING_ITEM_TO_EQUIPMENT_COOLDOWN;
            return;
         }
      }
   }

   private void pushItemToEquipment(@NonNull Item item) {
      for (int i = 0; i < equipment.length; ++i) {
         if (equipment[i] == null) {
            equipment[i] = item;
            return;
         }
      }
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

      if(item == getWeapon()) {
         setWeapon(null);
      }
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

      if (pickingItemToEquipmentCooldown > 0) {
         pickingItemToEquipmentCooldown = (int) Math.max(0, pickingItemToEquipmentCooldown - dt * 1000);
      }
   }

   private static final int PICKING_ITEM_TO_EQUIPMENT_COOLDOWN = 300;
}