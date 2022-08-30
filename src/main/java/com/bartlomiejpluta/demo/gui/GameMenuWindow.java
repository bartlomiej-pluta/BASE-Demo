package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.VOptionChoice;
import lombok.Getter;

import java.util.Map;


public class GameMenuWindow extends DecoratedWindow {

   @Ref("menu")
   private VOptionChoice menu;

   @Ref("resume_game")
   @Getter
   private Button resumeGameBtn;

   @Ref("start_menu")
   @Getter
   private Button startMenuBtn;

   @Ref("exit")
   @Getter
   private Button exitBtn;

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