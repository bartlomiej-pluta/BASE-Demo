package com.bartlomiejpluta.demo.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.lib.ai.RandomMovementAI;
import com.bartlomiejpluta.base.lib.ai.RunawayAI;
import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.entity.Enemy;

public class AnimalAI implements AI {
   private final Enemy animal;
   private final Creature creature;
   private final int range;
   private final AI idleAI;
   private final AI runawayAI;

   public AnimalAI(Enemy animal, Creature creature, int range) {
      this.animal = animal;
      this.creature = creature;
      this.range = range;
      this.idleAI = new RandomMovementAI<>(animal, 4);
      this.runawayAI = new RunawayAI<>(animal, creature);
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      if (animal.isMoving()) {
         return;
      }

      var distance = animal.manhattanDistance(creature);

      if (animal.getHp() < animal.getMaxHp() && distance < range) {
         runawayAI.nextActivity(layer, dt);
         return;
      }

      idleAI.nextActivity(layer, dt);
   }
}