package com.bartlomiejpluta.demo.map;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.lib.camera.CameraController;
import com.bartlomiejpluta.base.lib.camera.FollowingCameraController;
import com.bartlomiejpluta.base.util.world.CharacterSpawner;
import com.bartlomiejpluta.base.util.world.Warp;
import com.bartlomiejpluta.demo.entity.Enemy;
import com.bartlomiejpluta.demo.entity.MapObject;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.event.EnemyDiedEvent;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import lombok.NonNull;

public abstract class BaseMapHandler implements MapHandler {
   protected Screen screen;
   protected Context context;
   protected DemoRunner runner;
   protected Camera camera;
   protected GameMap map;
   protected Player player;
   protected ObjectLayer mainLayer;
   protected CameraController cameraController;

   @Override
   public void onCreate(Context context, GameMap map) {
      this.context = context;
      this.screen = context.getScreen();
      this.runner = (DemoRunner) context.getGameRunner();
      this.camera = context.getCamera();
      this.map = map;
      this.player = runner.getPlayer();
      this.cameraController = FollowingCameraController
              .on(screen, camera, map)
              .follow(player.getPosition());
   }

   @Override
   public void input(Input input) {
      if (context.isPaused()) {
         return;
      }

      if (runner.openedWindows() > 0) {
         return;
      }

      if (input.isKeyPressed(Key.KEY_SPACE)) {
         player.attack();
      }

      if (input.isKeyPressed(Key.KEY_ENTER)) {
         player.interact();
      }

      if (input.isKeyPressed(Key.KEY_LEFT_CONTROL)) {
         if (input.isKeyPressed(Key.KEY_DOWN)) {
            player.setFaceDirection(Direction.DOWN);
         } else if (input.isKeyPressed(Key.KEY_UP)) {
            player.setFaceDirection(Direction.UP);
         } else if (input.isKeyPressed(Key.KEY_LEFT)) {
            player.setFaceDirection(Direction.LEFT);
         } else if (input.isKeyPressed(Key.KEY_RIGHT)) {
            player.setFaceDirection(Direction.RIGHT);
         }
      } else {
         if (input.isKeyPressed(Key.KEY_DOWN)) {
            player.getLayer().pushMovement(player.prepareMovement(Direction.DOWN));
         } else if (input.isKeyPressed(Key.KEY_UP)) {
            player.getLayer().pushMovement(player.prepareMovement(Direction.UP));
         } else if (input.isKeyPressed(Key.KEY_LEFT)) {
            player.getLayer().pushMovement(player.prepareMovement(Direction.LEFT));
         } else if (input.isKeyPressed(Key.KEY_RIGHT)) {
            player.getLayer().pushMovement(player.prepareMovement(Direction.RIGHT));
         }
      }
   }

   @Override
   public void update(Context context, GameMap map, float dt) {
      cameraController.update();
   }

   public Enemy enemy(@NonNull String id) {
      return new Enemy(id);
   }

   public Enemy enemy(ObjectLayer layer, int x, int y, @NonNull String id) {
      var enemy = new Enemy(id);
      enemy.setCoordinates(x, y);
      layer.addEntity(enemy);
      return enemy;
   }

   public MapObject object(ObjectLayer layer, int x, int y, @NonNull String id) {
      var object = new MapObject(id);
      object.setCoordinates(x, y);
      layer.addEntity(object);
      return object;
   }

   public CharacterSpawner spawner(ObjectLayer layer, int x, int y) {
      var spawner = new CharacterSpawner().trackEntities(EnemyDiedEvent.TYPE);
      spawner.setCoordinates(x, y);
      layer.addEntity(spawner);
      return spawner;
   }

   public Icon icon(ObjectLayer layer, int x, int y, String iconSetUid, int row, int column) {
      var icon = context.createIcon(iconSetUid, row, column);
      icon.setScale(1f);
      icon.setZIndex(-1);
      icon.setCoordinates(x, y);
      layer.addEntity(icon);
      return icon;
   }

   public Warp warp(ObjectLayer layer, int x, int y, String targetMap, String targetLayer, int targetX, int targetY) {
      var warp = new Warp(A.maps.get(targetMap).uid, A.maps.getLayer(targetMap, targetLayer), targetX, targetY);
      warp.setEntity(player);
      warp.setCoordinates(x, y);
      layer.addEntity(warp);
      return warp;
   }
}