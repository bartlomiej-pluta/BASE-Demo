package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.Label;
import com.bartlomiejpluta.base.lib.gui.VGridOptionChoice;
import com.bartlomiejpluta.demo.entity.Enemy;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.world.item.Item;

import java.util.Map;

public class LootWindow extends DecoratedWindow implements Inflatable {
   private final Player player;

   @Ref("title")
   private Label titleLbl;

   @Ref("loot")
   private VGridOptionChoice lootMenu;

   private Item[] loot;

   private ItemIconView[] slots = new ItemIconView[Enemy.MAX_LOOT];

   public LootWindow(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      this.player = context.getGlobal("player", Player.class);
   }

   @Override
   public void onInflate() {
      for (int i = 0; i < Enemy.MAX_LOOT; ++i) {
         var itemView = new ItemIconView(context, gui, Map.of());
         itemView.setMargin(5f);
         lootMenu.add(itemView);
         slots[i] = itemView;
         itemView.setAction(this::handleClick);
      }
   }

   @Override
   public void onOpen(WindowManager manager, Object[] args) {
      super.onOpen(manager, args);

      this.loot = (Item[]) args[0];
      this.titleLbl.setText((String) args[1]);
      this.lootMenu.select(0, 0);
      this.lootMenu.focus();
      updateSlots();
   }

   @Override
   public void onClose(WindowManager manager) {
      super.onClose(manager);

      clearSlots();
      this.loot = null;
   }

   private void handleClick(ItemIconView slot, Item item) {
      if (item == null) {
         return;
      }

      if (player.pushItemToEquipment(item)) {
         for (int i = 0; i < Enemy.MAX_LOOT; i++) {
            if (loot[i] == item) {
               loot[i] = null;
               slot.setItem(null);
               break;
            }
         }
      }
   }

   private void updateSlots() {
      for (int i = 0; i < Enemy.MAX_LOOT; ++i) {
         slots[i].setItem(loot[i]);
      }
   }

   private void clearSlots() {
      for (var slot : slots) {
         slot.setItem(null);
      }
   }
}
