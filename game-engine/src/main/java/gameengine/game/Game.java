package gameengine.game;

import gameengine.engine.Engine;
import gameengine.engine.IScene;
import gameengine.engine.ITickable;
import gameengine.engine.window.Window;

public class Game implements ITickable {

	private Scene worldScene;
	
	public Game() {
		this.worldScene = null;
	}
	
	
	public void setup() {
		this.worldScene = new Scene();
		this.worldScene.addObject(new TestAnother());
	}
	
	public void tick(float deltaTime) {
		this.worldScene.tick(deltaTime);
		
		Window window = Engine.getInstance().getWindow();
		window.getInput().DEBUGmapInput(11306, (e) -> {
			window.enableCursor(!window.isCursorEnabled());
		});
	}
	
	public IScene getWorldScene() {
		return this.worldScene;
	}
}
