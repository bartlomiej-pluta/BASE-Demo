package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.*;

import com.bartlomiejpluta.base.api.screen.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.*;

import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.entity.*;

public class HUD extends BorderLayout {
	private final DemoRunner runner;
	private final Player player;
	private final Runtime runtime;

	@Ref("debug")
	private Label debugLbl;

	public HUD(Context context, GUI gui) {
		super(context, gui);
		this.runner = (DemoRunner) context.getGameRunner();
		this.player = runner.getPlayer();
		this.runtime = Runtime.getRuntime();
	}

	@Override
	public void draw(Screen screen, GUI gui) {
		var coords = player.getCoordinates();
		var pos = player.getPosition();
		debugLbl.setText(String.format(
			"FPS: %.2f\n" +
			"Mem: %.2f / %.2f [MB]\n" +
			"Coords: %d : %d\n" +
			"Pos: %.2f : %.2f",
			runner.instantFPS(),
			runtime.totalMemory() / 1024f / 1024f,
			runtime.maxMemory() / 1024f / 1024f,
			coords.x(), coords.y(),
			pos.x(), pos.y())
		);

		super.draw(screen, gui);
	}
}