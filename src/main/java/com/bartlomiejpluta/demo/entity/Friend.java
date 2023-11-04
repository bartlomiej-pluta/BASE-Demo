package com.bartlomiejpluta.demo.entity;

import DB.model.FriendModel;
import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.lib.ai.NoopAI;
import com.bartlomiejpluta.base.lib.ai.RandomMovementAI;
import com.bartlomiejpluta.base.util.random.DiceRoller;
import com.bartlomiejpluta.demo.world.item.Item;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2ic;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Friend extends Creature implements NPC {
   @Getter
   private AI strategy = NoopAI.INSTANCE;
   private AI priorStrategy;
   private boolean interacting;

   @Getter
   private final String name;

   @Getter
   private final int dialogNameColor;
   private Function<Friend, CompletableFuture<Object>> interaction;

   public Friend(@NonNull String id) {
      this(DB.dao.friend.find(id));
   }

   public Friend(@NonNull FriendModel template) {
      super(ContextHolder.INSTANCE.getContext().createCharacter(A.charsets.byName(template.getCharset()).$));
      name = template.getName();
      setSpeed(DiceRoller.roll(template.getSpeed()) / 10f);
      setBlocking(template.isBlocking());
      dialogNameColor = Integer.parseInt(template.getDialogColor(), 16);
   }

   @Override
   public void removeItemFromEquipment(Item item) {

   }

   public CompletableFuture<Object> interact(Character trigger) {
      if (interaction != null && !interacting) {
         setFaceDirection(getDirectionTowards(trigger));

         var movement = getMovement();
         if (movement != null) {
            movement.abort();
         }

         priorStrategy = strategy;
         strategy = NoopAI.INSTANCE;
         interacting = true;

         return interaction.apply(this).thenApply(o -> {
            strategy = priorStrategy;
            interacting = false;
            return o;
         });
      }

      return CompletableFuture.completedFuture(null);
   }

   public Friend interaction(Function<Friend, CompletableFuture<Object>> interaction) {
      this.interaction = interaction;
      return this;
   }

   public Friend randomMovementAI(float intervalSeconds, Vector2ic origin, int radius) {
      this.strategy = new RandomMovementAI<>(this, intervalSeconds, origin, radius);
      return this;
   }

   public Friend randomMovementAI(float intervalSeconds) {
      this.strategy = new RandomMovementAI<>(this, intervalSeconds);
      return this;
   }
}
