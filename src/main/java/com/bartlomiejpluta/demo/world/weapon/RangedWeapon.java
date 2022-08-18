package com.bartlomiejpluta.demo.world.weapon;

import java.util.Random;

import lombok.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.move.*;
import com.bartlomiejpluta.base.lib.animation.*;
import com.bartlomiejpluta.demo.database.model.RangedWeaponModel;
import com.bartlomiejpluta.demo.entity.Character;
import com.bartlomiejpluta.demo.util.DiceRoller;

import com.bartlomiejpluta.demo.event.HitEvent;

public class RangedWeapon implements Weapon {
	private final Random random = new Random();
	private final Context context;
	private final DiceRoller roller;
	private final BulletAnimationRunner animation;
	private final String sound;
	private final AnimationRunner punchAnimation;
	private final String punchSound;

	@Getter
	private String name;

	@Getter
	private int cooldown;

	public RangedWeapon(@NonNull Context context, @NonNull RangedWeaponModel template) {
		this.context = context;
		this.name = template.getName();
		this.roller = DiceRoller.of(template.getDamage());
		this.cooldown = template.getCooldown();
		this.animation = new BulletAnimationRunner(template.getAnimation())
			.range(template.getRange())
			.infinite()
			.offset(0, -15)
			.onHit(this::onHit)
			.speed(0.25f)
			.animationSpeed(0.07f)
			.scale(0.6f);
		this.sound = template.getSound();
		this.punchAnimation = new SimpleAnimationRunner(template.getPunchAnimation());
		this.punchSound = template.getPunchSound();
	}

	private void onHit(Movable attacker, Entity target) {
		if(target.isBlocking() && target instanceof Character) {
			var character = (Character) target;
			var damage = roller.roll();
			character.hit(damage);
			punchAnimation.run(context, character.getLayer(), character);
			context.playSound(punchSound);
			context.fireEvent(new HitEvent((Character) attacker, character, damage));
		}
	}

	@Override
	public boolean attack(Character attacker) {
		var direction = attacker.getFaceDirection();
		context.playSound(sound);
		animation
			.direction(direction)
			.rotation(direction.xAngle - 180)
			.run(context, attacker.getLayer(), attacker);
		return true;
	}
}