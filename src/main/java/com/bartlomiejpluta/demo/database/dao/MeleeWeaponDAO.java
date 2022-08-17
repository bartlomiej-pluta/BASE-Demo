package com.bartlomiejpluta.demo.database.dao;

import java.sql.*;
import java.util.*;

import com.bartlomiejpluta.base.api.context.Context;

import com.bartlomiejpluta.demo.database.model.MeleeWeaponModel;

public class MeleeWeaponDAO {
	private final Map<String, MeleeWeaponModel> items = new HashMap<>();

	public void init(Context context) {
		context.withDatabase(db -> {
			var result = db.prepareStatement("SELECT * FROM `melee_weapon`").executeQuery();

			while(result.next()) {
				var weapon = MeleeWeaponModel.builder()
					.id(result.getString("id"))
					.name(result.getString("name"))
					.damage(result.getString("damage"))
					.cooldown(result.getInt("cooldown"))
					.animation(result.getString("animation"))
					.sound(result.getString("sound"))
					.build();
				items.put(result.getString("id"), weapon);
			}
		});
	}

	public MeleeWeaponModel get(String id) {
		return items.get(id);
	}
}