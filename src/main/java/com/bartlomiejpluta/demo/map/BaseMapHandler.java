package com.bartlomiejpluta.demo.map;

import A.maps;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.gui.Window;
import com.bartlomiejpluta.base.api.gui.WindowPosition;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.map.layer.object.MapPin;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.lib.camera.CameraController;
import com.bartlomiejpluta.base.lib.camera.FollowingCameraController;
import com.bartlomiejpluta.base.util.input.InputUtil;
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
      this.runner = DemoRunner.instance();
      this.guiManager = runner.getGuiManager();
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

      if (guiManager.openedWindows() > 0) {
         return;
      }

      if (input.isKeyPressed(Key.KEY_SPACE)) {
         player.attack();
      }

      if (input.isKeyPressed(Key.KEY_ENTER)) {
         player.interact();
      }

      InputUtil.handleBasicControl(player, input);
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

   protected <T extends Entity> T addEntity(T entity, MapPin tile) {
      entity.setCoordinates(tile.getX(), tile.getY());
      map.getObjectLayer(tile.getLayer()).addEntity(entity);
      return entity;
   }

   public Enemy enemy(@NonNull MapPin tile, @NonNull String id) {
      return addEntity(new Enemy(id), tile);
   }

   public Chest chest(@NonNull MapPin tile, @NonNull String id) {
      return addEntity(new Chest(id), tile);
   }

   public Door door(@NonNull MapPin tile, @NonNull MapPin target, @NonNull String id) {
      return addEntity(new Door(target, id), tile);
   }

   public CharacterSpawner spawner(@NonNull MapPin tile) {
      return addEntity(new CharacterSpawner().trackEntities(EnemyDiedEvent.TYPE), tile);
   }

   public Medicament medicament(@NonNull MapPin tile, @NonNull String id, int count) {
      return addEntity(new Medicament(id, count), tile);
   }

   public Icon icon(@NonNull MapPin tile, String iconSetUid, int row, int column) {
      var icon = context.createIcon(iconSetUid, row, column);
      icon.setScale(1f);
      icon.setZIndex(-1);
      return addEntity(icon, tile);
   }

   public Warp warp(@NonNull MapPin tile, MapPin target) {
      var warp = new Warp(target);
      warp.setEntity(player);
      return addEntity(warp, tile);
   }
}