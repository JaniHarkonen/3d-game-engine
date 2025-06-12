package gameengine.engine;

import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.component.Camera;

public interface IScene extends IRenderable, ITickable {
	
	public void setActiveCamera(Camera camera);
	
	public Camera getActiveCamera();
}
