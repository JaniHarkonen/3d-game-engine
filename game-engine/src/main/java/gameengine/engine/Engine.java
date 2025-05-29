package gameengine.engine;

import gameengine.engine.physics.Physics;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.window.Window;
import gameengine.game.Game;

public class Engine {
	
	public static final long SECONDS = 1000000000;
	
	public static final float DEFAULT_TICK_RATE = 60f;
	
	private static Engine INSTANCE;
	
	public static Engine createInstance() {
		if( INSTANCE != null ) {
			throw new RuntimeException("FATAL ERROR: Trying to create more than one engine instance!");
		}
		
		return INSTANCE = new Engine();
	}
	
	public static Window getWindow() {
		return INSTANCE.window;
	}
	
	public static Game getGame() {
		return INSTANCE.game;
	}
	
	public static Renderer getRenderer() {
		return INSTANCE.renderer;
	}
	
	public static Physics getPhysics() {
		return INSTANCE.physics;
	}
	
	public static void requestStop() {
		INSTANCE.willStop = true;
	}
	
	public static void setTickRate(float tickRate) {
		INSTANCE.tickRate = tickRate;
	}
	
	public static void start() {
		INSTANCE.startEngine();
	}

	private boolean willStop;
	private Window window;
	private Game game;
	private Renderer renderer;
	private Physics physics;
	private float tickRate;
	private int tickRateRealized;
	
	public Engine() {
		this.willStop = false;
		this.window = null;
		this.game = null;
		this.renderer = null;
		this.physics = null;
		this.tickRate = DEFAULT_TICK_RATE;
		this.tickRateRealized = 0;
	}
	
	private void startEngine() {
		System.out.println("engine started");
		
		long engineStartTime = System.nanoTime();
		
		this.window = new Window();
		this.window.setup();
		
		this.renderer = new Renderer();
		this.physics = new Physics();
		
		this.game = new Game();
		this.game.setup();
		
		long lastTick = System.nanoTime();
		long lastPoll = System.nanoTime();
		
		while( !this.willStop && !this.window.isClosing() ) {
			float deltaTime = (System.nanoTime() - lastTick) / ((float) SECONDS);
			
			while( deltaTime >= 1 / this.tickRate ) {
				lastTick = System.nanoTime();
				
				this.window.pollEvents();
				this.game.tick(1 / this.tickRate);
				//this.physics.tick();
				this.renderer.render(this.game, this.window);
				this.window.update(deltaTime);
				this.tickRateRealized++;
				deltaTime -= 1 / this.tickRate;
			}
			
			if( System.nanoTime() - lastPoll >= 1 * SECONDS ) {
				this.window.changeTitle("TICK RATE: " + this.tickRateRealized + "|" + this.tickRate);
				this.tickRateRealized = 0;
				lastPoll = System.nanoTime();
			}
		}
		
		this.window.destroy();
		
		System.out.println("engine process terminated");
		System.out.println("ran for " + ((System.nanoTime() - engineStartTime) / SECONDS) + "s");
	}
}
