package com.bartlomiejpluta.demo.map;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.demo.entity.Friend;

import java.util.concurrent.CompletableFuture;

import static com.bartlomiejpluta.base.lib.animation.AnimationRunner.simple;

public class HeroHomeHandler extends BaseMapHandler {
   @Override
   public void onOpen(Context context, GameMap map) {
      dialog(player, "Ahhh, another beautiful day for an adventure... Let's go!");
   }

   protected CompletableFuture<Object> triggerGrandmaDialog(Friend grandma) {
      return dialog(player, "Good morning Grandma, how are you doing?")
         .thenCompose(n -> dialog(grandma, "Hello Honey... I'm fine thank you. Have a sit, I will bring you a breakfast in a moment."))
         .thenCompose(n -> dialog(player, "Thank you Grandma."))
         .thenCompose(n -> dialog(grandma, "What are you going to do today, Luna?"))
         .thenCompose(n -> dialogChoice(player,
            "I'm going to fix your roof, Grandma",
            "I'd like to look for some hidden treasure around your house",
            "I'm going to kill the Dekus outside",
            "Hmm... I don't know yet..."
         )).thenCompose(o -> dialog(grandma, switch (o) {
            case 0 -> "Ohh, Luna! It would be amazing, thank you!";
            case 1 -> "It seems you are going to have a wonderful adventure. Good luck!";
            case 2 -> "Be careful! Dekus are capable to attack from a distance!";
            case 3 -> "Hmm... our roof requires urgent fix... also there are some dangerous Dekus around our house...";
            default -> null;
         }).thenApply(n -> o))
         .thenCompose(o -> dialog(player, switch (o) {
            case 0 -> "You're welcome Grandma!";
            case 1 -> "Thank you Grandma! Hopefully will found something interesting...";
            case 2 -> "Oh, thanks. I believe I also would need to have some ranged weapon...";
            case 3 -> "Okay, there is lot of things to be done then.";
            default -> null;
         }))
         .thenCompose(n -> dialog(player, "Okay, I need to go. Thank you Grandma!"))
         .thenCompose(n -> dialog(grandma, "Thank you Luna! Be careful!"));
   }

   protected CompletableFuture<Object> triggerNekoDialog(Friend neko) {
      return dialog(player, "Ohhh, here you are Kitty...")
         .thenCompose(n -> CompletableFuture.allOf(
            simple(A.animations.heart_emoji.$).scale(0.4f).animationSpeed(1.7f).offset(0, -30).run(context, player),
            simple(A.animations.heart_emoji.$).scale(0.4f).animationSpeed(1.7f).offset(0, -15).delay(100).run(context, neko)
         ))
         .thenCompose(n -> dialog(neko, "Meow, meow..."))
         .thenCompose(n -> dialog(neko, "Purr, purr..."));
   }
}