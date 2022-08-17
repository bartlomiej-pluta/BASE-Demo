package com.bartlomiejpluta.demo.database.model;

import lombok.*;

@Data
@Builder
public class EnemyModel {
	private final String id;
	private final String name;
	private final String entitySet;
	private final float speed;
	private final float animationSpeed;
	private final boolean blocking;
	private final String meleeWeapon;
}