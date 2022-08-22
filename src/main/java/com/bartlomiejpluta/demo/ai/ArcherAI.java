package com.bartlomiejpluta.demo.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.MoveEvent;
import com.bartlomiejpluta.base.lib.ai.KeepStraightDistanceAI;
import com.bartlomiejpluta.base.lib.ai.RandomMovementAI;
import com.bartlomiejpluta.base.util.path.MovementPath;
import com.bartlomiejpluta.base.util.path.PathExecutor;
import com.bartlomiejpluta.base.util.pathfinder.AstarPathFinder;
import com.bartlomiejpluta.base.util.pathfinder.PathFinder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import com.bartlomiejpluta.demo.entity.Enemy;
import com.bartlomiejpluta.demo.entity.Creature;

public class ArcherAI extends KeepStraightDistanceAI<Enemy, Creature> {
	private static final int ASTAR_MAX_NODES = 100;
	private static final int IDLE_MOVEMENT_INTERVAL = 4;
	private final int range;
	private final AI idle;

	public ArcherAI(Enemy enemy, Creature target, int minRange, int maxRange, int range) {
		super(new AstarPathFinder(ASTAR_MAX_NODES), enemy, target, minRange, maxRange);
		this.range = range;
		this.idle = new RandomMovementAI<>(enemy, IDLE_MOVEMENT_INTERVAL);
	}

	@Override
	protected boolean sees(Enemy enemy, Creature target, ObjectLayer layer) {
		return enemy.manhattanDistance(target) < range;
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
