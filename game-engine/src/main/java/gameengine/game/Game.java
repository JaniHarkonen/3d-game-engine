package gameengine.game;

import gameengine.engine.ITickable;

public class Game implements ITickable {

	private Scene worldScene;
	
	public Game() {
		this.worldScene = null;
	}
	
	
	public void setup() {
		this.worldScene = new Scene();
		this.worldScene.addObject(new TestObject());
	}
	
	public void tick(float deltaTime) {
		this.worldScene.tick(deltaTime);
	}
	
	public Scene getWorldScene() {
		return this.worldScene;
	}
}
