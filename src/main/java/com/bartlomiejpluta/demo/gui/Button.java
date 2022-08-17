package com.bartlomiejpluta.demo.gui;

import lombok.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.api.input.*;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.*;

public class Button extends Label {
	private Color color;

	@Setter
	private Runnable action;

	public Button(Context context, GUI gui) {
		super(context, gui);
		this.color = gui.createColor();
		this.color.setRGBA(1, 1, 1, 0);

		setText("");
		setFontSize(17f);
		setAlignment(GUI.ALIGN_TOP | GUI.ALIGN_CENTER);
		setColor(0.4f, 0.7f, 0.0f, 1f);
		setPadding(10f);
		addEventListener(KeyEvent.TYPE, this::handleKeyEvent);
	}

	private void handleKeyEvent(KeyEvent event) {
		if(event.getKey() == Key.KEY_ENTER && event.getAction() == KeyAction.PRESS && action != null) {
			event.consume();
			action.run();
		}
	}

	@Override
	public void draw(Screen screen, GUI gui) {
		color.setAlpha(focused ? 0.7f : 0f);

		gui.beginPath();
		gui.drawRectangle(x, y, getWidth(), getHeight());
		gui.setFillColor(color);
		gui.fill();

		super.draw(screen, gui);
	}
}