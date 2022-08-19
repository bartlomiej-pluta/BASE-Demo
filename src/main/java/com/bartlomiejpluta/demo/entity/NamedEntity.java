package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.lib.entity.EntityDelegate;

public abstract class NamedEntity extends EntityDelegate {
	
	public NamedEntity(Entity entity) {
		super(entity);
	}
	
	public abstract String getName();
}