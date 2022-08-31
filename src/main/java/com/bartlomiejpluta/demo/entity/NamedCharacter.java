package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.lib.character.CharacterDelegate;
import com.bartlomiejpluta.demo.runner.DemoRunner;

public abstract class NamedCharacter extends CharacterDelegate {
   protected final Context context;
   protected final DemoRunner runner;

   public NamedCharacter(Character character) {
      super(character);
      this.context = ContextHolder.INSTANCE.getContext();
      this.runner = DemoRunner.instance();
   }

   public abstract String getName();
}