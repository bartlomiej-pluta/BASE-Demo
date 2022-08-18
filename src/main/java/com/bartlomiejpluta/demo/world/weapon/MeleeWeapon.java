package com.bartlomiejpluta.demo.world.weapon;

import java.util.Random;

import lombok.*;
import org.joml.Vector2i;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.lib.animation.*;
import com.bartlomiejpluta.base.util.random.DiceRoller;

import com.bartlomiejpluta.base.generated.db.model.MeleeWeaponModel;
import com.bartlomiejpluta.demo.entity.Character;

import com.bartlomiejpluta.demo.event.HitEvent;

public class MeleeWeapon implements Weapon {
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

	@Override
	public boolean attack(Character attacker) {
		var facingNeighbour = attacker.getCoordinates().add(attacker.getFaceDirection().vector, new Vector2i());
		for(var entity : attacker.getLayer().getEntities()) {
			if(entity.getCoordinates().equals(facingNeighbour) && entity.isBlocking() && entity instanceof Character) {
				var character = (Character) entity;
				var damage = roller.roll();
				character.hit(damage);
				animation.run(context, character.getLayer(), character);
				context.playSound(sound);
				context.fireEvent(new HitEvent(attacker, character, damage));
				return true;
			}
		}

		return false;
	}
}