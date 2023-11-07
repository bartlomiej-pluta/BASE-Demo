package com.bartlomiejpluta.demo.world.light;

import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.lib.light.LightDelegate;

import java.util.Random;

public class Torch extends LightDelegate {
   private final Random random = new Random();
   private float acc = 0;

   public Torch() {
      super(ContextHolder.INSTANCE.getContext().createLight());
      setIntensity(100f, 50f, 0f);
      setConstantAttenuation(1f);
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      if (acc > 0.1f) {
         setQuadraticAttenuation(0.1f * (1f + ((float) random.nextGaussian()) * 0.1f));
         acc = 0;
      }

      acc += dt;
   }
}
