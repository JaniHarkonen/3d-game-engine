package gameengine.engine.physics;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.collision.shapes.CollisionShape;

public class Collider {

	private CollisionShape collisionShape;
	private Map<IPhysicsObject, PersistentManifold> collisions;
	
	public Collider(CollisionShape collisionShape) {
		this.collisionShape = collisionShape;
		this.collisions = new LinkedHashMap<>();
	}
	
	
	void clearCollisions() {
		this.collisions.clear();
	}
	
	void reportCollision(IPhysicsObject other, PersistentManifold manifold) {
		this.collisions.put(other, manifold);
	}
	
	public CollisionShape getCollisionShape() {
		return this.collisionShape;
	}
	
	public boolean collidesWith(IPhysicsObject other) {
		return this.collisions.containsKey(other);
	}
}
