package com.bartlomiejpluta.demo.entity;

import lombok.*;

import com.bartlomiejpluta.base.api.context.*;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.move.MoveEvent;

import com.bartlomiejpluta.base.lib.ai.*;
import com.bartlomiejpluta.base.lib.animation.*;
import com.bartlomiejpluta.base.util.random.DiceRoller;

import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.world.weapon.*;
import com.bartlomiejpluta.demo.event.EnemyDiedEvent;
import com.bartlomiejpluta.demo.ai.*;
import com.bartlomiejpluta.demo.ai.ArcherAI;


public class Enemy extends Creature implements NPC {
	private final DB.model.EnemyModel template;
	private AI ai = NoopAI.INSTANCE;
	private final AnimationRunner dieAnimation;

	@Getter
	private MeleeWeapon meleeWeapon;

	@Getter
	private RangedWeapon rangedWeapon;

	@Getter
	private final String name;

	public Enemy(@NonNull String id) {
		this(DB.dao.enemy.find(id));
	}

	public Enemy(@NonNull DB.model.EnemyModel template) {
		super(ContextHolder.INSTANCE.getContext().createCharacter(A.charsets.get(template.getCharset()).uid));
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
			this.meleeWeapon = new MeleeWeapon(meleeWeaponTemplate);
		}

		if(rangedWeaponTemplate != null) {
			this.rangedWeapon = new RangedWeapon(rangedWeaponTemplate);
		}

		this.dieAnimation = new SimpleAnimationRunner(A.animations.get(template.getDieAnimation()).uid);
	}

	@Override
	public AI getStrategy() {
		return ai;
	}

	@Override
	public void die() {
		super.die();
		changeCharacterSet(A.charsets.get(template.getDeadCharset()).uid);
		setScale(0.5f);
		setBlocking(false);
		setZIndex(-1);

		ai = NoopAI.INSTANCE;

		dieAnimation.run(context, getLayer(), this);
		context.playSound(A.sounds.get(template.getDieSound()).uid);
		context.fireEvent(new EnemyDiedEvent(this));
	}

	public Enemy followAndAttack(Creature target, int range) {
		var ai = new SimpleEnemyAI(this, target, range);

		addEventListener(MoveEvent.TYPE, ai::recomputePath);
		addEventListener(EnemyDiedEvent.TYPE, e -> ai.recomputePath());

		this.ai = ai;

		return this;
	}

	public Enemy campAndHunt(Creature target, int range) {
		this.ai = new SimpleSniperAI(this, target, range);

		return this;
	}

	public Enemy asAnimal(Creature source, int range) {
		this.ai = new AnimalAI(this, source, range);

		return this;
	}

	public Enemy archer(Creature target, int minRange, int maxRange, int range) {
		var ai = new ArcherAI(this, target, minRange, maxRange, range);

		addEventListener(MoveEvent.TYPE, ai::recomputePath);
		addEventListener(EnemyDiedEvent.TYPE, e -> ai.recomputePath());

		this.ai = ai;

		return this;
	}

	public Enemy defaultAI() {
		var ai = new WeaponBasedAI(this, runner.getPlayer());

		addEventListener(MoveEvent.TYPE, ai::recomputePath);
		addEventListener(EnemyDiedEvent.TYPE, e -> ai.recomputePath());

		this.ai = ai;

		return this;
	}
}