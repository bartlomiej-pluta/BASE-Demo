package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.VOptionChoice;
import lombok.Getter;

import java.util.Map;

public class StartMenuWindow extends DecoratedWindow {

   @Ref("new_game")
   @Getter
   private Button newGameBtn;

   @Ref("exit")
   @Getter
   private Button exitBtn;

   @Ref("menu")
   private VOptionChoice menu;

   public StartMenuWindow(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
   }

   @Override
   public void onOpen(WindowManager manager, Object[] args) {
      super.onOpen(manager, args);
      menu.select(0);
      menu.focus();
   }
}