package com.bartlomiejpluta.demo.world.time;

import DB.dao;
import com.bartlomiejpluta.base.api.context.Context;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class WorldTime {

   @Getter
   private float progress = 0;
   private final float period; // seconds
   private final Context context;

   public WorldTime(@NonNull Context context) {
      this.context = context;
      this.period = Float.parseFloat(dao.config.find("full_day_duration").getValue());
   }

   public int getHour() {
      return (int) (progress * 24);
   }

   public int getMinute() {
      return (int) (progress * 24 * 60) % 60;
   }

   public void update(float dt) {
      if (context.isPaused()) {
         return;
      }

      progress += dt / period;
      if (progress > 1) {
         progress = 0;
      }
   }
}
