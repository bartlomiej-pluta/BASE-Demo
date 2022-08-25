package com.bartlomiejpluta.demo.world.weapon;

import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.demo.entity.Creature;

public interface Weapon {
   String getName();

   Icon getIcon();

   int getCooldown();

   boolean attack(Creature attacker);
}