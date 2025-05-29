package gameengine.game;

import gameengine.engine.Engine;
import gameengine.engine.IScene;
import gameengine.engine.ITickable;
import gameengine.engine.asset.AssetManager;
import gameengine.engine.asset.texture.Texture;
import gameengine.engine.window.Window;

public class Game implements ITickable {

	private AssetManager assetManager;
	private Scene worldScene;
	
	public Game() {
		this.assetManager = null;
		this.worldScene = null;
	}
	
	
	public void setup() {
		this.assetManager = new AssetManager();
		this.preloadAssets();
		this.worldScene = new Scene();
		this.worldScene.addObject(new TestAnother());
	}
	
	public void preloadAssets() {
		AssetManager.Group preload = new AssetManager.Group("preload");
		preload.put(new Texture("tex-default", "texture/texture.png"));
		this.assetManager.registerGroup(preload);
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
	
	public AssetManager.Group getAssets() {
		return this.assetManager.findGroup("preload");
	}
}
