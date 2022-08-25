package com.bartlomiejpluta.demo.world.weapon;

import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.item.Useable;

public interface Weapon extends Item, Useable {

   int getCooldown();

   boolean attack(Creature attacker);
}