package com.bartlomiejpluta.demo.world.weapon;

import java.util.Random;

import lombok.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.lib.animation.*;
import com.bartlomiejpluta.demo.database.model.MeleeWeaponModel;
import com.bartlomiejpluta.demo.entity.Character;
import com.bartlomiejpluta.demo.util.DiceRoller;

public class MeleeWeapon {
	private final Random random = new Random();
	private final Context context;
	private final DiceRoller roller;
	private final AnimationRunner animation;
	private final String sound;

	@Getter
	private String name;

	@Getter
	private int cooldown;

	public MeleeWeapon(@NonNull Context context, @NonNull MeleeWeaponModel template) {
		this.context = context;
		this.name = template.getName();
		this.roller = DiceRoller.of(template.getDamage());
		this.cooldown = template.getCooldown();
		this.animation = new RandomAnimationsRunner(5)
			.nRange(0, 2f)
			.nScale(0.2f, 0.15f)
			.uAnimationSpeed(0.01f, 0.05f)
			.offset(0, -10)
			.uDelay(0, 500)
			.with(template.getAnimation());
		this.sound = template.getSound();
	}

	public void attack(Character character) {
		character.hit(roller.roll());
		character.runAnimation(animation);
		context.playSound(sound);
	}
}