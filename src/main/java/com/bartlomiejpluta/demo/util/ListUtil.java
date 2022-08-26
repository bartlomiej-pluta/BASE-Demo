package com.bartlomiejpluta.demo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class ListUtil {
   private static final Random random = new Random();

   public static <T> T sample(List<T> list) {
      return list.get(random.nextInt(list.size()));
   }

   public static List<Integer> randomIntSequence(int startInclusive, int endExclusive) {
      var ints = new ArrayList<>(IntStream.range(startInclusive, endExclusive).boxed().toList());
      Collections.shuffle(ints);
      return ints;
   }
}
