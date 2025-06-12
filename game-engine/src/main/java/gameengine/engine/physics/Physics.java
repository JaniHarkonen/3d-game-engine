package gameengine.engine.physics;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

public class Physics {

	private final Transform controlledTransform;
	private RigidBody rigidBody;
	private Collider collider;
	
	public Physics(Transform controlledTransform, RigidBodyConstructionInfo info, Collider collider) {
		this.controlledTransform = controlledTransform;
		this.rigidBody = new RigidBody(info);
		this.collider = collider;
	}
	
	
	void update() {
		this.collider.clearCollisions();
	}
	
	public Transform getTransform() {
		return this.controlledTransform;
	}
	
	public RigidBody getRigidBody() {
		return this.rigidBody;
	}
	
	public Collider getCollider() {
		return this.collider;
	}
}
