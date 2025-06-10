/*package gameengine.engine.renderer.component;

import org.joml.Quaternionf;

import gameengine.engine.physics.Rotator;
import gameengine.util.GeometryUtils;

public class CameraRotator extends Rotator {

	public CameraRotator() {
		super();
	}
	
	
	@Override
	public void rotate(float x, float y) {
		Quaternionf rotationY = new Quaternionf().rotateAxis(GeometryUtils.toRadians(y), 0, 1, 0);
		Quaternionf rotationX = new Quaternionf().rotateAxis(GeometryUtils.toRadians(x), 1, 0, 0);
		this.setQuaternion(rotationX.mul(this.getAsQuaternion()).mul(rotationY));
	}
}
*/