package gameengine.game;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
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
	
	public void possess(Transform possessor) {
		this.position = possessor.position;
		this.rotator = possessor.rotator;
		this.scale = possessor.scale;
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
