package com.bartlomiejpluta.demo.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.lib.ai.FollowObjectAI;
import com.bartlomiejpluta.base.lib.ai.RandomMovementAI;
import com.bartlomiejpluta.base.util.pathfinder.AstarPathFinder;
import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.entity.Enemy;

public class SimpleEnemyAI extends FollowObjectAI<Enemy, Creature> {
   private static final int ASTAR_MAX_NODES = 100;
   private static final int IDLE_MOVEMENT_INTERVAL = 4;
   private final AI idle;
   private final int range;

   public SimpleEnemyAI(Enemy enemy, Creature target, int range) {
      super(new AstarPathFinder(ASTAR_MAX_NODES), enemy, target);
      this.range = range;
      this.idle = new RandomMovementAI<>(enemy, IDLE_MOVEMENT_INTERVAL);
   }

   @Override
   protected boolean sees(Enemy enemy, Creature target, ObjectLayer layer, int distance) {
      return distance < range;
   }

   @Override
   protected void interact(Enemy enemy, Creature target, ObjectLayer layer, float dt) {
      enemy.attack();
   }

   @Override
   protected void follow(Enemy enemy, Creature target, ObjectLayer layer, float dt) {
      // noop
   }

   @Override
   protected void idle(Enemy enemy, Creature target, ObjectLayer layer, float dt) {
      idle.nextActivity(layer, dt);
   }
}