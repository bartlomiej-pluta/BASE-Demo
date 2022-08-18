package com.bartlomiejpluta.demo.database.dao;

import java.sql.*;
import java.util.*;

import com.bartlomiejpluta.base.api.context.Context;

import com.bartlomiejpluta.demo.database.model.RangedWeaponModel;

public class RangedWeaponDAO {
	private final Map<String, RangedWeaponModel> items = new HashMap<>();

	public void init(Context context) {
		context.withDatabase(db -> {
			var result = db.prepareStatement("SELECT * FROM `ranged_weapon`").executeQuery();

			while(result.next()) {
				var weapon = RangedWeaponModel.builder()
					.id(result.getString("id"))
					.name(result.getString("name"))
					.damage(result.getString("damage"))
					.cooldown(result.getInt("cooldown"))
					.animation(result.getString("animation"))
					.sound(result.getString("sound"))
					.range(result.getString("range"))
					.punchAnimation(result.getString("punch_animation"))
					.punchSound(result.getString("punch_sound"))
					.missAnimation(result.getString("miss_animation"))
					.missSound(result.getString("miss_sound"))
					.build();
				items.put(result.getString("id"), weapon);
			}
		});
	}

	public RangedWeaponModel get(String id) {
		return items.get(id);
	}
}