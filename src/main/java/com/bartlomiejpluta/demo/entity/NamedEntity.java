package com.bartlomiejpluta.demo.entity;

import com.bartlomiejpluta.base.api.context.*;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.lib.entity.EntityDelegate;

import com.bartlomiejpluta.demo.runner.DemoRunner;

public abstract class NamedEntity extends EntityDelegate {
	protected final Context context;
	protected final DemoRunner runner;	
	
	public NamedEntity(Entity entity) {
		super(entity);
		this.context = ContextHolder.INSTANCE.getContext();
		this.runner = (DemoRunner) context.getGameRunner();
	}
	
	public abstract String getName();
}