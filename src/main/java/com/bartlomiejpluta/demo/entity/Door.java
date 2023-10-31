package com.bartlomiejpluta.demo.entity;

import A.maps;
import com.bartlomiejpluta.base.api.map.layer.object.MapPin;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;

public class Door extends MapObject {
   private final String mapUid;
   private final int targetX;
   private final int targetY;
   private final int layerId;
   private final Player player;

   public Door(@NonNull MapPin label, @NonNull String id) {
      super(id);
      this.mapUid = label.getMap();
      this.layerId = label.getLayer();
      this.targetX = label.getX();
      this.targetY = label.getY();
      player = DemoRunner.instance().getPlayer();
      setPositionOffset(0, 16);
   }

   public Door(@NonNull String mapName, @NonNull String layerName, int targetX, int targetY, @NonNull String id) {
      super(id);
      var map = maps.byName(mapName);
      this.mapUid = map.$;
      this.targetX = targetX;
      this.targetY = targetY;
      this.layerId = map.layer(layerName).$;
      player = DemoRunner.instance().getPlayer();
      setPositionOffset(0, 16);
   }

   @Override
   protected CompletableFuture<?> interact() {
      context.openMap(mapUid);
      context.getMap().getObjectLayer(layerId).addEntity(player);
      player.setCoordinates(targetX, targetY);

      reset();

      return CompletableFuture.completedFuture(null);
   }
}
