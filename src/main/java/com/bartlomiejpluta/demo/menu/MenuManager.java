package com.bartlomiejpluta.demo.menu;

import lombok.*;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.screen.Screen;
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

	public MenuManager(@NonNull DemoRunner runner, @NonNull Context context) {
		this.runner = runner;
		this.context = context;
		this.gui = context.newGUI();
		this.manager = new WindowManager(context, DisplayMode.DISPLAY_TOP, UpdateMode.UPDATE_TOP);

		this.gui.setRoot(this.manager);

		this.startMenu = (StartMenuWindow) gui.inflateWindow("ab9d40b4-eb28-45d7-bff2-9432a05eb41a");
		this.startMenu.getNewGameBtn().setAction(runner::newGame);
		this.startMenu.getExitBtn().setAction(runner::exit);
	}

	public void showStartMenu() {
		manager.closeAll();
		manager.open(startMenu);
	}

	public void closeAll() {
		manager.closeAll();
	}
}