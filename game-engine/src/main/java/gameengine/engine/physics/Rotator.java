package gameengine.engine.physics;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import gameengine.util.GeometryUtils;

public class Rotator {
	public static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
	public static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
	public static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

	private Quaternionf rotationQuaternion;
	
	public Rotator() {
		this.rotationQuaternion = new Quaternionf();
	}
	
	public Rotator(Rotator src) {
		this.rotationQuaternion = new Quaternionf(src.rotationQuaternion);
	}
	
	/*
	private void recalculate() {
		this.rotationQuaternion.fromAxisAngleRad(X_AXIS, this.angles.x)
		.mul(
			this.rotationQuaternionTempY.fromAxisAngleRad(Y_AXIS, this.angles.y)
		).mul(
			this.rotationQuaternionTempZ.fromAxisAngleRad(Z_AXIS, this.angles.z)
		);
	}*/
	
	public void rotate(float x, float y) {
		Quaternionf rotationY = new Quaternionf().rotateAxis(GeometryUtils.toRadians(y), 0, 1, 0);
		Quaternionf rotationX = new Quaternionf().rotateAxis(GeometryUtils.toRadians(x), 1, 0, 0);
		this.rotationQuaternion = rotationY.mul(this.rotationQuaternion).mul(rotationX);
	}
	
	/*public void rotate(float xDegree, float yDegree, float zDegree) {
		float x = GeometryUtils.toRadians(this.eulers.x + xDegree);
		float y = GeometryUtils.toRadians(this.eulers.y + yDegree);
		float z = GeometryUtils.toRadians(this.eulers.z + zDegree);
		
		float absYSin = 1 - (float) Math.abs(Math.sin(y));
		
		float xAxis = (float) Math.cos(x) * absYSin;
		float yAxis = (float) Math.sin(y);
		float zAxis = (float) Math.sin(x) * absYSin;
		
		Quaternionf q = new Quaternionf().lookAlong(new Vector3f(xAxis, yAxis, zAxis), Y_AXIS);
		this.rotationQuaternion = q;
		this.eulers.add(xDegree, yDegree, zDegree);
	}*/
	
	public void setQuaternion(Quaternionf quaternion) {
		this.rotationQuaternion = quaternion;
	}
	
	public void setQuaternion(float x, float y, float z, float w) {
		this.rotationQuaternion.set(x, y, z, w);
	}
	
	public Quaternionf getAsQuaternion() {
		return this.rotationQuaternion;
	}
	
	public Vector3f getForwardVector(Vector3f result) {
		this.getBackwardVector(result);
		result.negate();
		return result;
	}
	
	public Vector3f getBackwardVector(Vector3f result) {
		this.rotationQuaternion.positiveZ(result);
		return result;
	}
	
	public Vector3f getLeftVector(Vector3f result) {
		this.getRightVector(result);
		result.negate();
		return result;
	}
	
	public Vector3f getRightVector(Vector3f result) {
		this.rotationQuaternion.positiveX(result);
		return result;
	}
	
	public Vector3f getDownVector(Vector3f result) {
		this.rotationQuaternion.positiveY(result);
		return result;
	}
	
	public Vector3f getUpVector(Vector3f result) {
		this.getDownVector(result);
		result.negate();
		return result;
	}
}
