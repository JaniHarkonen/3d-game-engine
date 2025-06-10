/*package gameengine.engine.renderer.component;

import gameengine.engine.physics.IHasTransform;
import gameengine.engine.physics.Transform;


public class CameraTransform extends Transform {
	
	public CameraTransform() {
		super();
		this.rotator = new CameraRotator();
	}
	
	
	@Override
	protected void recalculate() {
		this.transformMatrix.identity()
		.rotate(this.rotator.getAsQuaternion())
		.translate(-this.position.x, -this.position.y, -this.position.z);
	}
	
	@Override
	public void possess(IHasTransform possessor) {
		Transform transform = possessor.getTransform();
		this.position = transform.getPosition();
		this.scale = transform.getScale();
		
		if( transform instanceof CameraTransform ) {
			this.rotator = transform.getRotator();
		}
	}
}*/
