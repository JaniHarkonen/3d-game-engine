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
	private CascadeShadowPass cascadeShadowPass;

	public Renderer() {
		this.scenePass = new ScenePass();
		this.cascadeShadowPass = new CascadeShadowPass();
		//this.hudPass = new HudPass();
	}
	
	
	public void setup() {
		GL.createCapabilities();
		GL46.glEnable(GL46.GL_DEPTH_TEST);
		GL46.glEnable(GL46.GL_CULL_FACE);
		GL46.glCullFace(GL46.GL_BACK);
		GL46.glEnable(GL46.GL_BLEND);
		GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
		this.cascadeShadowPass.setup();
		this.scenePass.setup();
		this.scenePass.cascadeShadowPass = this.cascadeShadowPass;
		Logger.info(this, "Renderer setup done.");
	}
	
	public void render(Game game, Window target) {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		GL46.glClearColor(0, 1, 0, 1);
		
			// Generate submissions
		Logger.spam(this, "Submitting to scene render pass...");
		IScene worldScene = game.getWorldScene();
		worldScene.submitToRenderer(this);
		
			// Cascade shadow pass
		this.cascadeShadowPass.execute();
		this.cascadeShadowPass.reset();

			// Scene render pass
		GL46.glViewport(0, 0, target.getWidth(), target.getHeight());
		this.scenePass.execute();
		this.scenePass.reset();
	}
	
	public ScenePass getScenePass() {
		return this.scenePass;
	}
	
	public CascadeShadowPass getCascadeShadowPass() {
		return this.cascadeShadowPass;
	}
}
