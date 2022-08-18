package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.*;

import com.bartlomiejpluta.base.api.screen.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.*;

import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.entity.*;

import com.bartlomiejpluta.demo.event.*;
import com.bartlomiejpluta.demo.util.LimitedQueue;

import java.util.stream.Collectors;

public class HUD extends BorderLayout {
	private static final int MAX_LOG_SIZE = 10;
	private static final float LOG_VISIBILITY_DURATION = 8000f;
	private static final float LOG_VISIBILITY_FADING_OUT = 1000f;
	private final DemoRunner runner;
	private final Player player;
	private final Runtime runtime;
	private LimitedQueue<String> log = new LimitedQueue<>(MAX_LOG_SIZE);

	private float logVisibilityDuration = 0f;

	@Ref("debug")
	private Label debugLbl;

	@Ref("log")
	private Label logLbl;

	public HUD(Context context, GUI gui) {
		super(context, gui);
		this.runner = (DemoRunner) context.getGameRunner();
		this.player = runner.getPlayer();
		this.runtime = Runtime.getRuntime();
		context.addEventListener(HitEvent.TYPE, this::logHitEvent);
		context.addEventListener(EnemyDiedEvent.TYPE, this::logEnemyDiedEvent);
	}

	private void logHitEvent(HitEvent event) {
		log.add(String.format("%s hits %s with damage = %d", event.getAttacker().getName(), event.getTarget().getName(), event.getDamage()));
		updateLog();
	}

	private void updateLog() {
		logLbl.setText(log.stream().collect(Collectors.joining("\n")));
		logVisibilityDuration = LOG_VISIBILITY_DURATION;
	}

	private void logEnemyDiedEvent(EnemyDiedEvent event) {
		log.add(String.format("%s has died with HP = %d", event.getEnemy().getName(), event.getEnemy().getHp()));
		updateLog();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if(logVisibilityDuration > 0) {
			logVisibilityDuration -= dt * 1000;
		} else {
			logVisibilityDuration = 0;
		}
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

		logLbl.setAlpha(Math.min(1f, logVisibilityDuration / LOG_VISIBILITY_FADING_OUT));

		super.draw(screen, gui);
	}
}