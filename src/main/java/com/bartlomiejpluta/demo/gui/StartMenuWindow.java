package com.bartlomiejpluta.demo.gui;

import lombok.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.*;

public class StartMenuWindow extends DecoratedWindow implements Inflatable {

	@Ref("new_game")
	@Getter
	private Button newGameBtn;

	@Ref("exit")
	@Getter
	private Button exitBtn;

	public StartMenuWindow(Context context, GUI gui) {
		super(context, gui);
	}

	@Override
	public void onInflate() {
		newGameBtn.focus();
	}
}