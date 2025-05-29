package gameengine.game;

import gameengine.engine.IScene;
import gameengine.engine.ITickable;

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
	}
	
	public IScene getWorldScene() {
		return this.worldScene;
	}
}
