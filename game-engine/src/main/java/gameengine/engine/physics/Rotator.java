package gameengine.engine.physics;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import gameengine.logger.Logger;
import gameengine.util.GeometryUtils;

public class Rotator {
	public static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
	public static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
	public static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);
	
	protected Transform transform;

	protected Quaternionf rotation;
	
	public Rotator(Transform transform) {
		this.transform = transform;
		this.rotation = new Quaternionf();
	}
	
	public Rotator() {
		this((Transform) null);
	}
	
	public Rotator(Rotator src) {
		this.transform = src.transform;
		this.rotation = new Quaternionf(src.rotation);
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
		Quaternionf rotationY = new Quaternionf().rotateAxis(GeometryUtils.toRadians(y), Y_AXIS);
		Quaternionf rotationX = new Quaternionf().rotateAxis(GeometryUtils.toRadians(x), X_AXIS);
		this.rotation = rotationY.mul(this.rotation).mul(rotationX);
	}
	
	public void rotate(Quaternionf rotation) {
		this.rotation = rotation.mul(this.rotation);
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
		this.rotation = quaternion;
	}
	
	public void setQuaternion(float x, float y, float z, float w) {
		this.rotation.set(x, y, z, w);
	}
	
	public Quaternionf getAsQuaternion(Quaternionf dest) {
		return dest.set(this.rotation);
	}
	
	public Vector3f getForwardVector(Vector3f result) {
		this.getBackwardVector(result);
		result.negate();
		return result;
	}
	
	public Vector3f getBackwardVector(Vector3f result) {
		this.rotation.positiveZ(result);
		return result;
	}
	
	public Vector3f getLeftVector(Vector3f result) {
		this.getRightVector(result);
		result.negate();
		return result;
	}
	
	public Vector3f getRightVector(Vector3f result) {
		this.rotation.positiveX(result);
		return result;
	}
	
	public Vector3f getDownVector(Vector3f result) {
		this.rotation.positiveY(result);
		return result;
	}
	
	public Vector3f getUpVector(Vector3f result) {
		this.getDownVector(result);
		result.negate();
		return result;
	}
}
