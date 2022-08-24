package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.api.input.*;
import com.bartlomiejpluta.base.api.screen.*;
import com.bartlomiejpluta.base.lib.gui.*;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.event.EnemyDiedEvent;
import com.bartlomiejpluta.demo.event.HitEvent;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.util.LimitedQueue;
import com.bartlomiejpluta.demo.world.weapon.Weapon;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
public class HUD extends BorderLayout {
    private static final int MAX_LOG_SIZE = 10;
    private static final float LOG_VISIBILITY_DURATION = 8000f;
    private static final float LOG_VISIBILITY_FADING_OUT = 1000f;
    private final DemoRunner runner;
    private final Player player;
    private final Runtime runtime;
    private LimitedQueue<String> logger = new LimitedQueue<>(MAX_LOG_SIZE);

    private float logVisibilityDuration = 0f;

    private Weapon currentWeapon;

    @Ref("hp")
    private Bar hp;

    @Ref("debug")
    private Label debugLbl;

    @Ref("log")
    private Label logLbl;

    @Ref("weapon")
    private IconView weapon;

    public HUD(Context context, GUI gui) {
        super(context, gui);
        this.runner = (DemoRunner) context.getGameRunner();
        this.player = runner.getPlayer();
        this.runtime = Runtime.getRuntime();
        context.addEventListener(HitEvent.TYPE, this::logHitEvent);
        context.addEventListener(EnemyDiedEvent.TYPE, this::logEnemyDiedEvent);
    }

    private void logHitEvent(HitEvent event) {
        log(String.format("%s hits %s with damage = %d", event.getAttacker().getName(), event.getTarget().getName(), event.getDamage()));
    }

    private void log(String message) {
        logger.add(message);
        log.info(message);
        logLbl.setText(logger.stream().collect(Collectors.joining("\n")));
        logVisibilityDuration = LOG_VISIBILITY_DURATION;
    }

    private void logEnemyDiedEvent(EnemyDiedEvent event) {
        log(String.format("%s has died with HP = %d", event.getEnemy().getName(), event.getEnemy().getHp()));
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        hp.setValue((float) player.getHp() / (float) player.getMaxHp());

        if (logVisibilityDuration > 0) {
            logVisibilityDuration -= dt * 1000;
        } else {
            logVisibilityDuration = 0;
        }

        if (player.getWeapon() != null && player.getWeapon() != currentWeapon) {
            weapon.setIcon(player.getWeapon().getIcon());
            this.currentWeapon = player.getWeapon();
        } else if (player.getWeapon() == null) {
            this.currentWeapon = null;
        }
    }

    @Override
    public void draw(Screen screen, GUI gui) {
        var coords = player.getCoordinates();
        var pos = player.getPosition();
        debugLbl.setText(String.format("FPS: %.2f\n" + "Mem: %.2f / %.2f [MB]\n" + "Coords: %d : %d\n" + "Pos: %.2f : %.2f\n" + "Entities: %d", runner.instantFPS(), runtime.totalMemory() / 1024f / 1024f, runtime.maxMemory() / 1024f / 1024f, coords.x(), coords.y(), pos.x(), pos.y(), player.getLayer().getEntities().size() - 1));

        logLbl.setAlpha(Math.min(1f, logVisibilityDuration / LOG_VISIBILITY_FADING_OUT));

        super.draw(screen, gui);
    }
}