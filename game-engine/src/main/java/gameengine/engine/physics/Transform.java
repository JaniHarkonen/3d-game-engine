package gameengine.engine.physics;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.bulletphysics.linearmath.MotionState;

import gameengine.util.GeometryUtils;
import gameengine.util.PhysicsUtils;

public class Transform extends MotionState {
	protected Vector3f position;
	protected Rotator rotator;
	protected Vector3f scale;
	protected Matrix4f transformMatrix;
	
	public Transform() {
		this.position = new Vector3f(0.0f);
		this.rotator = new Rotator();
		this.scale = new Vector3f(1.0f);
		this.transformMatrix = new Matrix4f();
	}
	
	/**
	 * Copy constructor. Creates a new Transform based on a source Transform.
	 * 
	 * @param src Source Transform.
	 */
	public Transform(Transform src) {
		this.position = new Vector3f(src.position);
		this.rotator = new Rotator(src.rotator);
		this.scale = new Vector3f(src.scale);
		src.recalculate();
		this.transformMatrix = new Matrix4f(src.transformMatrix);
	}
	
	
	protected void recalculate() {
		this.transformMatrix.translationRotateScale(
			this.position, this.rotator.getAsQuaternion(), this.scale
		);
	}
	
	public void possess(IHasTransform possessor) {
		Transform transform = possessor.getTransform();
		this.position = transform.position;
		this.rotator = transform.rotator;
		this.scale = transform.scale;
	}
	
	public void shift(float x, float y, float z) {
		this.position.add(x, y, z);
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}
	
	public void setScale(float x, float y, float z) {
		this.scale.set(x, y, z);
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public Rotator getRotator() {
		return this.rotator;
	}
	
	public Vector3f getScale() {
		return this.scale;
	}
	
	public Matrix4f getAsMatrix() {
		this.recalculate();
		return this.transformMatrix;
	}
	
	public com.bulletphysics.linearmath.Transform getAsBulletPhysicsTransform() {
		this.recalculate();
		return PhysicsUtils.transformToBulletphysicsTransform(this);
	}
	
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Transform) ) {
			return false;
		}
		
		Transform t = (Transform) o;
		return(
			this.position.equals(t.position) &&
			this.rotator.equals(t.rotator) &&
			this.scale.equals(t.scale)
		);
	}

	@Override
	public com.bulletphysics.linearmath.Transform getWorldTransform(
		com.bulletphysics.linearmath.Transform transform
	) {
		transform.set(this.getAsBulletPhysicsTransform());
		return transform;
	}

	@Override
	public void setWorldTransform(com.bulletphysics.linearmath.Transform transform) {
		Quaternionf rotation;
		this.position.set(transform.origin.x, transform.origin.y, transform.origin.z);
		rotation = GeometryUtils.getBulletPhysicsTransformRotation(transform, new Quaternionf());
		this.rotator.setQuaternion(rotation);
	}
}
