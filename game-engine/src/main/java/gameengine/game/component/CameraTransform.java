package gameengine.game.component;

import gameengine.engine.physics.Transform;

/**
 * This is a special transform class derived from the generic Transform.
 * CameraTransform should only be used by the camera, as its transform
 * matrix is calculated slightly different to account for the fact, that
 * the camera perspective exists inverse to the viewed scene.
 *
 */
public class CameraTransform extends Transform {
	@Override
	protected void recalculate() {
		this.transformMatrix.identity()
		.rotate(this.rotator.getAsQuaternion())
		.translate(-this.position.x, -this.position.y, -this.position.z);
	}
}
