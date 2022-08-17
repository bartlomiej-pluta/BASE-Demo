package com.bartlomiejpluta.demo.entity;

import lombok.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;

public class Player extends Character {

	public Player(@NonNull Context context, @NonNull Entity entity) {
		super(context, entity);
	}
}