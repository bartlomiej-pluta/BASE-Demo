package com.bartlomiejpluta.demo.world.item;

import com.bartlomiejpluta.demo.entity.Creature;

public interface Useable {
   void use(Creature creature);

   default String usageName() {
      return "Use";
   }
}
