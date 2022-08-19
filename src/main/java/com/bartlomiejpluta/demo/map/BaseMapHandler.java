package com.bartlomiejpluta.demo.map;

import lombok.*;

import com.bartlomiejpluta.base.api.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.input.*;

import com.bartlomiejpluta.base.lib.camera.*;

import com.bartlomiejpluta.base.util.world.EntitySpawner;

import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.entity.*;
import com.bartlomiejpluta.demo.event.*;

import com.bartlomiejpluta.demo.util.*;

import java.util.*;
import java.util.function.*;

public abstract class BaseMapHandler implements MapHandler {
	protected Screen screen;
	protected Context context;
	protected DemoRunner runner;
	protected Camera camera;
	protected GameMap map;
	protected Player player;
	protected ObjectLayer mainLayer;
	protected CameraController cameraController;
	protected final List<EntitySpawner> spawners = new LinkedList<>();

	@Override
	public void onCreate(Context context, GameMap map) {
		this.context = context;
		this.screen = context.getScreen();
		this.runner = (DemoRunner) context.getGameRunner();
		this.camera = context.getCamera();
		this.map = map;
		this.player = runner.getPlayer();
		this.cameraController = FollowingCameraController
			.on(screen, camera, map)
			.follow(player.getPosition());
	}

	@Override
	public void input(Input input) {
		if(context.isPaused()) {
			return;
		}

		if(input.isKeyPressed(Key.KEY_SPACE)) {
			player.attack();
		}

		if(input.isKeyPressed(Key.KEY_ENTER)) {
			player.interact();
		}

		if(input.isKeyPressed(Key.KEY_LEFT_CONTROL)) {
			if(input.isKeyPressed(Key.KEY_DOWN)) {
				player.setFaceDirection(Direction.DOWN);
			} else if(input.isKeyPressed(Key.KEY_UP)) {
				player.setFaceDirection(Direction.UP);
			} else if(input.isKeyPressed(Key.KEY_LEFT)) {
				player.setFaceDirection(Direction.LEFT);
			} else if(input.isKeyPressed(Key.KEY_RIGHT)) {
				player.setFaceDirection(Direction.RIGHT);
			}		
		} else {
			if(input.isKeyPressed(Key.KEY_DOWN)) {
				mainLayer.pushMovement(player.prepareMovement(Direction.DOWN));
			} else if(input.isKeyPressed(Key.KEY_UP)) {
				mainLayer.pushMovement(player.prepareMovement(Direction.UP));
			} else if(input.isKeyPressed(Key.KEY_LEFT)) {
				mainLayer.pushMovement(player.prepareMovement(Direction.LEFT));
			} else if(input.isKeyPressed(Key.KEY_RIGHT)) {
				mainLayer.pushMovement(player.prepareMovement(Direction.RIGHT));
			}
		}
	}

	@Override
	public void update(Context context, GameMap map, float dt) {
		cameraController.update();
		for(var spawner : spawners) {
			spawner.update(dt);
		}
	}

	public Enemy enemy(int x, int y, @NonNull String id) {
		var enemy = new Enemy(context, runner.getEnemyDAO().find(id));
		enemy.setCoordinates(x, y);
		mainLayer.addEntity(enemy);
		return enemy;
	}

	public MapObject object(int x, int y, @NonNull String id) {
		var object = new MapObject(context, runner.getMapObjectDAO().find(id));
		object.setCoordinates(x, y);
		mainLayer.addEntity(object);
		return object;
	}

	public EntitySpawner spawner(int x, int y, ObjectLayer layer) {
		var spawner = new EntitySpawner(x, y, map, layer).trackEntities(EnemyDiedEvent.TYPE);
		this.spawners.add(spawner);
		return spawner;
	}
}