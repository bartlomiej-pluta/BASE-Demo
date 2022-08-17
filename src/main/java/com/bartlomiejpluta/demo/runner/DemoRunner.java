package com.bartlomiejpluta.demo.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.api.runner.GameRunner;

import com.bartlomiejpluta.demo.map.ForrestTempleHandler;
import com.bartlomiejpluta.demo.entity.Player;

public class DemoRunner implements GameRunner {
   private static final Logger log = LoggerFactory.getLogger(DemoRunner.class);
   private Screen screen;
   private Context context;
   private Player player;

   @Override
   public void init(Context context) {
   	this.context = context;
   	this.screen = context.getScreen();
		
		configureScreen();
   	initPlayer();
   	resetPlayer();
   	newGame();
   	
		screen.show();
   }
   
   private void configureScreen() {
		var resolution = screen.getCurrentResolution();		
		screen.setSize(800, 600);
		screen.setPosition((resolution.x() - 800)/2, (resolution.y() - 600)/2);   
   }
   
   private void initPlayer() {
   	this.player = new Player(context, context.createEntity("815a5c5c-4979-42f5-a42a-ccbbff9a97e5"));
   }
   
   private void resetPlayer() {
   	this.player.changeEntitySet("815a5c5c-4979-42f5-a42a-ccbbff9a97e5");
		this.player.setScale(1.0f);
		this.player.setSpeed(0.07f);
		this.player.setAnimationSpeed(0.005f);	
		this.player.setBlocking(true);	
		this.player.setCoordinates(0, 11);
   }
   
   private void newGame() {
   	resetPlayer();
   	context.openMap(ForrestTempleHandler.UID);
   	context.getMap().getObjectLayer(ForrestTempleHandler.MAIN_LAYER).addEntity(this.player);
   	context.resume();
   }

   @Override
   public void update(float dt) {

   }

   @Override
   public void dispose() {
      // Do something after game loop is end
   }
   
   
}