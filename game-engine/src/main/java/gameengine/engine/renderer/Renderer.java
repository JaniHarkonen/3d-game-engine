package gameengine.engine.renderer;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import gameengine.engine.window.Window;
import gameengine.game.Game;

public class Renderer {
	
	private ScenePass scenePass;

	public Renderer() {
		GL.createCapabilities();
		GL46.glEnable(GL46.GL_DEPTH_TEST);
		scenePass = new ScenePass();
	}
	
	public void render(Game game, Window target) {
		GL46.glViewport(0, 0, target.getWidth(), target.getHeight());
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		scenePass.render(game);
	}
}
