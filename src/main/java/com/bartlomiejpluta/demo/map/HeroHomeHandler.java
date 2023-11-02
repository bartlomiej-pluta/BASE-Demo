package com.bartlomiejpluta.demo.map;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.model.GameMap;

public class HeroHomeHandler extends BaseMapHandler {
   @Override
   public void onOpen(Context context, GameMap map) {
      dialog(player, "Ahhh, another beautiful day for an adventure... Let's go!");
   }
}