package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Color;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.input.KeyAction;
import com.bartlomiejpluta.base.api.input.KeyEvent;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.lib.gui.IconView;
import com.bartlomiejpluta.demo.world.item.Item;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;
import java.util.function.Consumer;

import java.util.Map;

public class ItemIconView extends IconView {
   private final Color normal;
   private final Color hover;

   @Getter
   private Item item;

   @Setter
   private Consumer<Item> action;

   public ItemIconView(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      this.normal = gui.createColor();
      this.hover = gui.createColor();

      normal.setRGBA(0x444444FF);
      hover.setRGBA(0x888888FF);
      super.setScale(2f);

      addEventListener(KeyEvent.TYPE, this::handleKeyEvent);
   }

   public void setItem(Item item) {
      this.item = item;
      super.setIcon(item);
   }

   @Override
   public void setIcon(Icon icon) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setIconSet(String iconSetUid) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setIconSetRow(@NonNull Integer iconSetRow) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setIconSetColumn(@NonNull Integer iconSetColumn) {
      throw new UnsupportedOperationException();
   }

   private void handleKeyEvent(KeyEvent event) {
      if (event.getKey() == Key.KEY_ENTER && event.getAction() == KeyAction.PRESS && item != null && action != null) {
         event.consume();
         action.accept(item);
      }
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
