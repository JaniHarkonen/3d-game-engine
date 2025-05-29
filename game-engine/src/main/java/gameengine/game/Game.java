package gameengine.game;

import org.lwjgl.opengl.GL46;

import gameengine.engine.Engine;
import gameengine.engine.IScene;
import gameengine.engine.ITickable;
import gameengine.engine.asset.AssetManager;
import gameengine.engine.asset.Texture;
import gameengine.engine.renderer.shader.Shader;
import gameengine.engine.window.Window;
import gameengine.util.FileUtils;

public class Game implements ITickable {

	private AssetManager assetManager;
	private AssetManager.Group agShaders;
	private Scene worldScene;
	
	public Game() {
		this.assetManager = new AssetManager();
		this.agShaders = null;
		this.worldScene = null;
	}
	
	
	public void setup() {
		this.preloadAssets();
		this.worldScene = new Scene();
		this.worldScene.addObject(new TestAnother());
	}
	
	private void preloadAssets() {
		AssetManager.Group preload = new AssetManager.Group("preload");
		preload.put(new Texture("tex-default", FileUtils.getResourcePath("texture/texture.png")));
		this.assetManager.registerGroup(preload);
		preload.load();
	}
	
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
