package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Color;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.lib.gui.BaseComponent;
import lombok.Setter;

import java.util.Map;

public class Bar extends BaseComponent {

   private final Color stroke;
   private final Color fill;
   @Setter
   private float value = 1.0f;
   private float actualValue = 1.0f;
   private final float speed = 0.05f;

   public Bar(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);

      this.stroke = gui.createColor();
      this.fill = gui.createColor();

      stroke.setAlpha(1f);
      fill.setAlpha(1f);
   }

   public void setStrokeColor(Integer hex) {
      stroke.setRGB(hex);
   }

   public void setFillColor(Integer hex) {
      fill.setRGB(hex);
   }

   @Override
   public float getContentWidth() {
      return width;
   }

   @Override
   public float getContentHeight() {
      return height;
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var remainingDistance = value - actualValue;
      actualValue += remainingDistance * speed;

      gui.beginPath();
      gui.drawRectangle(x, y, Math.max(width * actualValue, 0), height);
      gui.setFillColor(fill);
      gui.fill();
      gui.closePath();
      gui.beginPath();
      gui.drawRectangle(x, y, width, height);
      gui.setStrokeColor(stroke);
      gui.stroke();
      gui.closePath();
   }
}