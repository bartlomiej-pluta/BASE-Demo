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

      this.startMenu = gui.inflateWindow(A.widgets.start_menu.$, StartMenuWindow.class);
      this.startMenu.reference("new_game", Button.class).setAction(runner::newGame);
      this.startMenu.reference("exit", Button.class).setAction(runner::exit);

      this.equipment = gui.inflateWindow(A.widgets.equipment.$, EquipmentWindow.class);

      this.gameMenu = gui.inflateWindow(A.widgets.game_menu.$, GameMenuWindow.class);
      this.gameMenu.reference("resume_game", Button.class).setAction(this::resumeGame);
      this.gameMenu.reference("equipment", Button.class).setAction(() -> manager.open(equipment));
      this.gameMenu.reference("start_menu", Button.class).setAction(runner::returnToStartMenu);
      this.gameMenu.reference("exit", Button.class).setAction(runner::exit);

      this.dialog = gui.inflateWindow(A.widgets.dialog.$, DialogWindow.class);
      this.loot = gui.inflateWindow(widgets.loot_menu.$, LootWindow.class);
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
            context.pause();
         }

         if (manager.isEmpty()) {
            context.resume();
         }

         event.consume();
      }
   }

   public int openedWindows() {
      return manager.size();
   }

   public CompletableFuture<Window> showStartMenu() {
      manager.closeAll();
      return manager.open(startMenu);
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
      return manager.open(dialog, message, position);
   }

   public CompletableFuture<Window> openLootWindow(@NonNull Enemy enemy) {
      return manager.open(loot, enemy.getLoot(), "Loot");
   }

   public CompletableFuture<Window> openChestWindow(@NonNull Chest chest) {
      return manager.open(loot, chest.getContent(), chest.getName());
   }

   public CompletableFuture<Window> openGameMenu() {
      return manager.open(gameMenu);
   }

   public void closeAll() {
      manager.closeAll();
   }

   private void resumeGame() {
      manager.closeAll();
      context.resume();
   }
}