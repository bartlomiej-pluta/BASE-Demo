package com.bartlomiejpluta.demo.entity;

import A.maps;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import lombok.NonNull;

public class Door extends MapObject {
   private final String mapUid;
   private final int targetX;
   private final int targetY;
   private final int layerId;
   private final Player player;

   public Door(@NonNull String mapName, @NonNull String layerName, int targetX, int targetY, @NonNull String id) {
      super(id);
      this.mapUid = maps.get(mapName).uid;
      this.targetX = targetX;
      this.targetY = targetY;
      this.layerId = maps.getLayer(mapName, layerName);
      player = DemoRunner.instance().getPlayer();
      setPositionOffset(0, 16);
   }

   @Override
   protected void interact() {
      context.openMap(mapUid);
      context.getMap().getObjectLayer(layerId).addEntity(player);
      player.setCoordinates(targetX, targetY);
   }

   @Override
   protected boolean shouldGoFurther(MapObject object) {
      return true;
   }
}
