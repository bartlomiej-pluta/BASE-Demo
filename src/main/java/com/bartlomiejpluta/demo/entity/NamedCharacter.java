package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.lib.animation.AnimationRunner;
import com.bartlomiejpluta.base.lib.animation.SimpleAnimationRunner;
import com.bartlomiejpluta.base.lib.character.CharacterDelegate;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class NamedCharacter extends CharacterDelegate {
   protected final Context context;
   protected final DemoRunner runner;

   public NamedCharacter(Character character) {
      super(character);
      this.context = ContextHolder.INSTANCE.getContext();
      this.runner = DemoRunner.instance();
   }

   public abstract String getName();

   public int getDialogNameColor() {
      return 0xFFFFFF;
   }

   public CompletableFuture<Void> runEmoji(@NonNull String animation) {
      return runEmoji(animation, null);
   }

   public CompletableFuture<Void> runEmoji(@NonNull String animation, Consumer<SimpleAnimationRunner> customizer) {
      var runner = AnimationRunner
         .simple(animation)
         .scale(0.4f)
         .animationSpeed(1.6f)
         .offset(0, -30);

      if (customizer != null) {
         customizer.accept(runner);
      }

      return runner.run(context, this);
   }
}