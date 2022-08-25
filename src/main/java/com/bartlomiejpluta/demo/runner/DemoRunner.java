package com.bartlomiejpluta.demo.runner;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.runner.GameRunner;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.util.profiler.FPSProfiler;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.menu.MenuManager;
import com.bartlomiejpluta.demo.world.weapon.RangedWeapon;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DemoRunner implements GameRunner {
    private static final Logger log = LoggerFactory.getLogger(DemoRunner.class);
    private Screen screen;
    private Context context;
    private MenuManager menu;
    private GUI hud;

    @Getter
    private Player player;

    private final FPSProfiler fpsProfiler = FPSProfiler.create(20);

    @Override
    public void init(Context context) {
        this.context = context;
        this.screen = context.getScreen();

        configureScreen();
        configureCamera();
        initPlayer();
        initHUD();
        initMenu();

        menu.showStartMenu();

        screen.show();
    }

    private void configureScreen() {
        var resolution = screen.getCurrentResolution();
        screen.setSize(1800, 1000);
        screen.setPosition((resolution.x() - 1800) / 2, (resolution.y() - 1000) / 2);
    }

    private void configureCamera() {
        context.getCamera().setScale(2f);
    }

    private void initMenu() {
        this.menu = new MenuManager(this, context);
    }
    
    public int openedWindows() {
    	return this.menu.openedWindows();
    }

    private void initHUD() {
        hud = context.newGUI();
        hud.hide();
        var hudComponent = hud.inflateComponent(A.widgets.hud.uid);
        hud.setRoot(hudComponent);
    }

    private void initPlayer() {
        this.player = new Player(context.createCharacter(A.charsets.luna.uid));
    }

    private void resetPlayer() {
        this.player.changeCharacterSet(A.charsets.luna.uid);
        this.player.setScale(1f);
        this.player.setSpeed(4f);
        this.player.setAnimationSpeed(1f);
        this.player.setBlocking(true);
        this.player.setWeapon(new RangedWeapon("wooden_bow"));
    }

    public void newGame() {
        menu.closeAll();
        menu.enableGameMenu();
        resetPlayer();
        context.openMap(A.maps.forrest.uid);
        context.getMap().getObjectLayer(A.maps.forrest.layers.main).addEntity(this.player);
        player.setCoordinates(5, 36);
        context.resume();
        hud.show();
    }

    public void returnToStartMenu() {
        menu.closeAll();
        hud.hide();
        context.pause();
        context.closeMap();
        menu.disableGameMenu();
        menu.showStartMenu();
    }

    public void exit() {
        context.close();
    }

    public double instantFPS() {
        return fpsProfiler.getInstantFPS();
    }

    @Override
    public void update(float dt) {
        fpsProfiler.update(dt);
    }

    @Override
    public void dispose() {
        // Do something after game loop is end
    }
}