package gameengine.engine.physics;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.glfw.GLFW;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import gameengine.engine.Engine;
import gameengine.engine.ITickable;
import gameengine.engine.window.Input;
import gameengine.logger.Logger;

public class Physics implements ITickable {

	private DynamicsWorld physicsWorld;
	public static RigidBody targetBody;
	public static RigidBody groundBody;
	private CollisionDispatcher dispatcher;
	
	public Physics() {
		BroadphaseInterface broadphase = new DbvtBroadphase();
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
		this.dispatcher = new CollisionDispatcher(collisionConfig);
		ConstraintSolver solver = new SequentialImpulseConstraintSolver();
		this.physicsWorld = new DiscreteDynamicsWorld(this.dispatcher, broadphase, solver, collisionConfig);
		this.physicsWorld.setGravity(new Vector3f(0, -10, 0));
		CollisionShape ground = new StaticPlaneShape(new Vector3f(0, 1, 0), 0.25f);
		CollisionShape ball = new SphereShape(1f);
		MotionState groundMotion = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1f)));
		RigidBodyConstructionInfo groundConstructionInfo = new RigidBodyConstructionInfo(0, groundMotion, ground, new Vector3f(0, 0, 0));
		groundConstructionInfo.restitution = 0.0f;
		groundBody = new RigidBody(groundConstructionInfo);
		groundBody.setFriction(0.5f);
		this.physicsWorld.addRigidBody(groundBody);
		
		RigidBody first = null;
		
		for( int i = 0; i < 1; i++ ) {
			Transform ballTransform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 20+i*5, 0), 1f));
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
		}
		
		targetBody = first;
		targetBody.activate(true);
		//this.physicsWorld.removeRigidBody(body);
	}
	
	
	@Override
	public void tick(float deltaTime) {
		float force = 10000;
		if( this.dispatcher.getNumManifolds() > 0 ) {
			if( this.dispatcher.getManifoldByIndexInternal(0).getNumContacts() > 0 ) {
				Logger.debug(this, "num manifolds", this.dispatcher.getManifoldByIndexInternal(0).getContactPoint(0).appliedImpulse);
			}
		}
		
		this.physicsWorld.stepSimulation(deltaTime);
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_SPACE).hashCode(), (e) -> {
			targetBody.activate(true);
			targetBody.applyCentralForce(new Vector3f(0,1000,0));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_UP).hashCode(), (e) -> {
			targetBody.activate(true);
			targetBody.applyCentralForce(new Vector3f(0,0,-force));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_DOWN).hashCode(), (e) -> {
			targetBody.activate(true);
			targetBody.applyCentralForce(new Vector3f(0,0,force));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_LEFT).hashCode(), (e) -> {
			targetBody.activate(true);
			targetBody.applyCentralForce(new Vector3f(-force,0,0));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_RIGHT).hashCode(), (e) -> {
			targetBody.activate(true);
			targetBody.applyCentralForce(new Vector3f(force,0,0));
		});
	}
}
