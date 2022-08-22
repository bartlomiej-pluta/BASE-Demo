package com.bartlomiejpluta.demo.entity;

import lombok.*;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.context.*;
import com.bartlomiejpluta.base.api.move.*;
import com.bartlomiejpluta.base.util.path.*;

public class MapObject extends NamedCharacter {
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
		super(ContextHolder.INSTANCE.getContext().createCharacter(A.charsets.get(template.getCharset()).uid));
		this.template = template;
		this.frame = template.getFrame();
		this.name = template.getName();
		this.interactSound = A.sounds.get(template.getInteractSound()).uid;

		setBlocking(true);
		disableAnimation();

		if(frame != null) {
			setAnimationFrame(frame);
		}

		pathExecutor.setPath(
			frame != null
			? new CharacterPath<MapObject>()
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
			: new CharacterPath<MapObject>()
		);
	}

	public void interact(Creature creature) {
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