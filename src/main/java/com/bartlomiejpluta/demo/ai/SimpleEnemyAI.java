package com.bartlomiejpluta.demo.ai;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;

import com.bartlomiejpluta.base.util.pathfinder.*;
import com.bartlomiejpluta.base.api.ai.*;
import com.bartlomiejpluta.base.lib.ai.*;

import com.bartlomiejpluta.demo.entity.Enemy;
import com.bartlomiejpluta.demo.entity.Character;

public class SimpleEnemyAI extends FollowEntityAI<Enemy, Character> {
	private static final int ASTAR_MAX_NODES = 100;
	private static final int IDLE_MOVEMENT_INTERVAL = 4;
	private final AI idle;
	private final int range;

	public SimpleEnemyAI(Enemy enemy, Character target, int range) {
		super(new AstarPathFinder(ASTAR_MAX_NODES), enemy, target);
		this.range = range;
		this.idle = new RandomMovementAI(enemy, IDLE_MOVEMENT_INTERVAL);
	}

	@Override
	protected boolean sees(Enemy enemy, Character target, ObjectLayer layer, int distance) {
		return distance < range;
	}

	@Override
	protected void interact(Enemy enemy, Character target, ObjectLayer layer, float dt) {
		enemy.attack();
	}

	@Override
	protected void follow(Enemy enemy, Character target, ObjectLayer layer, float dt) {
		// noop
	}

	@Override
	protected void idle(Enemy enemy, Character target, ObjectLayer layer, float dt) {
		idle.nextActivity(layer, dt);
	}
}