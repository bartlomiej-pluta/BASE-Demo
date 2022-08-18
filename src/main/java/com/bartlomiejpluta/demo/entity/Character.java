package com.bartlomiejpluta.demo.entity;

import lombok.*;
import org.slf4j.*;
import org.joml.Vector2i;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.lib.entity.EntityDelegate;
import com.bartlomiejpluta.base.lib.animation.AnimationRunner;

import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.world.weapon.Weapon;

public abstract class Character extends EntityDelegate {
	private static final Logger log = LoggerFactory.getLogger(Character.class);
	protected final Context context;
	protected final DemoRunner runner;

	protected int attackCooldown = 0;

	@Getter
	protected boolean alive = true;

	@Getter
	protected boolean immortal = false;

	@Getter
	protected int hp;

	@Setter
	private Weapon weapon;

	public Character(@NonNull Context context, @NonNull Entity entity) {
		super(entity);
		this.context = context;
		this.runner = (DemoRunner) context.getGameRunner();
	}

	public void attack() {
		if(weapon == null) {
			return;
		}

		if(attackCooldown >= weapon.getCooldown()) {
			if(weapon.attack(this)) {
				attackCooldown = 0;
			}
		}
	}

	public void hit(int dmg) {
		if(immortal) {
			return;
		}

		log.info(toString() + " received " + dmg + " damage");
		hp -= dmg;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if(weapon != null && attackCooldown < weapon.getCooldown()) {
			attackCooldown += (int) (dt * 1000f);
		}

		if(hp <= 0 && alive && getLayer() != null) {
			alive = false;
			die();
		}
	}

	protected void die() {
		log.info(getName() + " died with HP = " + hp);
	}

	public abstract String getName();
}