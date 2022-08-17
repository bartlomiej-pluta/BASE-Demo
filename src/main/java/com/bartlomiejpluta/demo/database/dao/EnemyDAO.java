package com.bartlomiejpluta.demo.database.dao;

import java.sql.*;
import java.util.*;

import com.bartlomiejpluta.base.api.context.Context;

import com.bartlomiejpluta.demo.database.model.EnemyModel;

public class EnemyDAO {
	private final Map<String, EnemyModel> enemies = new HashMap<>();

	public void init(Context context) {
		context.withDatabase(db -> {
			var result = db.prepareStatement("SELECT * FROM `enemy`").executeQuery();

			while(result.next()) {
				var enemy = EnemyModel.builder()
					.id(result.getString("id"))
					.name(result.getString("name"))
					.entitySet(result.getString("entset"))
					.deadEntitySet(result.getString("dead_entset"))
					.hp(result.getInt("hp"))
					.speed(result.getFloat("speed"))
					.animationSpeed(result.getFloat("animation_speed"))
					.blocking(result.getBoolean("blocking"))
					.meleeWeapon(result.getString("melee_weapon"))
					.build();
				enemies.put(result.getString("id"), enemy);
			}
		});
	}

	public EnemyModel get(String id) {
		return enemies.get(id);
	}
}