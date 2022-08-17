package com.bartlomiejpluta.demo.entity;

import lombok.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;

import com.bartlomiejpluta.base.lib.ai.NoopAI;

import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.database.model.EnemyModel;

import com.bartlomiejpluta.demo.world.weapon.MeleeWeapon;

public class Enemy extends Character implements NPC {
	private final EnemyModel template;

	public Enemy(@NonNull Context context, @NonNull EnemyModel template) {
		super(context, context.createEntity(template.getEntitySet()));
		this.template = template;
		setSpeed(template.getSpeed());
		setAnimationSpeed(template.getAnimationSpeed());
		setBlocking(template.isBlocking());
		setWeapon(new MeleeWeapon(((DemoRunner) context.getGameRunner()).getMeleeWeaponDAO().get(template.getMeleeWeapon())));
	}

	@Override
	public AI getStrategy() {
		return NoopAI.INSTANCE;
	}

	@Override
	public String toString() {
		return template.getName() + "@" + hashCode();
	}
}