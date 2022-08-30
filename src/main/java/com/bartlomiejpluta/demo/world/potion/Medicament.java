package com.bartlomiejpluta.demo.world.potion;

import DB.dao;
import com.bartlomiejpluta.base.util.random.DiceRoller;
import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.world.item.StackableItem;
import com.bartlomiejpluta.demo.world.item.Useable;
import lombok.Getter;
import lombok.NonNull;

public class Medicament extends StackableItem implements Useable {

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
   }

   @Override
   public void use(Creature creature) {
      if(--count == 0) {
         creature.removeItemFromEquipment(this);
      }

      creature.heal(roller.roll());
   }
}
