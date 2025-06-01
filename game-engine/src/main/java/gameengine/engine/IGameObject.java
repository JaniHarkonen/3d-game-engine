package gameengine.engine;

import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.ScenePass;

public interface IGameObject extends ITickable, IRenderable {

	public void onCreate();
	
	public void render(ScenePass renderPass);
}
