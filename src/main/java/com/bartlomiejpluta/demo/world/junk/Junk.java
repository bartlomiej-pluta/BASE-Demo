package com.bartlomiejpluta.demo.world.junk;

import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.lib.icon.IconDelegate;
import com.bartlomiejpluta.demo.world.item.Item;
import lombok.Getter;
import lombok.NonNull;

public class Junk extends IconDelegate implements Item {

   @Getter
   private final String name;

   public Junk(@NonNull String id) {
      this(DB.dao.junk.find(id));
   }

   public Junk(@NonNull DB.model.JunkModel template) {
      super(createIcon(template));
      this.name = template.getName();
   }

   private static Icon createIcon(DB.model.JunkModel template) {
      var icons = template.getIcon().split(",");
      return ContextHolder.INSTANCE.getContext().createIcon(A.iconsets.get(icons[0]).uid, Integer.parseInt(icons[1]), Integer.parseInt(icons[2]));
   }
}
