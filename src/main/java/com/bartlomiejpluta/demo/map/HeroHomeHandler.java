package com.bartlomiejpluta.demo.map;

import A.animations;
import A.maps;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.util.path.CharacterPath;
import com.bartlomiejpluta.base.util.pathfinder.AstarPathFinder;
import com.bartlomiejpluta.base.util.pathfinder.PathFinder;
import com.bartlomiejpluta.demo.entity.Friend;

import java.util.concurrent.CompletableFuture;

import static com.bartlomiejpluta.base.api.move.Direction.LEFT;

public class HeroHomeHandler extends BaseMapHandler {
   private final PathFinder finder = new AstarPathFinder(100);

   @Override
   public void onCreate(Context context, GameMap map) {
      super.onCreate(context, map);
      map.setAmbientColor(0.05f, 0.01f, 0.01f);
   }

   protected CharacterPath<Friend> grandmaPath(Friend grandma) {
      return new CharacterPath<Friend>()
         .run(() -> controls = false)
         .run(() -> player.runEmoji(A.animations.zzz.$, r -> r.repeat(5)))
         .insertPath(finder.findPath(grandma.getLayer(), grandma, maps.hero_home.main.grandma_waking.toCoordinates()))
         .wait(2f)
         .turn(LEFT)
         .wait(1f)
         .suspend(() -> morningDialogWithGrandma(grandma))
         .wait(1f)
         .run(() -> controls = true)
         .insertPath(finder.findPath(grandma.getLayer(), maps.hero_home.main.grandma_waking.toCoordinates(), maps.hero_home.main.grandma_origin.toCoordinates()))
         .run(() -> grandma.randomMovementAI(4f, grandma.getCoordinates(), 4));
   }

   private CompletableFuture<Object> morningDialogWithGrandma(Friend grandma) {
      return dialog(grandma, "Hello Honey, wake up, it's another beautiful day!")
         .thenCompose(n -> dialog(player, "Ahhh, good morning Grandma!"))
         .thenCompose(n -> dialog(grandma, "Have a wonderful day, Luna!"))
         .thenCompose(n -> dialog(player, "Thank you Grandma, have a nice day too!"));
   }

   protected CompletableFuture<Object> triggerGrandmaDialog(Friend grandma) {
      return dialog(grandma, "What are you going to do today, Luna?")
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
            player.runEmoji(animations.heart_emoji.$),
            neko.runEmoji(animations.heart_emoji.$, r -> r.offset(0, -15).delay(100))
         ))
         .thenCompose(n -> dialog(neko, "Meow, meow..."))
         .thenCompose(n -> dialog(neko, "Purr, purr..."));
   }
}