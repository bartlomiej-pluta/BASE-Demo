package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.demo.world.item.Item;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Chest extends MapObject {
   @Getter
   private final Item[] content = new Item[Enemy.MAX_LOOT];

   public Chest(@NonNull String id) {
      super(id);
   }

   @Override
   protected CompletableFuture<?> interact() {
      return runner.getGuiManager().openChestWindow(this);
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

   public Chest addItem(Item item, int slot) {
      if(slot >= content.length) {
         throw new IllegalStateException("The [" + slot + "] slot exceeds the chest size (" + content.length + ")!");
      }

      if (content[slot] != null) {
         throw new IllegalStateException("The [" + slot + "] slot is already filled!");
      }

      content[slot] = item;

      return this;
   }

   public Chest shuffle() {
      var random = new Random();
      for(int i = content.length - 1; i > 0; --i) {
         var index = random.nextInt(i + 1);
         var tmp = content[index];
         content[index] = content[i];
         content[i] = tmp;
      }

      return this;
   }
}
