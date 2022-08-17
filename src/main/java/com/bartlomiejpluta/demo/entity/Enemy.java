package com.bartlomiejpluta.demo.entity;

import lombok.*;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.move.MoveEvent;

import com.bartlomiejpluta.base.lib.ai.NoopAI;
import com.bartlomiejpluta.base.lib.animation.*;

import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.database.model.EnemyModel;
import com.bartlomiejpluta.demo.world.weapon.*;
import com.bartlomiejpluta.demo.event.EnemyDiedEvent;
import com.bartlomiejpluta.demo.ai.*;


public class Enemy extends Character implements NPC {
	private final EnemyModel template;
	private AI ai = NoopAI.INSTANCE;
	private final AnimationRunner dieAnimation;

	public Enemy(@NonNull Context context, @NonNull EnemyModel template) {
		super(context, context.createEntity(template.getEntitySet()));
		this.template = template;
		hp = template.getHp();
		setSpeed(template.getSpeed());
		setAnimationSpeed(template.getAnimationSpeed());
		setBlocking(template.isBlocking());
		var runner = (DemoRunner) context.getGameRunner();
		var meleeWeaponTemplate = template.getMeleeWeapon();
		var rangedWeaponTemplate = template.getRangedWeapon();

		if(meleeWeaponTemplate != null) {
			setWeapon(new MeleeWeapon(context, runner.getMeleeWeaponDAO().get(meleeWeaponTemplate)));
		} else if(rangedWeaponTemplate != null) {
			setWeapon(new RangedWeapon(context, runner.getRangedWeaponDAO().get(rangedWeaponTemplate)));
		}

		this.dieAnimation = new SimpleAnimationRunner(template.getDieAnimation());
	}

	@Override
	public AI getStrategy() {
		return ai;
	}

	@Override
	public void die() {
		super.die();
		changeEntitySet(template.getDeadEntitySet());
		setScale(0.5f);
		setBlocking(false);
		setZIndex(-1);

		ai = NoopAI.INSTANCE;

		getLayer().handleEvent(new EnemyDiedEvent(this));
		dieAnimation.run(context, getLayer(), this);
		context.playSound(template.getDieSound());
	}

	@Override
	public String toString() {
		return template.getName() + "@" + hashCode();
	}

	public Enemy followAndAttack(Character target, int range) {
		var ai = new SimpleEnemyAI(this, target, range);

		addEventListener(MoveEvent.TYPE, e -> ai.recomputePath());
		addEventListener(EnemyDiedEvent.TYPE, e -> ai.recomputePath());

		this.ai = ai;

		return this;
	}

	public Enemy campAndHunt(Character target, int range) {
		var ai = new SimpleSniperAI(this, target, range);

		this.ai = ai;

		return this;
	}
}