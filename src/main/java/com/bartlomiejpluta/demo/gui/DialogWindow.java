package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.input.KeyAction;
import com.bartlomiejpluta.base.api.input.KeyEvent;
import com.bartlomiejpluta.base.lib.gui.TextView;

import java.util.Map;

public class DialogWindow extends DecoratedWindow {

   @Ref("message")
   private TextView text;

   public DialogWindow(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      addEventListener(KeyEvent.TYPE, this::handleKey);
   }

   private void handleKey(KeyEvent event) {
      if (event.getKey() == Key.KEY_ESCAPE) {
         event.consume();
         return;
      }

      if (event.getKey() == Key.KEY_ENTER && event.getAction() == KeyAction.PRESS) {
         manager.close();
         event.consume();
      }
   }

   @Override
   public void onOpen(WindowManager manager, Object[] args) {
      super.onOpen(manager, args);

      text.setText((String) args[0]);

      if (args.length > 1) {
         setWindowPosition((WindowPosition) args[1]);
      }
   }

   @Override
   public void onClose(WindowManager manager) {
      super.onClose(manager);

      text.setText("");
   }
}
