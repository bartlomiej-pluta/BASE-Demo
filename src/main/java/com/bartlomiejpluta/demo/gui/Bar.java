package com.bartlomiejpluta.demo.gui;

import lombok.*;

import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.*;

import com.bartlomiejpluta.base.api.screen.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.*;

public class Bar extends BaseComponent {

	@Setter
	private float value = 1.0f;
	private float actualValue = 1.0f;
	private float speed = 0.05f;
	private final Color stroke;
	private final Color fill;

	public Bar(Context context, GUI gui) {
		super(context, gui);

		this.stroke = gui.createColor();
		this.fill = gui.createColor();

		stroke.setAlpha(1f);
		fill.setAlpha(1f);
	}

	public void setStrokeColor(Integer hex) {
		stroke.setRGB(hex);
	}

	public void setFillColor(Integer hex) {
		fill.setRGB(hex);
	}

	@Override
	public float getContentWidth() {
		return width;
	}

	@Override
	public float getContentHeight() {
		return height;
	}

	@Override
	public void draw(Screen screen, GUI gui) {
		var remainingDistance = value - actualValue;
		actualValue += remainingDistance * speed;

		gui.beginPath();
		gui.drawRectangle(x, y, Math.max(width * actualValue, 0), height);
		gui.setFillColor(fill);
		gui.fill();
		gui.closePath();
		gui.beginPath();
		gui.drawRectangle(x, y, width, height);
		gui.setStrokeColor(stroke);
		gui.stroke();
		gui.closePath();
	}
}