package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.Inflatable;
import com.bartlomiejpluta.base.api.gui.Ref;
import lombok.Getter;

import java.util.Map;


public class GameMenuWindow extends DecoratedWindow implements Inflatable {

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
   public void onInflate() {
      resumeGameBtn.focus();
   }
}