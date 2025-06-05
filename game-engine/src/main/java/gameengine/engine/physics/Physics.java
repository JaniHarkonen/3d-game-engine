package gameengine.engine.physics;

import com.bulletphysics.dynamics.RigidBody;

public class Physics {

	private RigidBody body;
	
	public Physics() {
		this.body = null;
	}
	
	
	public RigidBody getBody() {
		return this.body;
	}
}
