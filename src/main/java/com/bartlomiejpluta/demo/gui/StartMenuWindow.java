package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.Inflatable;
import com.bartlomiejpluta.base.api.gui.Ref;
import lombok.Getter;

public class StartMenuWindow extends DecoratedWindow implements Inflatable {

   @Ref("new_game")
   @Getter
   private Button newGameBtn;

   @Ref("exit")
   @Getter
   private Button exitBtn;

   public StartMenuWindow(Context context, GUI gui) {
      super(context, gui);
   }

   @Override
   public void onInflate() {
      newGameBtn.focus();
   }
}