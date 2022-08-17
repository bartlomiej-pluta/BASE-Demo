package com.bartlomiejpluta.demo.gui;

import lombok.*;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.*;


public class GameMenuWindow extends DecoratedWindow implements Inflatable {

	@Ref("resume_game")
	@Getter
	private Button resumeGameBtn;

	@Ref("start_menu")
	@Getter
	private Button startMenuBtn;

	@Ref("exit")
	@Getter
	private Button exitBtn;

	public GameMenuWindow(Context context, GUI gui) {
		super(context, gui);
	}

	@Override
	public void onInflate() {
		resumeGameBtn.focus();
	}
}