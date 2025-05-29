package gameengine.engine;

import gameengine.engine.renderer.Camera;
import gameengine.engine.renderer.Projection;

public interface IScene extends IRenderable {

	public Projection getActiveProjection();
	
	public Camera getActiveCamera();
}
