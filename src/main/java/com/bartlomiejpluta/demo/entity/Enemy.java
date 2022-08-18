package com.bartlomiejpluta.demo.entity;

import lombok.*;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.move.MoveEvent;

import com.bartlomiejpluta.base.lib.ai.*;
import com.bartlomiejpluta.base.lib.animation.*;
import com.bartlomiejpluta.base.util.random.DiceRoller;

import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.database.model.EnemyModel;
import com.bartlomiejpluta.demo.world.weapon.*;
import com.bartlomiejpluta.demo.event.EnemyDiedEvent;
import com.bartlomiejpluta.demo.ai.*;


public class Enemy extends Character implements NPC {
	private final EnemyModel template;
	private AI ai = NoopAI.INSTANCE;
	private final AnimationRunner dieAnimation;

	@Getter
	private final String name;

	public Enemy(@NonNull Context context, @NonNull EnemyModel template) {
		super(context, context.createEntity(template.getEntitySet()));
		this.template = template;
		name = template.getName();
		maxHp = DiceRoller.of(template.getHp()).roll();
		hp = maxHp;
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

		dieAnimation.run(context, getLayer(), this);
		context.playSound(template.getDieSound());
		context.fireEvent(new EnemyDiedEvent(this));
	}

	public Enemy followAndAttack(Character target, int range) {
		var ai = new SimpleEnemyAI(this, target, range);

		addEventListener(MoveEvent.TYPE, e -> ai.recomputePath());
		addEventListener(EnemyDiedEvent.TYPE, e -> ai.recomputePath());

		this.ai = ai;

		return this;
	}

	public Enemy campAndHunt(Character target, int range) {
		this.ai = new SimpleSniperAI(this, target, range);

		return this;
	}

	public Enemy asAnimal(Character source, int range) {
		this.ai = new AnimalAI(this, source, range);

		return this;
	}
}