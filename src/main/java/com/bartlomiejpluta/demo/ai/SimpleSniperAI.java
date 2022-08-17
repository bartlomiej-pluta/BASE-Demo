package com.bartlomiejpluta.demo.ai;

import lombok.*;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.ai.*;
import com.bartlomiejpluta.base.lib.ai.*;

import com.bartlomiejpluta.demo.entity.Enemy;
import com.bartlomiejpluta.demo.entity.Character;

@AllArgsConstructor
public class SimpleSniperAI implements AI {
	private final Enemy enemy;
	private Character target;
	private int range;

	@Override
	public void nextActivity(ObjectLayer layer, float dt) {
		var enemyCoords = enemy.getCoordinates();
		var targetCoords = target.getCoordinates();
		if(enemy.manhattanDistance(target) <= range && (enemyCoords.x() == targetCoords.x() || enemyCoords.y() == targetCoords.y())) {
			var direction = enemy.getDirectionTowards(target);
			enemy.setFaceDirection(direction);
			enemy.attack();
		}
	}
}