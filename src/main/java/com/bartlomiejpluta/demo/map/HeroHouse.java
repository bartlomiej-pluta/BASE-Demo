package com.bartlomiejpluta.demo.map;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.model.GameMap;

public class HeroHouse extends BaseMapHandler {
   @Override
   public void onCreate(Context context, GameMap map) {
      super.onCreate(context, map);
      dayNightCycle = true;
   }
}