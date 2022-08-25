package com.bartlomiejpluta.demo.world.weapon;

import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.lib.icon.IconDelegate;
import com.bartlomiejpluta.demo.entity.Creature;
import com.bartlomiejpluta.demo.world.item.ItemStack;
import com.bartlomiejpluta.demo.world.item.Useable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Ammunition extends IconDelegate implements ItemStack, Useable {

   @Getter
   private final String id;

   @Getter
   @Setter
   private int count;

   @Getter
   private final String name;

   @Getter
   private final String appliesTo;

   public Ammunition(@NonNull String id, int count) {
      this(DB.dao.ammunition.find(id), count);
   }

   public Ammunition(@NonNull DB.model.AmmunitionModel template, int count) {
      super(createIcon(template));
      this.id = template.getId();
      this.name = template.getName();
      this.count = count;
      this.appliesTo = template.getAppliesTo();
   }

   private static Icon createIcon(DB.model.AmmunitionModel template) {
      var icons = template.getIcon().split(",");
      return ContextHolder.INSTANCE.getContext().createIcon(A.iconsets.get(icons[0]).uid, Integer.parseInt(icons[1]), Integer.parseInt(icons[2]));
   }

   @Override
   public void decrease() {
      if (count > 0) {
         --count;
      }
   }

   @Override
   public void increase() {
      ++count;
   }

   @Override
   public void increase(int count) {
      this.count += count;
   }

   @Override
   public void decrease(int count) {
      this.count = Math.max(0, count);
   }

   @Override
   public void use(Creature creature) {
      creature.setAmmunition(this);
   }

   @Override
   public String usageName() {
      return "Equip";
   }
}
