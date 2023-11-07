package com.bartlomiejpluta.demo.runner;


import DB.dao;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.runner.GameRunner;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.menu.GuiManager;
import com.bartlomiejpluta.demo.world.time.WorldTime;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DemoRunner implements GameRunner {
   private static final Logger log = LoggerFactory.getLogger(DemoRunner.class);

   @Getter
   private WorldTime time;
   private static DemoRunner INSTANCE;

   private Screen screen;
   private Context context;
   private GUI hud;

   @Getter
   private GuiManager guiManager;

   @Getter
   private Player player;

   public static DemoRunner instance() {
      return INSTANCE;
   }

   private void configureScreen() {
      var resolution = screen.getCurrentResolution();
      var config = dao.config.find("screen").getValue().split("x");
      var width = Integer.parseInt(config[0]);
      var height = Integer.parseInt(config[1]);
      screen.setSize(width, height);
      screen.setPosition((resolution.x() - width) / 2, (resolution.y() - height) / 2);
   }

   private void configureCamera() {
      context.getCamera().setScale(Float.parseFloat(dao.config.find("camera_scale").getValue()));
   }

   private void initMenu() {
      this.guiManager = new GuiManager(this, context);
   }

   private void initHUD() {
      hud = context.newGUI();
      hud.hide();
      var hudComponent = hud.inflateComponent(A.widgets.hud.$);
      hud.setRoot(hudComponent);
   }

   private void initPlayer() {
      this.player = new Player(context.createCharacter(A.charsets.luna.$));
   }

   public void newGame() {
      context.resetMaps();
      guiManager.closeAll();
      guiManager.enableGameMenu();
      player.reset();
      var start = dao.config.find("start_game").getValue().split(",");

      var map = A.maps.byName(start[0]);
      var layer = map.layer(start[1]);
      var label = layer.label(start[2]);

      context.openMap(map.$);
      context.getMap().getObjectLayer(layer.$).addEntity(this.player);
      player.setCoordinates(label.getX(), label.getY());
      context.resume();
      hud.show();

      var x = A.maps.hero_home.main.entry;
   }

   public void returnToStartMenu() {
      guiManager.closeAll();
      hud.hide();
      context.pause();
      context.closeMap();
      guiManager.disableGameMenu();
      guiManager.showStartMenu();
   }

   public void exit() {
      context.close();
   }


   @Override
   public void dispose() {
      // Do something after game loop is end
   }

   @Override
   public void init(Context context) {
      DemoRunner.INSTANCE = this;

      this.context = context;
      this.screen = context.getScreen();
      this.time = new WorldTime(context);

      configureScreen();
      configureCamera();
      initPlayer();
      initHUD();
      initMenu();

      guiManager.showStartMenu();

      screen.show();
   }

   @Override
   public void update(float dt) {
      time.update(dt);
   }
}