package gameengine.engine;

import gameengine.engine.renderer.IRenderable;

public interface IGameObject extends ITickable, IRenderable {

	public void onCreate();
}
