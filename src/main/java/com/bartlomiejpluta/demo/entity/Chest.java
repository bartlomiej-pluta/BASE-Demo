package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.world.item.Item;
import lombok.Getter;
import lombok.NonNull;

public class Chest extends MapObject {
   private final DemoRunner runner;

   @Getter
   private final Item[] content = new Item[Enemy.MAX_LOOT];

   public Chest(@NonNull String id) {
      super(id);
      runner = ((DemoRunner) ContextHolder.INSTANCE.getContext().getGameRunner());
   }

   @Override
   protected void interact() {
      runner.openChestWindow(this);
   }

   @Override
   protected boolean shouldGoFurther(MapObject object) {
      return runner.openedWindows() == 0;
   }

   public Chest addItem(Item item) {
      for (int i = 0; i < content.length; ++i) {
         if (content[i] == null) {
            content[i] = item;
            return this;
         }
      }

      throw new IllegalStateException("Chest is full!");
   }
}
