package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.*;

public abstract class DecoratedWindow extends BaseWindow {
	private Paint paint;
	private Color inner;
	private Color outer;

	public DecoratedWindow(Context context, GUI gui) {
		super(context, gui);

		this.inner = gui.createColor();
		this.outer = gui.createColor();
		this.paint = gui.createPaint();

		inner.setRGBA(0.1f, 0.1f, 0.1f, 1f);
		outer.setRGBA(0.2f, 0.2f, 0.2f, 1f);
	}

	@Override
	public void draw(Screen screen, GUI gui) {
		gui.beginPath();
		gui.drawRectangle(x, y, getWidth(), getHeight());
		gui.setFillPaint(paint);
		gui.boxGradient(x, y, getWidth(), getHeight(), 10f, 100f, inner, outer, paint);
		gui.fill();
		gui.stroke();

		super.draw(screen, gui);
	}
}