package gameengine.engine.physics;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Rotator {
	public static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
	public static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
	public static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

	private Vector3f angles;
	private Quaternionf rotationQuaternion;
	private Quaternionf rotationQuaternionTempY;
	private Quaternionf rotationQuaternionTempZ;
	
	public Rotator() {
		this.angles = new Vector3f(0.0f);
		this.rotationQuaternion = new Quaternionf();
		this.rotationQuaternionTempY = new Quaternionf();
		this.rotationQuaternionTempZ = new Quaternionf();
	}
	
	public Rotator(Rotator src) {
		this.angles = new Vector3f(src.angles);
		this.rotationQuaternion = new Quaternionf(src.rotationQuaternion);
	}
	
	
	private void recalculate() {
		this.rotationQuaternion.fromAxisAngleRad(X_AXIS, this.angles.x)
		.mul(
			this.rotationQuaternionTempY.fromAxisAngleRad(Y_AXIS, this.angles.y)
		).mul(
			this.rotationQuaternionTempZ.fromAxisAngleRad(Z_AXIS, this.angles.z)
		);
	}
	
	public void rotate(float xAngle, float yAngle, float zAngle) {
		this.setEulerAngles(
			this.angles.x + xAngle, this.angles.y + yAngle, this.angles.z + zAngle
		);
	}
	
	public void rotateX(float xAngle) {
		this.rotate(xAngle, 0, 0);
	}
	
	public void rotateY(float yAngle) {
		this.rotate(0, yAngle, 0);
	}
	
	public void rotateZ(float zAngle) {
		this.rotate(0, 0, zAngle);
	}
	
	public void setEulerAngles(float xAngle, float yAngle, float zAngle) {
		this.angles.set(xAngle, yAngle, zAngle);
	}
	
	public void setXAngle(float xAngle) {
		this.setEulerAngles(xAngle, this.angles.y, this.angles.z);
	}
	
	public void setYAngle(float yAngle) {
		this.setEulerAngles(this.angles.x, yAngle, this.angles.z);
	}
	
	public void setZAngle(float zAngle) {
		this.setEulerAngles(this.angles.x, this.angles.y, zAngle);
	}
	
	public void setQuaternion(Quaternionf quaternion) {
		this.rotationQuaternion = quaternion;
		float x = quaternion.x;
		float y = quaternion.y;
		float z = quaternion.z;
		float w = quaternion.w;
		
		float roll, pitch, yaw;

			// Roll (x-axis rotation)
		float sinr_cosp = 2 * (w * x + y * z);
		float cosr_cosp = 1 - 2 * (x * x + y * y);
        roll = (float) Math.atan2(sinr_cosp, cosr_cosp);

        	// Pitch (y-axis rotation)
        float sinp = 2 * (w * y - z * x);
        
        /*if( Math.abs(sinp) >= 1 ) {
        	pitch = (float) Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
        } else {*/
        	pitch = (float) Math.asin(sinp);
        //}

        	// Yaw (z-axis rotation)
        float siny_cosp = 2 * (w * z + x * y);
        float cosy_cosp = 1 - 2 * (y * y + z * z);
        yaw = (float) Math.atan2(siny_cosp, cosy_cosp);
        this.setEulerAngles(roll, pitch, yaw);
	}
	
	public void setQuaternion(float x, float y, float z, float w) {
		this.rotationQuaternion.set(x, y, z, w);
	}
	
	public float getXAngle() {
		return this.angles.x;
	}
	
	public float getYAngle() {
		return this.angles.y;
	}
	
	public float getZAngle() {
		return this.angles.z;
	}
	
	public Vector3f getAsEulerAngles() {
		return this.angles;
	}
	
	public Quaternionf getAsQuaternion() {
		this.recalculate();
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
	
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Rotator) ) {
			return false;
		}
		
		Rotator r = (Rotator) o;
		return this.angles.equals(r.angles);
	}
}
