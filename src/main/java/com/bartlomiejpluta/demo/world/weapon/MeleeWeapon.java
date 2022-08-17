package com.bartlomiejpluta.demo.world.weapon;

import java.util.Random;

import lombok.*;
import com.bartlomiejpluta.demo.database.model.MeleeWeaponModel;
import com.bartlomiejpluta.demo.entity.Character;
import com.bartlomiejpluta.demo.util.DiceRoller;

public class MeleeWeapon {
	private final Random random = new Random();
	private final DiceRoller roller;

	@Getter
	private String name;

	@Getter
	private int cooldown;

	public MeleeWeapon(MeleeWeaponModel template) {
		this.name = template.getName();
		this.roller = DiceRoller.of(template.getDamage());
		this.cooldown = template.getCooldown();
	}

	public void attack(Character character) {
		character.hit(roller.roll());
	}
}