package com.bartlomiejpluta.demo.entity;

import lombok.*;
import org.slf4j.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;

public class Player extends Character {
	private static final Logger log = LoggerFactory.getLogger(Player.class);

	public Player(@NonNull Context context, @NonNull Entity entity) {
		super(context, entity);
		this.hp = 100;
		this.maxHp = 100;
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