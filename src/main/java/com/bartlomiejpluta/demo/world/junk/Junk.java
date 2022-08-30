package com.bartlomiejpluta.demo.world.junk;

import com.bartlomiejpluta.demo.world.item.BaseItem;
import lombok.Getter;
import lombok.NonNull;

public class Junk extends BaseItem {

   @Getter
   private final String name;

   public Junk(@NonNull String id) {
      this(DB.dao.junk.find(id));
   }

   public Junk(@NonNull DB.model.JunkModel template) {
      super(template.getIcon());
      this.name = template.getName();
   }
}
