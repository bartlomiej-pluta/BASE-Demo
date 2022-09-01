package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.Ref;
import com.bartlomiejpluta.base.api.gui.WindowManager;
import com.bartlomiejpluta.base.lib.gui.VOptionChoice;

import java.util.Map;


public class GameMenuWindow extends DecoratedWindow {

   @Ref("menu")
   private VOptionChoice menu;

   public GameMenuWindow(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
   }

   @Override
   public void onOpen(WindowManager manager, Object[] args) {
      super.onOpen(manager, args);
      menu.select(0);
      menu.focus();
   }
}