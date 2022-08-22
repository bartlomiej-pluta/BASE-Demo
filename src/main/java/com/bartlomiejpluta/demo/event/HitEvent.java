package com.bartlomiejpluta.demo.event;

import lombok.*;

import com.bartlomiejpluta.base.api.event.*;
import com.bartlomiejpluta.base.lib.event.*;
import com.bartlomiejpluta.demo.entity.Creature;

@Getter
@RequiredArgsConstructor
public class HitEvent extends BaseEvent {
	public static final EventType<HitEvent> TYPE = new EventType<>("HIT_EVENT");

	private final Creature attacker;
	private final Creature target;
	private final int damage;

	@Override
	public EventType<HitEvent> getType() {
		return TYPE;
	}
}