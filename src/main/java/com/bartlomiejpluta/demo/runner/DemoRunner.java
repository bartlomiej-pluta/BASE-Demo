package com.bartlomiejpluta.demo.runner;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.runner.GameRunner;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.util.profiler.FPSProfiler;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.menu.GuiManager;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DemoRunner implements GameRunner {
   private static final Logger log = LoggerFactory.getLogger(DemoRunner.class);
   private static DemoRunner INSTANCE;

   private Screen screen;
   private Context context;
   private GUI hud;

   @Getter
   private final FPSProfiler fpsProfiler = FPSProfiler.create(20);
   @Getter
   private GuiManager guiManager;

   @Getter
   private Player player;

   public static DemoRunner instance() {
      return INSTANCE;
   }

   private void configureScreen() {
      var resolution = screen.getCurrentResolution();
      screen.setSize(1800, 1000);
      screen.setPosition((resolution.x() - 1800) / 2, (resolution.y() - 1000) / 2);
   }

   private void configureCamera() {
      context.getCamera().setScale(2f);
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

   private void resetPlayer() {
      this.player.changeCharacterSet(A.charsets.luna.uid);
      this.player.setScale(1f);
      this.player.setSpeed(4f);
      this.player.setAnimationSpeed(1f);
      this.player.setBlocking(true);
   }

   public void newGame() {
      guiManager.closeAll();
      guiManager.enableGameMenu();
      resetPlayer();
      context.openMap(A.maps.hero_home.uid);
      context.getMap().getObjectLayer(A.maps.hero_home.layers.main).addEntity(this.player);
      player.setCoordinates(11, 14);
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
   public void update(float dt) {
      fpsProfiler.update(dt);
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