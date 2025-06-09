package gameengine.engine.physics;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import gameengine.engine.ITickable;

public class PhysicsScene implements ITickable {

	private DynamicsWorld physicsWorld;
	private CollisionDispatcher dispatcher;
	private Map<RigidBody, IPhysicsObject> objects;
	
	public PhysicsScene() {
		this.objects = new LinkedHashMap<>();
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
		this.dispatcher = new CollisionDispatcher(collisionConfig);
		this.physicsWorld = new DiscreteDynamicsWorld(
			this.dispatcher, 
			new DbvtBroadphase(), 
			new SequentialImpulseConstraintSolver(), 
			new DefaultCollisionConfiguration()
		);
		this.physicsWorld.setGravity(new Vector3f(0, -10, 0));
	}
	
	
	@Override
	public void tick(float deltaTime) {
		
			// Handle simulation (NOTE: this also updates the transforms)
		this.physicsWorld.stepSimulation(deltaTime);
		
			// Reset collisions of colliders
		for( IPhysicsObject object : this.objects.values() ) {
			object.getPhysics().update();
		}
		
			// Update colliders if any collisions were detected
		for( int i = 0; i < this.dispatcher.getNumManifolds(); i++ ) {
			PersistentManifold manifold = this.dispatcher.getManifoldByIndexInternal(i);
			
			if( manifold.getNumContacts() > 0 ) {
				IPhysicsObject physics0 = this.objects.get((CollisionObject) manifold.getBody0());
				IPhysicsObject physics1 = this.objects.get((CollisionObject) manifold.getBody1());
				physics0.getPhysics().getCollider().reportCollision(physics1, manifold);
				physics1.getPhysics().getCollider().reportCollision(physics0, manifold);
			}
		}
	}
	
	public void addObject(IPhysicsObject object) {
		RigidBody rigidBody = object.getPhysics().getRigidBody();
		this.physicsWorld.addRigidBody(rigidBody);
		this.objects.put(rigidBody, object);
	}
	
	public void addStaticCollision(IPhysicsObject object) {
		RigidBody rigidBody = object.getPhysics().getRigidBody();
		this.physicsWorld.addCollisionObject(rigidBody);
		this.objects.put(rigidBody, object);
	}
	
	public DynamicsWorld getPhysicsWorld() {
		return this.physicsWorld;
	}
}
