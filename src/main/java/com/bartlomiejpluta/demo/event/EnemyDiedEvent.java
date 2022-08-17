package com.bartlomiejpluta.demo.event;

import lombok.*;

import com.bartlomiejpluta.base.api.event.*;
import com.bartlomiejpluta.base.lib.event.*;
import com.bartlomiejpluta.demo.entity.Enemy;

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