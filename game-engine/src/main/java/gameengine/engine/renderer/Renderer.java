package gameengine.engine.renderer;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import gameengine.engine.IScene;
import gameengine.engine.window.Window;
import gameengine.game.Game;
import gameengine.logger.Logger;

public class Renderer {
	
	public static final int RENDER_PASS_COUNT = 1;
	
	public static final int MAX_TEXTURE_COUNT = 1;
	
	private ScenePass scenePass;

	public Renderer() {
		this.scenePass = new ScenePass();
		//this.shadowPass = new ShadowPass();
		//this.hudPass = new HudPass();
	}
	
	
	public void setup() {
		GL.createCapabilities();
		GL46.glEnable(GL46.GL_DEPTH_TEST);
		GL46.glEnable(GL46.GL_CULL_FACE);
		GL46.glCullFace(GL46.GL_BACK);
		this.scenePass.setup();
		Logger.info(this, "Renderer setup done.");
	}
	
	public void render(Game game, Window target) {
		GL46.glViewport(0, 0, target.getWidth(), target.getHeight());
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		GL46.glClearColor(0, 1, 0, 1);
		
			// Reset scenes before submissions
		
			// Generate submissions
		Logger.spam(this, "Submitting to scene render pass...");
		IScene worldScene = game.getWorldScene();
		worldScene.submitToRenderer(this);
		
			// Scene render pass
		this.scenePass.execute();
	}
	
	public ScenePass getScenePass() {
		return this.scenePass;
	}
}
