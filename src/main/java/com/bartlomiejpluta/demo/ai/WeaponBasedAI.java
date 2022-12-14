package com.bartlomiejpluta.demo.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.MoveEvent;
import com.bartlomiejpluta.base.lib.ai.RunawayAI;
import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.entity.Enemy;
import lombok.NonNull;

public class WeaponBasedAI implements AI {
   private static final int RANGE = 20;
   private static final int MIN_RANGE = 3;
   private static final int MAX_RANGE = 12;
   private final Enemy enemy;
   private final Creature target;
   private final RunawayAI<Enemy, Creature> runawayAI;
   private final SimpleEnemyAI meleeAI;
   private final ArcherAI archerAI;

   public WeaponBasedAI(@NonNull Enemy enemy, @NonNull Creature target) {
      this.enemy = enemy;
      this.target = target;
      this.runawayAI = new RunawayAI<>(enemy, target);
      this.meleeAI = new SimpleEnemyAI(enemy, target, RANGE);
      this.archerAI = new ArcherAI(enemy, target, MIN_RANGE, MAX_RANGE, RANGE);
   }

   public void recomputePath() {
      meleeAI.recomputePath();
      archerAI.recomputePath();
   }

   public void recomputePath(@NonNull MoveEvent event) {
      meleeAI.recomputePath(event);
      archerAI.recomputePath(event);
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      var lastAttacker = enemy.getLastAttacker();
      if (lastAttacker instanceof Creature attacker) {
         if (attacker.isAlive()) {
            runawayAI.setDanger(attacker);
            meleeAI.setTarget(attacker);
            archerAI.setTarget(attacker);
         } else {
            runawayAI.setDanger(target);
            meleeAI.setTarget(target);
            archerAI.setTarget(target);
         }
      }

      var meleeWeapon = enemy.getMeleeWeapon();
      var rangedWeapon = enemy.getRangedWeapon();
      var throwingWeapon = enemy.getThrowingWeapon();

      if (meleeWeapon == null && rangedWeapon == null && throwingWeapon == null) {
         runawayAI.nextActivity(layer, dt);
         return;
      }

      if ((rangedWeapon == null && throwingWeapon == null) || (rangedWeapon != null && enemy.getAmmunition() == null && throwingWeapon == null) || enemy.manhattanDistance(target) == 1) {
         enemy.setWeapon(meleeWeapon);
         meleeAI.nextActivity(layer, dt);
         return;
      }

      if (throwingWeapon != null) {
         enemy.setWeapon(throwingWeapon);
         archerAI.nextActivity(layer, dt);
         return;
      }

      enemy.setWeapon(rangedWeapon);
      archerAI.nextActivity(layer, dt);

//      if (enemy.getWeapon() instanceof MeleeWeapon) {
//         meleeAI.nextActivity(layer, dt);
//      }


   }
}