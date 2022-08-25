package com.bartlomiejpluta.demo.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.entity.Enemy;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimpleSniperAI implements AI {
   private final Enemy enemy;
   private Creature target;
   private int range;

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      var enemyCoords = enemy.getCoordinates();
      var targetCoords = target.getCoordinates();
      if (enemy.manhattanDistance(target) <= range && (enemyCoords.x() == targetCoords.x() || enemyCoords.y() == targetCoords.y())) {
         var direction = enemy.getDirectionTowards(target);
         enemy.setFaceDirection(direction);
         enemy.attack();
      }
   }
}