package com.bartlomiejpluta.demo.world.weapon;

import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.world.item.Item;

public interface Weapon extends Item {

   int getCooldown();

   boolean attack(Creature attacker);
}