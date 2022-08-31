package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.demo.world.item.Item;
import lombok.Getter;
import lombok.NonNull;

public class Chest extends MapObject {
   @Getter
   private final Item[] content = new Item[Enemy.MAX_LOOT];

   public Chest(@NonNull String id) {
      super(id);
   }

   @Override
   protected void interact() {
      guiManager.openChestWindow(this);
   }

   @Override
   protected boolean shouldGoFurther(MapObject object) {
      return guiManager.openedWindows() == 0;
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
