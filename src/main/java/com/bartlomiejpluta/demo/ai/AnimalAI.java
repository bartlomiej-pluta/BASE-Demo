package com.bartlomiejpluta.demo.ai;

import lombok.*;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.ai.*;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.lib.ai.*;

import com.bartlomiejpluta.demo.entity.Enemy;
import com.bartlomiejpluta.demo.entity.Character;

public class AnimalAI implements AI {
	private final Enemy animal;
	private final Character character;
	private final int range;
	private final AI idleAI;
	private final AI runawayAI;

	public AnimalAI(Enemy animal, Character character, int range) {
		this.animal = animal;
		this.character = character;
		this.range = range;
		this.idleAI = new RandomMovementAI(animal, 4);
		this.runawayAI = new RunawayAI<>(animal, character);
	}

	@Override
   public void nextActivity(ObjectLayer layer, float dt) {
		if(animal.isMoving()) {
			return;
		}

		var distance = animal.manhattanDistance(character);

		if(animal.getHp() < animal.getMaxHp() && distance < range) {
			runawayAI.nextActivity(layer, dt);
			return;
		}

		idleAI.nextActivity(layer, dt);
   }
}