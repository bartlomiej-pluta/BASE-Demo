package com.bartlomiejpluta.demo.world.weapon;

import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.world.item.StackableItem;
import com.bartlomiejpluta.demo.world.item.Useable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
public class Ammunition extends StackableItem implements Useable {

   @Getter
   private final String id;

   @Getter
   private final String name;

   @Getter
   private final String appliesTo;

   public Ammunition(@NonNull String id, int count) {
      this(DB.dao.ammunition.find(id), count);
   }

   public Ammunition(@NonNull DB.model.AmmunitionModel template, int count) {
      super(template.getIcon(), count);

      this.id = template.getId();
      this.name = template.getName();
      this.appliesTo = template.getAppliesTo();
   }


   @Override
   public void use(Creature creature) {
      creature.setAmmunition(this);
   }

   @Override
   public String usageName() {
      return "Arm";
   }
}
