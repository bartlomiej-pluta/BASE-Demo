package com.bartlomiejpluta.demo.gui;

import A.fonts;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.TextView;
import com.bartlomiejpluta.base.lib.gui.VOptionChoice;

import java.util.Collections;
import java.util.Map;

public class DialogChoiceWindow extends DecoratedWindow {

   @Ref("speaker")
   private TextView speaker;

   @Ref("choice")
   private VOptionChoice choice;

   @SuppressWarnings("unchecked")
   @Override
   public void onOpen(WindowManager manager, Object[] args) {
      super.onOpen(manager, args);

      speaker.setText((String) args[0]);
      speaker.setColor((int) args[1]);

      var index = 0;
      choice.removeAllChildren();
      var options = (String[]) args[2];
      for(var option : options) {
         var button = new Button(context, gui, Collections.emptyMap());
         choice.add(button);
         button.setText(option);
         button.setColor(0xFFFFFF);
         button.setWidthMode(SizeMode.RELATIVE);
         button.setWidth(1f);
         button.setFont(fonts.roboto_regular.$);
         button.setFontSize(20f);
         button.setAction(onChoose(index));
         ++index;
      }

      choice.focus();
   }

   private Runnable onChoose(int index) {
      return () -> resolve(index);
   }

   public DialogChoiceWindow(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
   }
}
