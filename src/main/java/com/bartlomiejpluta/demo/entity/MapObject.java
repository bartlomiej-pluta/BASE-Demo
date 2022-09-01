package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class MapObject extends com.bartlomiejpluta.base.util.world.MapObject {
   protected final Context context;
   protected final DemoRunner runner;
   @Getter
   private final String name;
   private final String interactSound;

   public MapObject(@NonNull String id) {
      this(DB.dao.object.find(id));
   }

   public MapObject(@NonNull DB.model.ObjectModel template) {
      super(ContextHolder.INSTANCE.getContext().createCharacter(A.charsets.get(template.getCharset()).uid), template.getFrame());
      this.context = ContextHolder.INSTANCE.getContext();
      this.runner = DemoRunner.instance();
      this.name = template.getName();
      this.interactSound = A.sounds.get(template.getInteractSound()).uid;
   }

   @Override
   protected @NonNull CompletableFuture<?> onInteractionBegin() {
      if (interactSound != null) {
         context.playSound(interactSound);
      }

      return completedFuture(null);
   }
}