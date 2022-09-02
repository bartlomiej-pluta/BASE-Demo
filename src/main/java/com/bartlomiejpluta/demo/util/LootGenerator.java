package com.bartlomiejpluta.demo.util;

import DB.EnemyDropDAO;
import DB.dao;
import com.bartlomiejpluta.base.lib.db.Relop;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.item.ItemStack;
import com.bartlomiejpluta.demo.world.junk.Junk;
import com.bartlomiejpluta.demo.world.potion.Medicament;
import com.bartlomiejpluta.demo.world.weapon.Ammunition;
import com.bartlomiejpluta.demo.world.weapon.MeleeWeapon;
import com.bartlomiejpluta.demo.world.weapon.RangedWeapon;
import com.bartlomiejpluta.demo.world.weapon.ThrowingWeapon;
import lombok.NonNull;

import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.bartlomiejpluta.base.util.random.DiceRoller.roll;
import static com.bartlomiejpluta.demo.util.ListUtil.randomIntSequence;

public class LootGenerator {
   private static final Random rand = new Random();
   private static final Map<String, Function<String, Item>> itemGenerators = Map.of(
           "melee", MeleeWeapon::new,
           "ranged", RangedWeapon::new,
           "junk", Junk::new
   );
   private static final Map<String, BiFunction<String, Integer, ItemStack>> stackableGenerator = Map.of(
           "ammo", Ammunition::new,
           "throwing", ThrowingWeapon::new,
           "med", Medicament::new
   );

   public static void generate(@NonNull String enemy, @NonNull Item[] loot) {
      var def = dao.enemy_drop.query()
              .where(EnemyDropDAO.Column.ENEMY, Relop.EQ, enemy)
              .orderBy("rand()")
              .find();

      var index = randomIntSequence(0, loot.length).iterator();

      for (var template : def) {
         var parts = template.getItem().split(":");

         var item = itemGenerators.get(parts[0]);
         if (item != null) {
            for (int i = 0; i < roll(template.getAmount()) && index.hasNext(); ++i) {
               if (rand.nextFloat() <= template.getChance()) {
                  loot[index.next()] = item.apply(parts[1]);
               }
            }

            continue;
         }

         var stack = stackableGenerator.get(parts[0]);
         if (stack != null && index.hasNext()) {
            if (rand.nextFloat() <= template.getChance()) {
               loot[index.next()] = stack.apply(parts[1], roll(template.getAmount()));
            }
         }
      }
   }
}
