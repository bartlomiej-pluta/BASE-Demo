package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.Ref;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.lib.gui.BorderLayout;
import com.bartlomiejpluta.base.lib.gui.Label;
import com.bartlomiejpluta.base.lib.gui.TextView;
import com.bartlomiejpluta.base.util.profiler.FPSProfiler;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.event.EnemyDiedEvent;
import com.bartlomiejpluta.demo.event.HitEvent;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.util.LimitedQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class HUD extends BorderLayout {
   private static final int MAX_LOG_SIZE = 10;
   private static final float LOG_VISIBILITY_DURATION = 8000f;
   private static final float LOG_VISIBILITY_FADING_OUT = 1000f;
   private final Player player;
   private final Runtime runtime;
   private final LimitedQueue<String> logger = new LimitedQueue<>(MAX_LOG_SIZE);

   private final FPSProfiler fpsProfiler;

   private float logVisibilityDuration = 0f;

   @Ref("hp")
   private Bar hp;

   @Ref("debug")
   private TextView debugTxt;

   @Ref("log")
   private Label logLbl;

   public HUD(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      this.player = DemoRunner.instance().getPlayer();
      this.fpsProfiler = DemoRunner.instance().getFpsProfiler();
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


   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var coords = player.getCoordinates();
      var pos = player.getPosition();
      debugTxt.setText(String.format("FPS: %.2f\n" + "Mem: %.2f / %.2f [MB]\n" + "Coords: %d : %d\n" + "Pos: %.2f : %.2f\n" + "Entities: %d\n", fpsProfiler.getInstantFPS(), runtime.totalMemory() / 1024f / 1024f, runtime.maxMemory() / 1024f / 1024f, coords.x(), coords.y(), pos.x(), pos.y(), player.getLayer().getEntities().size() - 1));

      logLbl.setAlpha(Math.min(1f, logVisibilityDuration / LOG_VISIBILITY_FADING_OUT));

      super.draw(screen, gui);
   }
}