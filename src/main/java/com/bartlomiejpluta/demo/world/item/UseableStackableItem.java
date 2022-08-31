package com.bartlomiejpluta.demo.world.item;

import com.bartlomiejpluta.demo.entity.Creature;
import lombok.NonNull;

public abstract class UseableStackableItem extends StackableItem implements Useable {

   protected UseableStackableItem(@NonNull String id, int count) {
      super(id, count);
   }

   @Override
   public void use(Creature creature) {
      if (--count == 0) {
         creature.removeItemFromEquipment(this);
      }
   }
}
