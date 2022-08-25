package com.bartlomiejpluta.demo.util;

import lombok.AllArgsConstructor;

import java.util.LinkedList;

@AllArgsConstructor
public class LimitedQueue<E> extends LinkedList<E> {
   private int limit;

   @Override
   public boolean add(E o) {
      super.add(o);

      while (size() > limit) {
         super.remove();
      }

      return true;
   }
}