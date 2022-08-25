package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.Ref;
import com.bartlomiejpluta.base.api.gui.WindowManager;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.lib.gui.VGridOptionChoice;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.runner.DemoRunner;

import java.util.Map;


public class EquipmentWindow extends DecoratedWindow {
   private final DemoRunner runner;
   private final Player player;

   @Ref("eq")
   private VGridOptionChoice eqGrid;

   public EquipmentWindow(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      this.runner = (DemoRunner) context.getGameRunner();
      this.player = runner.getPlayer();
   }

   @Override
   public void onOpen(WindowManager manager) {
      super.onOpen(manager);

      var i = 0;
      for(var child : eqGrid.getChildren()) {
         var slot = (ItemIconView) child;
         slot.setIcon(player.getEquipmentItem(i++));
      }
   }
}