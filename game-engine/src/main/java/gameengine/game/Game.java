package gameengine.game;

import gameengine.engine.Engine;
import gameengine.engine.IScene;
import gameengine.engine.ITickable;
import gameengine.engine.asset.AssetManager;
import gameengine.engine.asset.Texture;
import gameengine.engine.window.Window;
import gameengine.game.test.TestAnother;
import gameengine.game.test.TestCamera;
import gameengine.logger.Logger;
import gameengine.util.FileUtils;

public class Game implements ITickable {

	private AssetManager assetManager;
	private Scene worldScene;
	
	public Game() {
		this.assetManager = new AssetManager();
		this.worldScene = null;
	}
	
	
	public void setup() {
		this.preloadAssets();
		this.worldScene = new Scene();
		this.worldScene.addObject(new TestCamera());
		this.worldScene.addObject(new TestAnother());
		Logger.info(this, "Game setup done!");
	}
	
	private void preloadAssets() {
		AssetManager.Group preload = new AssetManager.Group("preload");
		preload.put(new Texture("tex-default", FileUtils.getResourcePath("texture/texture.png")));
		preload.put(new Mesh("mesh-man", FileUtils.getResourcePath("model/man.fbx")));
		preload.put(new Mesh("mesh-box", FileUtils.getResourcePath("model/box.fbx")));
		this.assetManager.registerGroup(preload);
		preload.load();
	}
	
	@Override
	public void tick(float deltaTime) {
		this.worldScene.tick(deltaTime);
		
		Window window = Engine.getWindow();
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
