package com.bartlomiejpluta.demo.gui;

import A.fonts;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.input.KeyAction;
import com.bartlomiejpluta.base.api.input.KeyEvent;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.lib.gui.IconView;
import com.bartlomiejpluta.demo.util.IconUtil;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.item.ItemStack;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;
import java.util.function.BiConsumer;

public class ItemIconView extends IconView {
   private final GUI gui;
   private final Color normal;
   private final Color hover;
   private final Color textColor;

   @Getter
   private Item item;

   private IconSet placeholderIconSet;
   private Paint placeholderIconPaint;

   private int placeholderIconSetRow;
   private int placeholderIconSetColumn;

   @Setter
   private BiConsumer<ItemIconView, Item> action;

   public ItemIconView(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      this.gui = gui;
      this.normal = gui.createColor();
      this.hover = gui.createColor();
      this.textColor = gui.createColor();

      normal.setRGBA(0x444444FF);
      hover.setRGBA(0x888888FF);
      textColor.setRGBA(0xFFFFFFFF);

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

   @Attribute(value = "placeholder", separator = ",")
   public void setPlaceholderIcon(String icon, int row, int column) {
      this.placeholderIconPaint = gui.createPaint();
      this.placeholderIconSet = gui.getIconSet(A.iconsets.get(icon).uid);
      this.placeholderIconSetRow = row;
      this.placeholderIconSetColumn = column;
   }

   private void handleKeyEvent(KeyEvent event) {
      if (event.getKey() == Key.KEY_ENTER && event.getAction() == KeyAction.PRESS && item != null && action != null) {
         event.consume();
         action.accept(this, item);
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

      if (item == null && placeholderIconSet != null) {
         gui.icon(this.x, this.y, 2, 2, 0, 0.2f, placeholderIconSet, placeholderIconSetRow, placeholderIconSetColumn, placeholderIconPaint);

         return;
      }

      super.draw(screen, gui);

      if (item != null && item instanceof ItemStack stack) {
         gui.beginPath();
         gui.setFontFace(fonts.roboto_regular.uid);
         gui.setFontSize(17);
         gui.putText(x + 15, y + 5, String.valueOf(stack.getCount()));
         gui.setFillColor(textColor);
         gui.fill();
         gui.closePath();
      }
   }
}
