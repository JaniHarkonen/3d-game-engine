package gameengine.game;

public class CameraTransform extends Transform {
	@Override
	protected void recalculate() {
		this.transformMatrix.identity()
		.rotate(this.rotator.getAsQuaternion())
		.translate(-this.position.x, -this.position.y, -this.position.z);
	}
}
