package com.bartlomiejpluta.demo.menu;

import A.widgets;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.input.KeyAction;
import com.bartlomiejpluta.base.api.input.KeyEvent;
import com.bartlomiejpluta.demo.entity.Chest;
import com.bartlomiejpluta.demo.entity.Enemy;
import com.bartlomiejpluta.demo.gui.*;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GuiManager {
   private final DemoRunner runner;
   private final Context context;
   private final GUI gui;
   private final WindowManager manager;

   private final StartMenuWindow startMenu;
   private final GameMenuWindow gameMenu;
   private final EquipmentWindow equipment;
   private final LootWindow loot;
   private final DialogWindow dialog;
   private final Consumer<KeyEvent> gameMenuHandler = this::handleGameMenuKeyEvent;

   public GuiManager(@NonNull DemoRunner runner, @NonNull Context context) {
      this.runner = runner;
      this.context = context;
      this.gui = context.newGUI();
      this.manager = new WindowManager(context, DisplayMode.DISPLAY_TOP, UpdateMode.UPDATE_TOP);

      this.gui.setRoot(this.manager);

      this.startMenu = gui.inflateWindow(A.widgets.start_menu.uid, StartMenuWindow.class);
      this.startMenu.getNewGameBtn().setAction(runner::newGame);
      this.startMenu.getExitBtn().setAction(runner::exit);

      this.gameMenu = gui.inflateWindow(A.widgets.game_menu.uid, GameMenuWindow.class);
      this.gameMenu.getResumeGameBtn().setAction(this::resumeGame);
      this.gameMenu.getStartMenuBtn().setAction(runner::returnToStartMenu);
      this.gameMenu.getExitBtn().setAction(runner::exit);

      this.dialog = gui.inflateWindow(A.widgets.dialog.uid, DialogWindow.class);

      this.equipment = gui.inflateWindow(A.widgets.equipment.uid, EquipmentWindow.class);
      this.loot = gui.inflateWindow(widgets.loot_menu.uid, LootWindow.class);
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

   public CompletableFuture<Window> showDialog(@NonNull String message, @NonNull WindowPosition position) {
      manager.closeAll();
      return manager.open(dialog, message, position);
   }

   public void openLootWindow(@NonNull Enemy enemy) {
      manager.closeAll();

      manager.open(loot, enemy.getLoot(), "Loot");
   }

   public void openChestWindow(@NonNull Chest chest) {
      manager.closeAll();

      manager.open(loot, chest.getContent(), chest.getName());
   }

   public void closeAll() {
      manager.closeAll();
   }

   private void resumeGame() {
      manager.closeAll();
      context.resume();
   }
}