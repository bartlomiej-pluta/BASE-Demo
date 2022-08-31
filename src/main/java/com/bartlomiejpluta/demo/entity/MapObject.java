package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.util.path.CharacterPath;
import com.bartlomiejpluta.base.util.path.PathExecutor;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class MapObject extends NamedCharacter {
   private static final float INTERVAL = 0.05f;
   private static final float COOLDOWN = 0.5f;
   protected final PathExecutor<MapObject> pathExecutor = new PathExecutor<>(this);
   private final String interactSound;

   protected boolean interacting = false;

   private Supplier<CompletableFuture<?>> beforeAll;
   private Supplier<CompletableFuture<?>> before;
   private Supplier<CompletableFuture<?>> after;
   private Supplier<CompletableFuture<?>> afterAll;

   private CompletableFuture<?> future;
   @Getter
   private final String name;


   public MapObject(@NonNull String id) {
      this(DB.dao.map_object.find(id));
   }

   public MapObject(@NonNull DB.model.MapObjectModel template) {
      super(ContextHolder.INSTANCE.getContext().createCharacter(A.charsets.get(template.getCharset()).uid));
      short frame = requireNonNull(template.getFrame());
      this.name = template.getName();
      this.interactSound = A.sounds.get(template.getInteractSound()).uid;

      setBlocking(true);
      disableAnimation();

      setAnimationFrame(frame);
      pathExecutor.setRepeat(1);

      pathExecutor.setPath(new CharacterPath<MapObject>()
              .run(this::startInteraction)
              .turn(Direction.LEFT, frame)
              .wait(INTERVAL)
              .turn(Direction.RIGHT, frame)
              .wait(INTERVAL)
              .turn(Direction.UP, frame)
              .wait(INTERVAL)
              .run(this::runInteraction)
              .suspend(() -> future)
              .wait(INTERVAL)
              .turn(Direction.RIGHT, frame)
              .wait(INTERVAL)
              .turn(Direction.LEFT, frame)
              .wait(INTERVAL)
              .turn(Direction.DOWN, frame)
              .run(this::finishInteraction)
              .suspend(() -> future)
              .wait(COOLDOWN)
              .run(this::completeInteraction)
      );
   }

   public MapObject beforeAll(@NonNull Supplier<CompletableFuture<?>> action) {
      this.beforeAll = action;
      return this;
   }

   public MapObject before(@NonNull Supplier<CompletableFuture<?>> action) {
      this.before = action;
      return this;
   }

   public MapObject after(@NonNull Supplier<CompletableFuture<?>> action) {
      this.after = action;
      return this;
   }

   public MapObject afterAll(@NonNull Supplier<CompletableFuture<?>> action) {
      this.afterAll = action;
      return this;
   }

   public void triggerInteraction() {
      if (interacting) {
         return;
      }

      pathExecutor.reset();

      if (beforeAll != null) {
         beforeAll.get().thenRun(() -> interacting = true);
      } else {
         interacting = true;
      }
   }

   private void runInteraction() {
      this.future = (before != null ? before.get() : completedFuture(null))
              .thenCompose(v -> interact())
              .thenCompose(v -> (after != null ? after.get() : completedFuture(null)));
   }

   private void startInteraction() {
      if (interactSound != null) {
         context.playSound(interactSound);
      }
   }

   private void finishInteraction() {
      this.future = afterAll != null ? afterAll.get() : completedFuture(null);
   }

   private void completeInteraction() {
      interacting = false;
   }

   @Override
   public void update(float dt) {
      if (interacting) {
         pathExecutor.execute(getLayer(), dt);
      }
   }

   protected final void reset() {
      setFaceDirection(Direction.DOWN);
      pathExecutor.reset();
      interacting = false;
   }

   protected abstract CompletableFuture<?> interact();
}