package com.bartlomiejpluta.demo.database.model;

import lombok.*;

@Data
@Builder
public class MeleeWeaponModel {
	private final String id;
	private final String name;
	private final String damage;
	private final int cooldown;
}