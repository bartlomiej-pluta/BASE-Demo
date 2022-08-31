package com.bartlomiejpluta.demo.world.potion;

import DB.dao;
import com.bartlomiejpluta.base.util.random.DiceRoller;
import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.world.item.Useable;
import com.bartlomiejpluta.demo.world.item.UseableStackableItem;
import lombok.Getter;
import lombok.NonNull;

import static java.lang.String.format;

public class Medicament extends UseableStackableItem implements Useable {
   @Getter
   private final String id;

   @Getter
   private final String name;

   @Getter
   private final DiceRoller roller;

   public Medicament(@NonNull String id, int count) {
      this(dao.medicaments.find(id), count);
   }

   public Medicament(@NonNull DB.model.MedicamentsModel template, int count) {
      super(template.getIcon(), count);
      this.name = template.getName();
      this.roller = DiceRoller.of(template.getHp());
      this.id = format("med:%s", template.getId());
   }

   @Override
   public void use(Creature creature) {
      super.use(creature);
      creature.heal(roller.roll());
   }
}
