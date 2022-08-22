package com.bartlomiejpluta.demo.world.weapon;

import com.bartlomiejpluta.demo.entity.Creature;

public interface Weapon {
	String getName();
	int getCooldown();
	boolean attack(Creature attacker);
}