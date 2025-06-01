package gameengine.engine.renderer.component;

import gameengine.game.component.CameraTransform;
import gameengine.game.component.Transform;

public class Camera {
	private Transform transform;
	private Projection projection;

    public Camera(Projection projection) {
    		// Special transform whose matrix is translated and rotated slightly different
        this.transform = new CameraTransform();
        this.projection = projection;
    }

    
    public Transform getTransform() {
    	return this.transform;
    }
    
    public Projection getProjection() {
    	return this.projection;
    }
}
