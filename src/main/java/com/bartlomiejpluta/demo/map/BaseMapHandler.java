package com.bartlomiejpluta.demo.map;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Window;
import com.bartlomiejpluta.base.api.gui.WindowPosition;
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
import com.bartlomiejpluta.demo.entity.Chest;
import com.bartlomiejpluta.demo.entity.Door;
import com.bartlomiejpluta.demo.entity.Enemy;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.event.EnemyDiedEvent;
import com.bartlomiejpluta.demo.menu.GuiManager;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.world.potion.Medicament;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;

public abstract class BaseMapHandler implements MapHandler {
   protected Screen screen;
   protected Context context;
   protected DemoRunner runner;
   protected GuiManager guiManager;
   protected Camera camera;
   protected GameMap map;
   protected Player player;
   protected CameraController cameraController;

   @Override
   public void onCreate(Context context, GameMap map) {
      this.context = context;
      this.screen = context.getScreen();
      this.runner = (DemoRunner) context.getGameRunner();
      this.guiManager = context.getGlobal("gui", GuiManager.class);
      this.camera = context.getCamera();
      this.map = map;
      this.player = context.getGlobal("player", Player.class);
      this.cameraController = FollowingCameraController
              .on(screen, camera, map)
              .follow(player.getPosition());
   }

   @Override
   public void input(Input input) {
      if (context.isPaused()) {
         return;
      }

      if (guiManager.openedWindows() > 0) {
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

   public CompletableFuture<Window> dialog(String message, WindowPosition position) {
      return guiManager.showDialog(message, position);
   }

   public CompletableFuture<Window> dialog(String message) {
      return guiManager.showDialog(message, WindowPosition.BOTTOM);
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

   public Chest chest(ObjectLayer layer, int x, int y, @NonNull String id) {
      var chest = new Chest(id);
      chest.setCoordinates(x, y);
      layer.addEntity(chest);
      return chest;
   }

   public Door door(ObjectLayer layer, int x, int y, @NonNull String mapName, @NonNull String layerName, int targetX, int targetY, @NonNull String id) {
      var door = new Door(mapName, layerName, targetX, targetY, id);
      door.setCoordinates(x, y);
      layer.addEntity(door);
      return door;
   }

   public CharacterSpawner spawner(ObjectLayer layer, int x, int y) {
      var spawner = new CharacterSpawner().trackEntities(EnemyDiedEvent.TYPE);
      spawner.setCoordinates(x, y);
      layer.addEntity(spawner);
      return spawner;
   }

   public Medicament medicament(@NonNull String id, int count) {
      return new Medicament(id, count);
   }

   public Medicament medicament(ObjectLayer layer, int x, int y, @NonNull String id, int count) {
      var medicament = new Medicament(id, count);
      medicament.setCoordinates(x, y);
      layer.addEntity(medicament);
      return medicament;
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