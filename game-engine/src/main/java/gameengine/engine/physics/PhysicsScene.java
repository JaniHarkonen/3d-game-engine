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
		/*CollisionShape ground = new StaticPlaneShape(new Vector3f(0, 1, 0), 0.25f);
		CollisionShape ball = new SphereShape(1f);
		MotionState groundMotion = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1f)));
		RigidBodyConstructionInfo groundConstructionInfo = new RigidBodyConstructionInfo(0, groundMotion, ground, new Vector3f(0, 0, 0));
		groundConstructionInfo.restitution = 0.0f;
		groundBody = new RigidBody(groundConstructionInfo);
		groundBody.setFriction(0.5f);
		this.physicsWorld.addCollisionObject(groundBody);*/
		
		//RigidBody first = null;
		/*
		for( int i = 0; i < 1; i++ ) {
			com.bulletphysics.linearmath.Transform ballTransform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 20+i*5, 0), 1f));
			MotionState ballMotion = new DefaultMotionState(ballTransform);
			Vector3f ballInertia = new Vector3f(0, 0, 0);
			ball.calculateLocalInertia(2.5f, ballInertia);
			RigidBodyConstructionInfo ballConstructionInfo = new RigidBodyConstructionInfo(2000.5f, ballMotion, ball, ballInertia);
			ballConstructionInfo.restitution = 0.0f;
			ballConstructionInfo.angularDamping = 0.95f;
			targetBody = new RigidBody(ballConstructionInfo);
			targetBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
			targetBody.setAngularFactor(1);
			targetBody.setFriction(0.2f);
			this.physicsWorld.addRigidBody(targetBody);
			
			if( first == null ) {
				first = targetBody;
			}
		}*/
		
		//this.physicsWorld.addCollisionObject(collisionObject);
		//this.physicsWorld.addRigidBody(targetBody);
		
		//targetBody = first;
		//targetBody.activate(true);
		//this.physicsWorld.removeRigidBody(body);
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
