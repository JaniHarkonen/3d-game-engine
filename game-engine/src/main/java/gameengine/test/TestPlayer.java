package gameengine.test;


import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

import gameengine.debug.CollisionDebugger;
import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.asset.Animation;
import gameengine.engine.physics.Collider;
import gameengine.engine.physics.IHasTransform;
import gameengine.engine.physics.IPhysicsObject;
import gameengine.engine.physics.Physics;
import gameengine.engine.physics.Transform;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.component.Camera;
import gameengine.engine.window.Input;
import gameengine.game.component.Animator;
import gameengine.game.component.Model;
import gameengine.util.GeometryUtils;

public class TestPlayer implements IGameObject, IHasTransform, IPhysicsObject {
	
	private Transform transform;
	private Model model;
	private Animator animator;
	private float cameraMode;
	private Physics physics;
	private CollisionDebugger debbuger;
	private boolean possessCamera;

    public TestPlayer(Model model) {
    	this.transform = new Transform();
    	model.getTransform().possess(this);
    	this.animator = new Animator(model);
    	this.cameraMode = 6f;
    	this.physics = null;
    	this.debbuger = null;
    	this.possessCamera = false;
    }

    
	@Override
	public void onCreate() {
		CollisionShape collision = new BoxShape(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(2.2f/2, 1.6f/2, 4.7f/2)));
		this.debbuger = new CollisionDebugger(collision, this.transform);
		
		Vector3f inertia = new Vector3f(1, 1, 1);
		//RigidBodyConstructionInfo bodyInfo = new RigidBodyConstructionInfo(2000.5f, new Transform(), collision);
		RigidBodyConstructionInfo bodyInfo = new RigidBodyConstructionInfo(2000.5f, this.transform, collision);
		collision.calculateLocalInertia(2000.5f, GeometryUtils.vector3fToJavaxVector3f(inertia, bodyInfo.localInertia));
		bodyInfo.restitution = 0.0f;
		bodyInfo.angularDamping = 0.3f;
		Collider collider = new Collider(collision);
		//this.physics = new Physics(new Transform(), bodyInfo, collider);
		this.physics = new Physics(this.transform, bodyInfo, collider);
		RigidBody body = this.physics.getRigidBody();
		body.setAngularFactor(0.7f);
		body.setFriction(0.2f);
	}

	@Override
	public void tick(float deltaTime) {
		this.animator.tick(deltaTime);
		/*
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_R).hashCode(), (e) -> {
			this.getTransform().getRotator().rotateX(deltaTime);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_F).hashCode(), (e) -> {
			this.getTransform().getRotator().rotateX(-deltaTime);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_T).hashCode(), (e) -> {
			this.getTransform().getRotator().rotateY(deltaTime);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_G).hashCode(), (e) -> {
			this.getTransform().getRotator().rotateY(-deltaTime);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_Y).hashCode(), (e) -> {
			this.getTransform().getRotator().rotateZ(deltaTime);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_H).hashCode(), (e) -> {
			this.getTransform().getRotator().rotateZ(-deltaTime);
		});
		*/
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_1).hashCode(), (e) -> {
			this.getAnimator().setAnimation((Animation) Engine.getGame().getAssets().get("anim-player-idle"));
			this.getAnimator().restart();
			this.getAnimator().play();
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_2).hashCode(), (e) -> {
			this.getAnimator().setAnimation((Animation) Engine.getGame().getAssets().get("anim-player-run"));
			this.getAnimator().restart();
			this.getAnimator().play();
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_3).hashCode(), (e) -> {
			this.getAnimator().setAnimation((Animation) Engine.getGame().getAssets().get("anim-player-idle-smoke"));
			this.getAnimator().restart();
			this.getAnimator().play();
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_4).hashCode(), (e) -> {
			this.getAnimator().setAnimation((Animation) Engine.getGame().getAssets().get("anim-player-get-up-back"));
			this.getAnimator().restart();
			this.getAnimator().play();
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_V).hashCode(), (e) -> {
			this.cameraMode = (this.cameraMode == 18) ? 6 : this.cameraMode + 6;
		});
		
		RigidBody targetBody = this.physics.getRigidBody();
		float force = 10000;
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_SPACE).hashCode(), (e) -> {
			targetBody.activate(true);
			targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(0,1000,0)));
		});
		
		
		Vector3f forwards = this.transform.getRotator().getForwardVector(new Vector3f());
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_UP).hashCode(), (e) -> {
			targetBody.activate(true);
			//Vector3f forward = this.transform.getRotator().getForwardVector(new Vector3f());
			//targetBody.setLinearVelocity(GeometryUtils.vector3fToJavaxVector3f(forward.mul(2)));
			//targetBody.applyForce(GeometryUtils.vector3fToJavaxVector3f(forward.mul(force)), GeometryUtils.vector3fToJavaxVector3f(forward.mul(3f).add(new Vector3f(4,0,0))));
			targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getForwardVector(new Vector3f()).mul(force).mul(new Vector3f(-1,1,1))));
			//targetBody.applyForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getForwardVector(new Vector3f()).mul(force)), GeometryUtils.vector3fToJavaxVector3f(new Vector3f()));
			//targetBody.setLinearVelocity(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(0,0,-20)));
			
			//Vector3f forward = this.transform.getRotator().getForwardVector(new Vector3f()).mul(20).mul(new Vector3f(-1,1,1));
			//this.transform.shift(forward.x, forward.y, forward.z);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_DOWN).hashCode(), (e) -> {
			//targetBody.activate(true);
			//targetBody.applyForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getBackwardVector(new Vector3f()).mul(force)), GeometryUtils.vector3fToJavaxVector3f(new Vector3f()));
			//targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getBackwardVector(new Vector3f()).mul(force)));
			targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getBackwardVector(new Vector3f()).mul(force).mul(new Vector3f(-1,1,1))));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_LEFT).hashCode(), (e) -> {
			//targetBody.activate(true);
			//targetBody.applyForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getLeftVector(new Vector3f()).mul(force)), GeometryUtils.vector3fToJavaxVector3f(new Vector3f()));
			//targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getLeftVector(new Vector3f()).mul(force)));
			targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getLeftVector(new Vector3f()).mul(force).mul(new Vector3f(1,1,-1))));
			//targetBody.setLinearVelocity(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(-20,0,0)));
			//this.transform.shift(-20, 0, 0);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_RIGHT).hashCode(), (e) -> {
			targetBody.activate(true);
			//targetBody.applyForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getRightVector(new Vector3f()).mul(force)), GeometryUtils.vector3fToJavaxVector3f(new Vector3f()));
			targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getRightVector(new Vector3f()).mul(force).mul(new Vector3f(1,1,-1))));
			//targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getRightVector(new Vector3f()).mul(force)));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_C).hashCode(), (e) -> {
			this.possessCamera = !this.possessCamera;
			//targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getRightVector(new Vector3f()).mul(force)));
		});
		
		Camera cam = Engine.getGame().getWorldScene().getActiveCamera();
		org.joml.Vector3f v = cam.getTransform().getRotator().getForwardVector(new org.joml.Vector3f()).mul(this.cameraMode).negate();
		org.joml.Vector3f me = this.transform.getPosition();
		
		if( !this.possessCamera ) {
			cam.getTransform().setPosition(me.x + v.x, me.y + v.y + 3f, me.z + v.z);
			//cam.getTransform().setPosition(me.x, me.y + 3f, me.z);
			//cam.getTransform().getRotator().setQuaternion(this.transform.getRotator().getAsQuaternion());
		} else {
			cam.getTransform().possess(this);
		}
		
		//cam.getTransform().setPosition(me.x, me.y, me.z);
		//cam.getTransform().getRotator().set
	}
	
	@Override
	public void submitToRenderer(Renderer renderer) {
		this.animator.submitToRenderer(renderer);
		//this.debbuger.submitToRenderer(renderer);
	}
	
	@Override
	public Transform getTransform() {
		return this.transform;
	}
	
	public Animator getAnimator() {
		return this.animator;
	}
	
	@Override
	public Physics getPhysics() {
		return this.physics;
	}
}
