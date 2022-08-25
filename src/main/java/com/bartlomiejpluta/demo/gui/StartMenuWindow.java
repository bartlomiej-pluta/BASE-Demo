package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.Inflatable;
import com.bartlomiejpluta.base.api.gui.Ref;
import com.bartlomiejpluta.base.lib.gui.VOptionChoice;
import lombok.Getter;

import java.util.Map;

public class StartMenuWindow extends DecoratedWindow implements Inflatable {

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
   public void onInflate() {
      menu.select(0);
      menu.focus();
   }
}