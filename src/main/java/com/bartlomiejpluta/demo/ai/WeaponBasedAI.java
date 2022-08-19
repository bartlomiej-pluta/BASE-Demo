package com.bartlomiejpluta.demo.ai;

import lombok.*;
import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.move.MoveEvent;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.lib.ai.*;

import com.bartlomiejpluta.demo.entity.Character;
import com.bartlomiejpluta.demo.entity.Enemy;
import com.bartlomiejpluta.demo.world.weapon.*;

public class WeaponBasedAI implements AI {
	private static final int RANGE = 10;
	private static final int MIN_RANGE = 3;
	private static final int MAX_RANGE = 12;
	private final Enemy enemy;
	private final Character target;
	private final RunawayAI runawayAI;
	private final SimpleEnemyAI meleeAI;
	private final ArcherAI archerAI;

	public WeaponBasedAI(@NonNull Enemy enemy, @NonNull Character target) {
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
		if(lastAttacker != null && lastAttacker instanceof Character) {
			var attacker = (Character) lastAttacker;
			if(attacker.isAlive()) {
				runawayAI.setCharacter(attacker);
				meleeAI.setTarget(attacker);
				archerAI.setTarget(attacker);
			} else {
				runawayAI.setCharacter(target);
				meleeAI.setTarget(target);
				archerAI.setTarget(target);
			}
		}

		if(enemy.getWeapon() == null) {
			runawayAI.nextActivity(layer, dt);
		}

		if(enemy.getWeapon() instanceof MeleeWeapon) {
			meleeAI.nextActivity(layer, dt);
		}

		if(enemy.getWeapon() instanceof RangedWeapon) {
			archerAI.nextActivity(layer, dt);
		}
	}
}