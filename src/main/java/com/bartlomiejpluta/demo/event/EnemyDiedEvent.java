package com.bartlomiejpluta.demo.event;

import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.lib.event.BaseEvent;
import com.bartlomiejpluta.demo.entity.Enemy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EnemyDiedEvent extends BaseEvent {
   public static final EventType<EnemyDiedEvent> TYPE = new EventType<>("ENEMY_DIED_EVENT");

   private final Enemy enemy;

   @Override
   public EventType<EnemyDiedEvent> getType() {
      return TYPE;
   }
}