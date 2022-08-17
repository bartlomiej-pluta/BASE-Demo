package com.bartlomiejpluta.demo.entity;

import lombok.*;
import org.slf4j.*;
import org.joml.Vector2i;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.lib.entity.EntityDelegate;

import com.bartlomiejpluta.demo.world.weapon.MeleeWeapon;

public class Character extends EntityDelegate {
	private static final Logger log = LoggerFactory.getLogger(Character.class);
	protected final Context context;
	protected int attackCooldown = 0;

	@Setter
	private MeleeWeapon weapon;
	
	public Character(@NonNull Context context, @NonNull Entity entity) {
		super(entity);
		this.context = context;
	}

	public void attack() {
		if(weapon == null) {
			return;
		}

		if(attackCooldown >= weapon.getCooldown()) {
			var facingNeighbour = getCoordinates().add(getFaceDirection().vector, new Vector2i());
			for(var entity : getLayer().getEntities()) {
				if(entity.getCoordinates().equals(facingNeighbour) && entity.isBlocking() && entity instanceof Character) {
					weapon.attack((Character) entity);
					attackCooldown = 0;
				}
			}
		}
	}

	public void hit(int dmg) {
		log.info(toString() + " received " + dmg + " damage");
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if(weapon != null && attackCooldown < weapon.getCooldown()) {
			attackCooldown += (int) (dt * 1000f);
		}
	}
}