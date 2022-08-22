package com.bartlomiejpluta.demo.entity;

import lombok.*;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.context.*;
import com.bartlomiejpluta.base.api.move.*;
import com.bartlomiejpluta.base.lib.entity.EntityDelegate;
import com.bartlomiejpluta.base.util.path.*;

import com.bartlomiejpluta.demo.entity.Character;

public class MapObject extends NamedEntity {
	private final PathExecutor<MapObject> pathExecutor = new PathExecutor<>(this);
	private final DB.model.MapObjectModel template;
	private final Short frame;
	private final String interactSound;
	
	@Getter
	private final String name;
	
	private boolean interacting = false;

	public MapObject(@NonNull String id) {
		this(DB.dao.map_object.find(id));
	}

	public MapObject(@NonNull DB.model.MapObjectModel template) {
		super(ContextHolder.INSTANCE.getContext().createEntity(template.getEntset()));
		this.template = template;
		this.frame = template.getFrame();
		this.name = template.getName();
		this.interactSound = template.getInteractSound();

		setBlocking(true);
		disableAnimation();

		if(frame != null) {
			setAnimationFrame(frame);
		}

		pathExecutor.setPath(
			frame != null
			? new EntityPath<MapObject>()
				.run(this::startInteraction)
				.turn(Direction.LEFT, frame)
				.wait(0.05f)
				.turn(Direction.RIGHT, frame)
				.wait(0.05f)
				.turn(Direction.UP, frame)
				.wait(0.5f)
				.turn(Direction.RIGHT, frame)
				.wait(0.05f)
				.turn(Direction.LEFT, frame)
				.wait(0.05f)
				.turn(Direction.DOWN, frame)
				.wait(0.5f)
				.run(this::finishInteraction)
			: new EntityPath<MapObject>()
		);
	}

	public void interact(Character character) {
		interacting = true;
	}

	private void startInteraction() {
		if(interactSound != null) {
			context.playSound(interactSound);
		}
	}

	private void finishInteraction() {
		interacting = false;
	}

	@Override
	public void update(float dt) {
		if(interacting) {
			pathExecutor.execute(getLayer(), dt);
		}
	}
}