package com.bartlomiejpluta.demo.event;

import lombok.*;

import com.bartlomiejpluta.base.api.event.*;
import com.bartlomiejpluta.base.lib.event.*;
import com.bartlomiejpluta.demo.entity.Character;

@Getter
@RequiredArgsConstructor
public class HitEvent extends BaseEvent {
	public static final EventType<HitEvent> TYPE = new EventType<>("HIT_EVENT");

	private final Character attacker;
	private final Character target;
	private final int damage;

	@Override
	public EventType<HitEvent> getType() {
		return TYPE;
	}
}