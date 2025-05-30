package gameengine.engine;

import gameengine.engine.renderer.Camera;
import gameengine.engine.renderer.IRenderable;

public interface IScene extends IRenderable {
	
	public void setActiveCamera(Camera camera);
	
	public Camera getActiveCamera();
}
