package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.move.MoveEvent;
import com.bartlomiejpluta.base.lib.ai.NoopAI;
import com.bartlomiejpluta.base.lib.animation.AnimationRunner;
import com.bartlomiejpluta.base.lib.animation.SimpleAnimationRunner;
import com.bartlomiejpluta.base.util.random.DiceRoller;
import com.bartlomiejpluta.demo.ai.*;
import com.bartlomiejpluta.demo.event.EnemyDiedEvent;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.util.LootGenerator;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.weapon.Ammunition;
import com.bartlomiejpluta.demo.world.weapon.MeleeWeapon;
import com.bartlomiejpluta.demo.world.weapon.RangedWeapon;
import com.bartlomiejpluta.demo.world.weapon.ThrowingWeapon;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Random;


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
   @Getter
   @Setter
   private ThrowingWeapon throwingWeapon;

   public Enemy(@NonNull String id) {
      this(DB.dao.enemy.find(id));
   }

   public Enemy(@NonNull DB.model.EnemyModel template) {
      super(ContextHolder.INSTANCE.getContext().createCharacter(A.charsets.byName(template.getCharset()).$));
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
      var throwingWeaponTemplate = template.getThrowingWeapon();

      if (meleeWeaponTemplate != null) {
         this.meleeWeapon = new MeleeWeapon(meleeWeaponTemplate);
      }

      if (rangedWeaponTemplate != null) {
         var split = rangedWeaponTemplate.split(",");

         this.rangedWeapon = new RangedWeapon(split[0]);
         setAmmunition(new Ammunition(split[1], DiceRoller.roll(split[2])));
      }

      if (throwingWeaponTemplate != null) {
         var split = throwingWeaponTemplate.split(",");
         this.throwingWeapon = new ThrowingWeapon(split[0], DiceRoller.roll(split[1]));
      }

      this.dieAnimation = new SimpleAnimationRunner(A.animations.byName(template.getDieAnimation()).$);
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
      changeCharacterSet(A.charsets.byName(template.getDeadCharset()).$);
      setScale(0.5f);
      setBlocking(false);
      setZIndex(-1);

      ai = NoopAI.INSTANCE;

      dieAnimation.run(context, getLayer(), this);
      context.playSound(A.sounds.byName(template.getDieSound()).$);
      context.fireEvent(new EnemyDiedEvent(this));

      LootGenerator.generate(template.getId(), loot);
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
      var ai = new WeaponBasedAI(this, DemoRunner.instance().getPlayer());

      addEventListener(MoveEvent.TYPE, ai::recomputePath);
      addEventListener(EnemyDiedEvent.TYPE, e -> ai.recomputePath());

      this.ai = ai;

      return this;
   }
}