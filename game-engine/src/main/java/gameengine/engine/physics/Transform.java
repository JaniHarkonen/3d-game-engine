package gameengine.engine.physics;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.bulletphysics.linearmath.MotionState;

import gameengine.logger.Logger;
import gameengine.util.GeometryUtils;
import gameengine.util.PhysicsUtils;

public class Transform extends MotionState {
	public static class Node {
		private final Transform transform;
		
		private Node parent;
		private List<Node> children;
		private boolean isStale;
		
		public Node(Transform transform) {
			this.transform = transform;
			this.parent = null;
			this.children = new ArrayList<>();
			this.isStale = false;
		}
		
		
		public void parentOf(Node childNode) {
			this.children.add(childNode);
			childNode.parent = this;
		}
		
		public void childOf(Node parentNode) {
			parentNode.parentOf(this);
		}
		
		public Transform edit() {
			this.stale();
			return this.transform;
		}
		
		public Transform read() {
			this.recalculate();
			return this.transform;
		}
		
		private void stale() {
			this.isStale = true;
			
			for( Node child : this.children ) {
				child.stale();
			}
		}
		
		private void recalculate() {
			if( this.isStale && this.parent != null ) {
				this.parent.recalculate();
			}
			
			//this.transform.
		}
	}
	
	protected Vector3f position;
	protected Rotator rotator;
	protected Vector3f scale;
	protected Vector3f origin;
	protected Matrix4f transformMatrix;
	
	public Transform() {
		this.position = new Vector3f(0.0f);
		this.rotator = new Rotator();
		this.scale = new Vector3f(1.0f);
		this.origin = new Vector3f(0.0f);
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
			this.getPosition(new Vector3f()), 
			this.rotator.getAsQuaternion(new Quaternionf()), 
			this.scale
		);
	}
	
	public void bindPosition(Transform transform) {
		this.position = transform.position;
	}
	
	public void bindRotation(Transform transform) {
		this.rotator = transform.rotator;
	}
	
	public void bindScale(Transform transform) {
		this.scale = transform.scale;
	}
	
	public void unbindPosition() {
		this.position = new Vector3f(this.position);
	}
	
	public void unbindRotation() {
		this.rotator = new Rotator(this.rotator);
	}
	
	public void unbindScale() {
		this.scale = new Vector3f(this.scale);
	}
	
	public void bind(Transform transform) {
		this.bindPosition(transform);
		this.bindRotation(transform);
		this.bindScale(transform);
	}
	
	public void shift(float x, float y, float z) {
		this.position.add(x, y, z);
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}
	
	public void setRotator(Rotator rotator) {
		this.rotator = rotator;
	}
	
	public void setScale(float x, float y, float z) {
		this.scale.set(x, y, z);
	}
	
	public void setOrigin(float x, float y, float z) {
		this.origin.set(x, y, z);
	}
	
	@Override
	public void setWorldTransform(com.bulletphysics.linearmath.Transform transform) {
		Quaternionf rotation;
		this.position.set(transform.origin.x, transform.origin.y, transform.origin.z);
		rotation = GeometryUtils.getBulletPhysicsTransformRotation(transform, new Quaternionf());
		this.rotator.setQuaternion(rotation);
	}
	
	@Override
	public com.bulletphysics.linearmath.Transform getWorldTransform(
		com.bulletphysics.linearmath.Transform transform
	) {
		transform.set(this.getAsBulletPhysicsTransform());
		return transform;
	}
	
	public Vector3f getPosition() {
		return new Vector3f().set(this.position).add(this.origin);
	}
	
	public Vector3f getPosition(Vector3f dest) {
		return dest.set(this.getPosition());
	}
	
	public Rotator getRotator() {
		return this.rotator;
	}
	
	public Vector3f getScale() {
		return this.scale;
	}
	
	public Vector3f getOrigin() {
		return this.origin;
	}
	
	public Vector3f getScale(Vector3f dest) {
		return dest.set(this.scale);
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
}
