package com.bartlomiejpluta.demo.entity;

import lombok.*;
import org.joml.Vector2i;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.demo.entity.MapObject;

public class Player extends Character {

	public Player(@NonNull Context context, @NonNull Entity entity) {
		super(context, entity);
		this.hp = 100;
		this.maxHp = 100;
	}

	public void interact() {
		var coords = getCoordinates().add(getFaceDirection().vector, new Vector2i());
		for(var entity : getLayer().getEntities()) {
			if(entity.getCoordinates().equals(coords) && entity instanceof MapObject) {
				((MapObject) entity).interact(this);
			}
		}
	}

	@Override
	public void die() {
		super.die();
		runner.returnToStartMenu();
	}

	@Override
	public String getName() {
		return "Player";
	}
}