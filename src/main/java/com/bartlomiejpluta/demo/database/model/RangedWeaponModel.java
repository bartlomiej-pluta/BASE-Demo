package com.bartlomiejpluta.demo.database.model;

import lombok.*;

@Data
@Builder
public class RangedWeaponModel {
	private final String id;
	private final String name;
	private final int cooldown;
	private final String damage;
	private final String animation;
	private final String sound;
	private final String range;
	private final String punchAnimation;
	private final String punchSound;
	private final String missAnimation;
	private final String missSound;
}