package com.bartlomiejpluta.demo.menu;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.DisplayMode;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.UpdateMode;
import com.bartlomiejpluta.base.api.gui.WindowManager;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.input.KeyAction;
import com.bartlomiejpluta.base.api.input.KeyEvent;
import com.bartlomiejpluta.demo.gui.EquipmentWindow;
import com.bartlomiejpluta.demo.gui.GameMenuWindow;
import com.bartlomiejpluta.demo.gui.StartMenuWindow;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import lombok.NonNull;

import java.util.function.Consumer;

public class MenuManager {
   private final DemoRunner runner;
   private final Context context;
   private final GUI gui;
   private final WindowManager manager;

   private final StartMenuWindow startMenu;
   private final GameMenuWindow gameMenu;
   private final EquipmentWindow equipment;

   private final Consumer<KeyEvent> gameMenuHandler = this::handleGameMenuKeyEvent;

   public MenuManager(@NonNull DemoRunner runner, @NonNull Context context) {
      this.runner = runner;
      this.context = context;
      this.gui = context.newGUI();
      this.manager = new WindowManager(context, DisplayMode.DISPLAY_TOP, UpdateMode.UPDATE_TOP);

      this.gui.setRoot(this.manager);

      this.startMenu = (StartMenuWindow) gui.inflateWindow(A.widgets.start_menu.uid);
      this.startMenu.getNewGameBtn().setAction(runner::newGame);
      this.startMenu.getExitBtn().setAction(runner::exit);

      this.gameMenu = (GameMenuWindow) gui.inflateWindow(A.widgets.game_menu.uid);
      this.gameMenu.getResumeGameBtn().setAction(this::resumeGame);
      this.gameMenu.getStartMenuBtn().setAction(runner::returnToStartMenu);
      this.gameMenu.getExitBtn().setAction(runner::exit);

      this.equipment = (EquipmentWindow) gui.inflateWindow(A.widgets.equipment.uid);
   }

   private void handleGameMenuKeyEvent(KeyEvent event) {
      if (event.getKey() == Key.KEY_E && event.getAction() == KeyAction.PRESS) {
         if (manager.isEmpty()) {
            manager.open(equipment);
         } else if (manager.top() == equipment) {
            manager.close();
         }

         event.consume();
      }

      if (event.getKey() == Key.KEY_ESCAPE && event.getAction() == KeyAction.PRESS) {
         if (manager.size() > 0) {
            manager.close();
         } else {
            manager.open(gameMenu);
         }

         if (manager.size() > 0) {
            context.pause();
         } else {
            context.resume();
         }

         event.consume();
      }
   }

   public int openedWindows() {
      return manager.size();
   }

   public void showStartMenu() {
      manager.closeAll();
      manager.open(startMenu);
   }

   public void enableGameMenu() {
      manager.closeAll();

      context.getInput().addKeyEventHandler(gameMenuHandler);
      manager.setDisplayMode(DisplayMode.DISPLAY_STACK);
   }

   public void disableGameMenu() {
      context.getInput().removeKeyEventHandler(gameMenuHandler);
      manager.setDisplayMode(DisplayMode.DISPLAY_TOP);
   }

   public void closeAll() {
      manager.closeAll();
   }

   private void resumeGame() {
      manager.closeAll();
      context.resume();
   }
}