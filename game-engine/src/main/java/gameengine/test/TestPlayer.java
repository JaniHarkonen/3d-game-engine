package gameengine.test;


import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

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
import gameengine.logger.Logger;
import gameengine.util.GeometryUtils;

public class TestPlayer implements IGameObject, IHasTransform, IPhysicsObject {
	
	private Transform transform;
	private Model model;
	private Animator animator;
	private float cameraMode;
	private Physics physics;

    public TestPlayer(Model model) {
    	this.transform = new Transform();
    	model.getTransform().possess(this);
    	this.animator = new Animator(model);
    	this.cameraMode = 6f;
    	this.physics = null;
    }

    
	@Override
	public void onCreate() {
		//CollisionShape collision = new BoxShape(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(4, 0.1f, 8f)));
		//CollisionShape collision = new SphereShape(1f);
		CollisionShape collision = new CapsuleShape(1, 2);
		/*Vector3f[] vertices = Mesh.class.cast(Engine.getGame().getAssets().get("mesh-car-test")).getSubmesh(0).getVertices();
		ObjectArrayList<javax.vecmath.Vector3f> objectArrayList = new ObjectArrayList<>(vertices.length);
		for( Vector3f v : vertices ) {
			objectArrayList.add(GeometryUtils.vector3fToJavaxVector3f(v));
		}
		ConvexHullShape collision = new ConvexHullShape(objectArrayList);
		ShapeHull shape = new ShapeHull(collision);
		shape.buildHull(0);
		collision = new ConvexHullShape(shape.getVertexPointer());*/
		
		Vector3f inertia = new Vector3f(0, 0, 0);
		RigidBodyConstructionInfo bodyInfo = new RigidBodyConstructionInfo(2000.5f, this.transform, collision);
		collision.calculateLocalInertia(2000.5f, GeometryUtils.vector3fToJavaxVector3f(inertia, bodyInfo.localInertia));
		bodyInfo.restitution = 0.0f;
		bodyInfo.angularDamping = 0.95f;
		Collider collider = new Collider(collision);
		this.physics = new Physics(this.transform, bodyInfo, collider);
		RigidBody body = this.physics.getRigidBody();
		body.setAngularFactor(0f);
		body.setFriction(0.2f);
	}

	@Override
	public void tick(float deltaTime) {
		this.animator.tick(deltaTime);
		Logger.debug(this, Math.toDegrees(this.transform.getRotator().getAsEulerAngles().x),Math.toDegrees(this.transform.getRotator().getAsEulerAngles().y),Math.toDegrees(this.transform.getRotator().getAsEulerAngles().z));
		
		//Vector3f pos = PhysicsScene.targetBody.getWorldTransform(new com.bulletphysics.linearmath.Transform()).origin;
		//Quat4f rot = PhysicsScene.targetBody.getWorldTransform(new com.bulletphysics.linearmath.Transform()).getRotation(new Quat4f());
		//this.transform.setPosition(pos.x, pos.y, pos.z);
		//this.transform.getRotator().setQuaternion(new Quaternionf(rot.x, rot.y, rot.z, rot.w));
		
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
		float force = 20000;
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_SPACE).hashCode(), (e) -> {
			targetBody.activate(true);
			targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(0,1000,0)));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_UP).hashCode(), (e) -> {
			targetBody.activate(true);
			Vector3f forward = this.transform.getRotator().getForwardVector(new Vector3f());
			targetBody.setLinearVelocity(GeometryUtils.vector3fToJavaxVector3f(forward.mul(2)));
			targetBody.applyForce(GeometryUtils.vector3fToJavaxVector3f(forward.mul(force)), GeometryUtils.vector3fToJavaxVector3f(forward.mul(3f).add(new Vector3f(4,0,0))));
			//targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getForwardVector(new Vector3f()).mul(force)));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_DOWN).hashCode(), (e) -> {
			targetBody.activate(true);
			targetBody.applyForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getBackwardVector(new Vector3f()).mul(force)), GeometryUtils.vector3fToJavaxVector3f(new Vector3f()));
			//targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getBackwardVector(new Vector3f()).mul(force)));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_LEFT).hashCode(), (e) -> {
			targetBody.activate(true);
			targetBody.applyForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getLeftVector(new Vector3f()).mul(force)), GeometryUtils.vector3fToJavaxVector3f(new Vector3f()));
			//targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getLeftVector(new Vector3f()).mul(force)));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_RIGHT).hashCode(), (e) -> {
			targetBody.activate(true);
			targetBody.applyForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getRightVector(new Vector3f()).mul(force)), GeometryUtils.vector3fToJavaxVector3f(new Vector3f()));
			//targetBody.applyCentralForce(GeometryUtils.vector3fToJavaxVector3f(this.transform.getRotator().getRightVector(new Vector3f()).mul(force)));
		});
		
		Camera cam = Engine.getGame().getWorldScene().getActiveCamera();
		org.joml.Vector3f v = cam.getTransform().getRotator().getForwardVector(new org.joml.Vector3f()).mul(this.cameraMode).negate();
		org.joml.Vector3f me = this.transform.getPosition();
		cam.getTransform().setPosition(me.x + v.x, me.y + v.y + 3f, me.z + v.z);
	}
	
	@Override
	public void submitToRenderer(Renderer renderer) {
		this.animator.submitToRenderer(renderer);
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
