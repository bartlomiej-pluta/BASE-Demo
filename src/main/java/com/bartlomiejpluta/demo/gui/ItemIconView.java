package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Color;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.lib.gui.IconView;

import java.util.Map;

public class ItemIconView extends IconView {
   private final Color normal;
   private final Color hover;

   public ItemIconView(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      this.normal = gui.createColor();
      this.hover = gui.createColor();

      normal.setRGBA(0x444444FF);
      hover.setRGBA(0x888888FF);
      super.setScale(2f);
   }

   @Override
   protected float getContentWidth() {
      return 68f;
   }

   @Override
   protected float getContentHeight() {
      return 68f;
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      gui.beginPath();
      gui.drawRectangle(x, y, getWidth(), getHeight());
      gui.setFillColor(focused ? hover : normal);
      gui.fill();
      gui.closePath();

      super.draw(screen, gui);
   }
}
