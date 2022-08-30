package com.bartlomiejpluta.demo.world.item;

import com.bartlomiejpluta.base.lib.icon.IconDelegate;
import com.bartlomiejpluta.demo.util.IconUtil;
import lombok.NonNull;

public abstract class BaseItem extends IconDelegate implements Item {
   protected BaseItem(@NonNull String icon) {
      super(IconUtil.parseIcon(icon));
      setZIndex(-1);
   }
}
