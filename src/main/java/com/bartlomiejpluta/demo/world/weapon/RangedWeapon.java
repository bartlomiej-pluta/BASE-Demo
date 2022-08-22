package com.bartlomiejpluta.demo.world.weapon;

import java.util.Random;

import lombok.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.move.*;
import com.bartlomiejpluta.base.lib.animation.*;
import com.bartlomiejpluta.base.util.random.DiceRoller;

import com.bartlomiejpluta.demo.entity.Character;
import com.bartlomiejpluta.demo.entity.NamedEntity;

import com.bartlomiejpluta.demo.event.HitEvent;

public class RangedWeapon implements Weapon {
	private final Random random = new Random();
	private final Context context;
	private final DiceRoller dmgRoller;
	private final DiceRoller rangeRoller;
	private final BulletAnimationRunner animation;
	private final String sound;
	private final AnimationRunner punchAnimation;
	private final String punchSound;
	private final AnimationRunner missAnimation;
	private final String missSound;

	@Getter
	private String name;

	@Getter
	private int cooldown;

	public RangedWeapon(@NonNull Context context, @NonNull DB.model.RangedWeaponModel template) {
		this.context = context;
		this.name = template.getName();
		this.dmgRoller = DiceRoller.of(template.getDamage());
		this.rangeRoller = DiceRoller.of(template.getRange());
		this.cooldown = template.getCooldown();
		this.animation = new BulletAnimationRunner(template.getAnimation())
			.infinite()
			.offset(0, -15)
			.onHit(this::onHit)
			.onMiss(this::onMiss)
			.speed(0.25f)
			.animationSpeed(0.07f)
			.scale(0.6f);
		this.sound = template.getSound();
		this.punchAnimation = new SimpleAnimationRunner(template.getPunchAnimation());
		this.punchSound = template.getPunchSound();
		this.missAnimation = new SimpleAnimationRunner(template.getMissAnimation())
			.scale(0.4f);
		this.missSound = template.getMissSound();
	}

	private void onHit(Movable attacker, Entity target) {
		if(target.isBlocking() && target instanceof Character) {
			var namedAttacker = (Character) attacker;
			var character = (Character) target;
			var damage = dmgRoller.roll();
			character.hit(namedAttacker, damage);
			punchAnimation.run(context, character.getLayer(), character);
			context.playSound(punchSound);
			context.fireEvent(new HitEvent(namedAttacker, character, damage));
		}
	}

	private void onMiss(Movable attacker, Animation animation) {
		missAnimation.run(context, ((Character) attacker).getLayer(), animation.getPosition());
		context.playSound(missSound);
	}

	@Override
	public boolean attack(Character attacker) {
		var direction = attacker.getFaceDirection();
		context.playSound(sound);
		animation
			.range(rangeRoller.roll())
			.direction(direction)
			.rotation(direction.xAngle - 180)
			.run(context, attacker.getLayer(), attacker);
		return true;
	}
}