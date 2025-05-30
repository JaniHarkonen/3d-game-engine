package gameengine.engine.renderer;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import gameengine.engine.IScene;
import gameengine.engine.window.Window;
import gameengine.game.Game;

public class Renderer {
	
	private ScenePass scenePass;

	public Renderer() {
		this.scenePass = new ScenePass();
		//this.shadowPass = new ShadowPass();
		//this.hudPass = new HudPass();
	}
	
	
	public void setup() {
		GL.createCapabilities();
		GL46.glEnable(GL46.GL_DEPTH_TEST);
		this.scenePass.setup();
	}
	
	public void render(Game game, Window target) {
		GL46.glViewport(0, 0, target.getWidth(), target.getHeight());
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		
			// Reset scenes before submissions
		this.scenePass.reset();
		
			// Generate submissions
		IScene worldScene = game.getWorldScene();
		worldScene.submitToRenderer(this);
		
			// Scene render pass
		this.scenePass.camera = worldScene.getActiveCamera();
		this.scenePass.execute();
	}
	
	public ScenePass getScenePass() {
		return this.scenePass;
	}
}
