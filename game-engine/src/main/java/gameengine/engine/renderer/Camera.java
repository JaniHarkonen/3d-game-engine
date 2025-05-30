package gameengine.engine.renderer;

import gameengine.game.Transform;

public class Camera {
	private Transform transform;
	private Projection projection;

    public Camera(Projection projection) {
        this.transform = new Transform() {
        	@Override
        	protected void recalculate() {
        		this.transformMatrix.identity()
        		.rotate(this.rotator.getAsQuaternion())
        		.translate(-this.position.x, -this.position.y, -this.position.z);
        	}
        };
        this.projection = projection;
    }

    
    public Transform getTransform() {
    	return this.transform;
    }
    
    public Projection getProjection() {
    	return this.projection;
    }
}
