package com.bartlomiejpluta.demo.world.weapon;

import com.bartlomiejpluta.demo.entity.Character;

public interface Weapon {
	String getName();
	int getCooldown();
	boolean attack(Character attacker);
}