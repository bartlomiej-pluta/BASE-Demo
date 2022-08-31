package com.bartlomiejpluta.demo.world.item;

public interface ItemStack extends Item {
   String getId();

   int getCount();

   void setCount(int count);

   void decrease();

   void increase();

   void increase(int count);

   void decrease(int count);
}
