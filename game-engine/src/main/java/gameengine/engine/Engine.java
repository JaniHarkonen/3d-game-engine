package gameengine.engine;

import gameengine.engine.physics.Physics;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.window.Window;
import gameengine.game.Game;
import gameengine.logger.Logger;

public class Engine {
	
	public static final long SECONDS = 1000000000;
	
	public static final float DEFAULT_TICK_RATE = 60f;
	
	public static final String SYSTEM_WINDOW = "GLFW";
	
	public static final String SYSTEM_RENDERER = "OpenGL";
	
	public static final String SYSTEM_GAME = "Game";
	
	public static final String SYSTEM_PHYSICS = "Physics";
	
	public static final String SYSTEM_ENGINE = "Engine";
	
	private static Engine instance;
	
	public static void init() {
		if( instance != null ) {
			Logger.fatal("Engine.init()", "Trying to create more than one engine instance!");
		}
		
		instance = new Engine();
	}
	
	public static Window getWindow() {
		return instance.window;
	}
	
	public static Game getGame() {
		return instance.game;
	}
	
	public static Renderer getRenderer() {
		return instance.renderer;
	}
	
	public static Physics getPhysics() {
		return instance.physics;
	}
	
	public static void requestStop() {
		instance.willStop = true;
	}
	
	public static void setTickRate(float tickRate) {
		instance.tickRate = tickRate;
	}
	
	public static void start() {
		instance.startEngine();
	}

	private boolean willStop;
	private Window window;
	private Renderer renderer;
	private Game game;
	private Physics physics;
	private float tickRate;
	private int tickRateRealized;
	
	public Engine() {
		this.willStop = false;
		this.window = null;
		this.renderer = null;
		this.game = null;
		this.physics = null;
		this.tickRate = DEFAULT_TICK_RATE;
		this.tickRateRealized = 0;
	}
	
	
	private void startEngine() {
		Logger.setSystem(SYSTEM_ENGINE);
		Logger.info(this, "Engine started...");
		
		long engineStartTime = System.nanoTime();
		
		this.window = new Window()
		.setDimensions(1280, 720);
		Logger.setSystem(SYSTEM_WINDOW);
		this.window.setup();
		
		this.renderer = new Renderer();
		Logger.setSystem(SYSTEM_RENDERER);
		this.renderer.setup();
		
		this.physics = new Physics();
		Logger.setSystem(SYSTEM_PHYSICS);

		this.game = new Game();
		Logger.setSystem(SYSTEM_GAME);
		this.game.setup();
		
		long lastTick = System.nanoTime();
		long lastPoll = System.nanoTime();
		
		while( !this.willStop && !this.window.isClosing() ) {
			float deltaTime = (System.nanoTime() - lastTick) / ((float) SECONDS);
			
			if( deltaTime >= 1 / this.tickRate ) {
				while( deltaTime >= 1 / this.tickRate ) {
					lastTick = System.nanoTime();
					
					Logger.setSystem(SYSTEM_WINDOW);
					this.window.pollEvents();
					
					Logger.setSystem(SYSTEM_GAME);
					this.game.tick(1 / this.tickRate);
					
					Logger.setSystem(SYSTEM_PHYSICS);
					this.physics.tick(1 / this.tickRate);
					
					Logger.setSystem(SYSTEM_WINDOW);
					this.window.update(deltaTime);
					this.tickRateRealized++;
					deltaTime -= 1 / this.tickRate;
				}
				
					// Only render once all ticks have been processed
				Logger.setSystem(SYSTEM_RENDERER);
				this.renderer.render(this.game, this.window);
			}
			
				// Debug functionality, to be removed
			if( System.nanoTime() - lastPoll >= 1 * SECONDS ) {
				Logger.setSystem(SYSTEM_ENGINE);
				this.window.changeTitle("TICK RATE: " + this.tickRateRealized + "|" + this.tickRate);
				this.tickRateRealized = 0;
				lastPoll = System.nanoTime();
			}
		}
		
		Logger.setSystem(SYSTEM_ENGINE);
		
		this.window.dispose();
		
		Logger.debug(this, "engine process terminated");
		Logger.debug(this, "ran for " + ((System.nanoTime() - engineStartTime) / SECONDS) + "s");
	}
}
