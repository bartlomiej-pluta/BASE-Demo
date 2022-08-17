package com.bartlomiejpluta.demo.database.model;

import lombok.*;

@Data
@Builder
public class EnemyModel {
	private final String id;
	private final String name;
	private final String entitySet;
	private final String deadEntitySet;
	private final int hp;
	private final float speed;
	private final float animationSpeed;
	private final boolean blocking;
	private final String meleeWeapon;
	private final String dieAnimation;
	private final String dieSound;
}