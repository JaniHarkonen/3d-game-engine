package gameengine.engine;

import gameengine.engine.renderer.IHasShadow;
import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.ScenePass;

public interface IGameObject extends ITickable, IRenderable, IHasShadow {

	public void onCreate();
	
	public void render(ScenePass renderPass);
}
