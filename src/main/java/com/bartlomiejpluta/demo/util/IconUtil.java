package com.bartlomiejpluta.demo.util;

import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.icon.Icon;

public class IconUtil {
   public static Icon parseIcon(String iconDefinition) {
      var parts = iconDefinition.split(",");
      return ContextHolder.INSTANCE.getContext().createIcon(A.iconsets.byName(parts[0]).$, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
   }
}
