package com.bartlomiejpluta.demo.map;

import com.bartlomiejpluta.base.api.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.input.*;

import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.entity.Player;

public abstract class BaseMapHandler implements MapHandler {
	protected Context context;
	protected Player player;
	protected ObjectLayer mainLayer;

	@Override
	public void onCreate(Context context, GameMap map) {
		this.context = context;
		this.player = ((DemoRunner) context.getGameRunner()).getPlayer();
	}

	@Override
	public void input(Input input) {
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