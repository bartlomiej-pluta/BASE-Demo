package com.bartlomiejpluta.demo.menu;

import lombok.*;

import java.util.function.*;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.api.input.*;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.*;

import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.gui.*;

public class MenuManager {
	private final DemoRunner runner;
	private final Context context;
	private final GUI gui;
	private final WindowManager manager;

	private final StartMenuWindow startMenu;
	private final GameMenuWindow gameMenu;

	private final Consumer<KeyEvent> gameMenuHandler = this::handleGameMenuKeyEvent;

	public MenuManager(@NonNull DemoRunner runner, @NonNull Context context) {
		this.runner = runner;
		this.context = context;
		this.gui = context.newGUI();
		this.manager = new WindowManager(context, DisplayMode.DISPLAY_TOP, UpdateMode.UPDATE_TOP);

		this.gui.setRoot(this.manager);

		this.startMenu = (StartMenuWindow) gui.inflateWindow("ab9d40b4-eb28-45d7-bff2-9432a05eb41a");
		this.startMenu.getNewGameBtn().setAction(runner::newGame);
		this.startMenu.getExitBtn().setAction(runner::exit);

		this.gameMenu = (GameMenuWindow) gui.inflateWindow("56ca6b39-f949-4212-9c23-312db25887e0");
		this.gameMenu.getResumeGameBtn().setAction(this::resumeGame);
		this.gameMenu.getStartMenuBtn().setAction(runner::returnToStartMenu);
		this.gameMenu.getExitBtn().setAction(runner::exit);
	}

	private void handleGameMenuKeyEvent(KeyEvent event) {
		if (event.getKey() == Key.KEY_ESCAPE && event.getAction() == KeyAction.PRESS) {
			if(manager.size() > 0) {
				manager.close();
			} else {
				manager.open(gameMenu);
			}

			if(manager.size() > 0) {
				context.pause();
			} else {
				context.resume();
			}

			event.consume();
		}
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