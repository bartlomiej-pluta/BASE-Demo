package com.bartlomiejpluta.demo.entity;

import DB.EnemyDropDAO;
import DB.dao;
import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.move.MoveEvent;
import com.bartlomiejpluta.base.lib.ai.NoopAI;
import com.bartlomiejpluta.base.lib.animation.AnimationRunner;
import com.bartlomiejpluta.base.lib.animation.SimpleAnimationRunner;
import com.bartlomiejpluta.base.lib.db.Relop;
import com.bartlomiejpluta.base.util.random.DiceRoller;
import com.bartlomiejpluta.demo.ai.*;
import com.bartlomiejpluta.demo.event.EnemyDiedEvent;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.junk.Junk;
import com.bartlomiejpluta.demo.world.weapon.Ammunition;
import com.bartlomiejpluta.demo.world.weapon.MeleeWeapon;
import com.bartlomiejpluta.demo.world.weapon.RangedWeapon;
import lombok.Getter;
import lombok.NonNull;

import java.util.Random;

import static com.bartlomiejpluta.demo.util.ListUtil.randomIntSequence;


public class Enemy extends Creature implements NPC {
   public static final int MAX_LOOT = 4 * 4;

   private final Random random = new Random();
   private final DB.model.EnemyModel template;
   private final AnimationRunner dieAnimation;

   @Getter
   private final Item[] loot = new Item[MAX_LOOT];
   @Getter
   private final String name;
   private AI ai = NoopAI.INSTANCE;
   @Getter
   private MeleeWeapon meleeWeapon;
   @Getter
   private RangedWeapon rangedWeapon;

   public Enemy(@NonNull String id) {
      this(DB.dao.enemy.find(id));
   }

   public Enemy(@NonNull DB.model.EnemyModel template) {
      super(ContextHolder.INSTANCE.getContext().createCharacter(A.charsets.get(template.getCharset()).uid));
      this.template = template;
      name = template.getName();
      maxHp = DiceRoller.roll(template.getHp());
      hp = maxHp;
      var speed = DiceRoller.roll(template.getSpeed()) / 10f;
      setSpeed(speed);
      setAnimationSpeed(speed / 2.0f);
      setBlocking(template.isBlocking());
      var meleeWeaponTemplate = template.getMeleeWeapon();
      var rangedWeaponTemplate = template.getRangedWeapon();

      if (meleeWeaponTemplate != null) {
         this.meleeWeapon = new MeleeWeapon(meleeWeaponTemplate);
      }

      if (rangedWeaponTemplate != null) {
         var split = rangedWeaponTemplate.split(",");

         this.rangedWeapon = new RangedWeapon(split[0]);
         setAmmunition(new Ammunition(split[1], DiceRoller.roll(split[2])));
      }

      this.dieAnimation = new SimpleAnimationRunner(A.animations.get(template.getDieAnimation()).uid);
   }

   @Override
   public AI getStrategy() {
      return ai;
   }

   @Override
   public void removeItemFromEquipment(Item item) {
      // noop
   }

   @Override
   public void die() {
      super.die();
      changeCharacterSet(A.charsets.get(template.getDeadCharset()).uid);
      setScale(0.5f);
      setBlocking(false);
      setZIndex(-1);

      ai = NoopAI.INSTANCE;

      dieAnimation.run(context, getLayer(), this);
      context.playSound(A.sounds.get(template.getDieSound()).uid);
      context.fireEvent(new EnemyDiedEvent(this));

      drawLoot();
   }

   private void drawLoot() {
      var def = dao.enemy_drop.query()
              .where(EnemyDropDAO.Column.ENEMY, Relop.EQ, template.getId())
              .orderBy("rand()")
              .find();

      var indexesIterator = randomIntSequence(0, loot.length).iterator();
      for (var d : def) {
         if (!indexesIterator.hasNext()) {
            break;
         }

         var index = indexesIterator.next();

         var split = d.getItem().split(":");
         loot[index] = switch (split[0]) {
            case "melee_weapon" -> new MeleeWeapon(split[1]);
            case "ranged_weapon" -> new RangedWeapon(split[1]);
            case "ammo" -> new Ammunition(split[1], DiceRoller.roll(d.getAmount()));
            case "junk" -> new Junk(split[1]);
            default -> throw new IllegalArgumentException("Unsupported item type");
         };
      }
   }


   public Enemy followAndAttack(Creature target, int range) {
      var ai = new SimpleEnemyAI(this, target, range);

      addEventListener(MoveEvent.TYPE, ai::recomputePath);
      addEventListener(EnemyDiedEvent.TYPE, e -> ai.recomputePath());

      this.ai = ai;

      return this;
   }

   public Enemy campAndHunt(Creature target, int range) {
      this.ai = new SimpleSniperAI(this, target, range);

      return this;
   }

   public Enemy asAnimal(Creature source, int range) {
      this.ai = new AnimalAI(this, source, range);

      return this;
   }

   public Enemy archer(Creature target, int minRange, int maxRange, int range) {
      var ai = new ArcherAI(this, target, minRange, maxRange, range);

      addEventListener(MoveEvent.TYPE, ai::recomputePath);
      addEventListener(EnemyDiedEvent.TYPE, e -> ai.recomputePath());

      this.ai = ai;

      return this;
   }

   public Enemy defaultAI() {
      var ai = new WeaponBasedAI(this, context.getGlobal("player", Player.class));

      addEventListener(MoveEvent.TYPE, ai::recomputePath);
      addEventListener(EnemyDiedEvent.TYPE, e -> ai.recomputePath());

      this.ai = ai;

      return this;
   }
}