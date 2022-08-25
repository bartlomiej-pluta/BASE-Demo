package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.character.Character;
import lombok.NonNull;
import org.joml.Vector2i;

public class Player extends Creature {

   public Player(@NonNull Character entity) {
      super(entity);
      this.hp = 500;
      this.maxHp = 500;
   }

   public void interact() {
      var coords = getCoordinates().add(getFaceDirection().vector, new Vector2i());
      for (var entity : getLayer().getEntities()) {
         if (entity.getCoordinates().equals(coords) && entity instanceof MapObject) {
            ((MapObject) entity).interact(this);
         }
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
}