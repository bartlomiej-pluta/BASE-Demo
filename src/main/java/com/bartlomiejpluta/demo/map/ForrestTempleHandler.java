package com.bartlomiejpluta.demo.map;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.model.GameMap;

public class ForrestTempleHandler extends BaseMapHandler {
   public static final String UID = "f845355e-b9ad-4884-a217-dd3a4c18a3fa";
   public static final A.maps.GameMapAsset_Layers_ForrestTemple LAYERS = A.maps.forrest_temple.layers;

   public static final int MAIN_LAYER = 4;

   @Override
   public void onCreate(Context context, GameMap map) {
      super.onCreate(context, map);
      this.mainLayer = map.getObjectLayer(MAIN_LAYER);
   }
}