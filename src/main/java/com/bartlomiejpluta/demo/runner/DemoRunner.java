package com.bartlomiejpluta.demo.runner;

import DB.dao;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.runner.GameRunner;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.menu.GuiManager;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Integer.parseInt;


public class DemoRunner implements GameRunner {
   private static final Logger log = LoggerFactory.getLogger(DemoRunner.class);
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
      screen.setSize(800, 600);
      screen.setPosition((resolution.x() - 1800) / 2, (resolution.y() - 1000) / 2);
   }

   private void configureCamera() {
      context.getCamera().setScale(2.5f);
   }

   private void initMenu() {
      this.guiManager = new GuiManager(this, context);
   }

   private void initHUD() {
      hud = context.newGUI();
      hud.hide();
      var hudComponent = hud.inflateComponent(A.widgets.hud.uid);
      hud.setRoot(hudComponent);
   }

   private void initPlayer() {
      this.player = new Player(context.createCharacter(A.charsets.luna.uid));
   }

   public void newGame() {
      context.resetMaps();
      guiManager.closeAll();
      guiManager.enableGameMenu();
      player.reset();
      var start = dao.start_game.find((short) 1);
      var startPoint = start.getStartPoint().split(",");
      context.openMap(A.maps.get(startPoint[0]).uid);
      context.getMap().getObjectLayer(A.maps.getLayer(startPoint[0], startPoint[1])).addEntity(this.player);
      player.setCoordinates(parseInt(startPoint[2]), parseInt(startPoint[3]));
      context.resume();
      hud.show();
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

      configureScreen();
      configureCamera();
      initPlayer();
      initHUD();
      initMenu();

      guiManager.showStartMenu();

      screen.show();
   }
}