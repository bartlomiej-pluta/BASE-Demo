package com.bartlomiejpluta.demo.entity;

import lombok.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.lib.entity.EntityDelegate;

public class Character extends EntityDelegate {
	protected final Context context;
	
	public Character(@NonNull Context context, @NonNull Entity entity) {
		super(entity);
		this.context = context;
	}
}