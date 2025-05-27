package gameengine.engine;

import gameengine.engine.physics.Physics;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.window.Window;
import gameengine.game.Game;

public class Engine {
	
	public static final long SECONDS = 1000000000;
	
	private static Engine INSTANCE;
	
	public static Engine createInstance() {
		if( INSTANCE != null ) {
			
		}
		
		return INSTANCE = new Engine();
	}
	

	private boolean willStop;
	private Window window;
	private Game game;
	private Renderer renderer;
	private Physics physics;
	
	public Engine() {
		this.willStop = false;
		this.window = null;
		this.game = null;
		this.renderer = null;
		this.physics = null;
	}
	
	public void start() {
		System.out.println("engine started");
		
		long engineStartTime = System.nanoTime();
		
		this.window = new Window();
		this.window.setup();
		
		this.renderer = new Renderer();
		this.physics = new Physics();
		
		this.game = new Game();
		this.game.setup();
		
		while( !this.willStop && !this.window.isClosing() ) {
			this.window.pollEvents();
			//this.game.tick();
			//this.physics.tick();
			this.renderer.render(this.game, this.window);
			this.window.update();
		}
		
		this.window.destroy();
		
		System.out.println("engine process terminated");
		System.out.println("ran for " + ((System.nanoTime() - engineStartTime) / SECONDS) + "s");
	}
	
	public void requestStop() {
		this.willStop = true;
	}
}
