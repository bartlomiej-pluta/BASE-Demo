package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.demo.world.item.Item;
import lombok.NonNull;
import org.joml.Vector2i;

public class Player extends Creature {
   private final Item[] equipment = new Item[4 * 4];

   public Player(@NonNull Character entity) {
      super(entity);
      this.hp = 500;
      this.maxHp = 500;
   }

   public void interact() {
      var coords = getCoordinates().add(getFaceDirection().vector, new Vector2i());
      var entities = getLayer().getEntities();
      for (var i=0; i< entities.size(); ++i) {
         var entity = entities.get(i);

         if (entity.getCoordinates().equals(coords) && entity instanceof MapObject object) {
            object.interact(this);
            return;
         }

         if (entity.getCoordinates().equals(getCoordinates()) && entity instanceof Item item) {
            pushItemToEquipment(item);
            getLayer().removeEntity(item);
         }
      }
   }

   private void pushItemToEquipment(@NonNull Item item) {
      for(int i=0; i<equipment.length; ++i) {
         if(equipment[i] == null) {
            equipment[i] = item;
            return;
         }
      }
   }

   public Item getEquipmentItem(int index) {
      return equipment[index];
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
}