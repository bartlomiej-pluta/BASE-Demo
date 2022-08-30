package com.bartlomiejpluta.demo.world.item;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public abstract class StackableItem extends BaseItem implements ItemStack {
   @Getter
   @Setter
   protected int count;

   protected StackableItem(@NonNull String id, int count) {
      super(id);
      this.count = Math.max(0, count);
   }

   @Override
   public void decrease() {
      if (count > 0) {
         --count;
      }
   }

   @Override
   public void increase() {
      ++count;
   }

   @Override
   public void increase(int count) {
      this.count += count;
   }

   @Override
   public void decrease(int count) {
      this.count = Math.max(0, count);
   }
}
