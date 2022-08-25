package com.bartlomiejpluta.demo.world.weapon;

import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.lib.icon.IconDelegate;
import com.bartlomiejpluta.demo.world.item.ItemStack;
import lombok.Getter;
import lombok.NonNull;

public class Ammunition extends IconDelegate implements ItemStack {
   private int count;

   @Getter
   private final String name;

   public Ammunition(@NonNull String id, int count) {
      this(DB.dao.ammunition.find(id), count);
   }

   public Ammunition(@NonNull DB.model.AmmunitionModel template, int count) {
      super(createIcon(template));
      this.name = template.getName();
      this.count = count;
   }

   @Override
   public int count() {
      return count;
   }

   private static Icon createIcon(DB.model.AmmunitionModel template) {
      var icons = template.getIcon().split(",");
      return ContextHolder.INSTANCE.getContext().createIcon(A.iconsets.get(icons[0]).uid, Integer.parseInt(icons[1]), Integer.parseInt(icons[2]));
   }
}
