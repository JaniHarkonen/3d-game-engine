package gameengine.test;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.asset.Animation;
import gameengine.engine.physics.IHasTransform;
import gameengine.engine.physics.PhysicsScene;
import gameengine.engine.physics.Transform;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.component.Camera;
import gameengine.engine.window.Input;
import gameengine.game.component.Animator;
import gameengine.game.component.Model;

public class TestPlayer implements IGameObject, IHasTransform {
	
	private Transform transform;
	private Model model;
	private Animator animator;
	private float cameraMode;

    public TestPlayer(Model model) {
    	this.transform = new Transform();
    	model.getTransform().possess(this);
    	this.animator = new Animator(model);
    	this.cameraMode = 6f;
    }

    
	@Override
	public void onCreate() {
	}

	@Override
	public void tick(float deltaTime) {
		this.animator.tick(deltaTime);
		Vector3f pos = PhysicsScene.targetBody.getWorldTransform(new com.bulletphysics.linearmath.Transform()).origin;
		Quat4f rot = PhysicsScene.targetBody.getWorldTransform(new com.bulletphysics.linearmath.Transform()).getRotation(new Quat4f());
		this.transform.setPosition(pos.x, pos.y, pos.z);
		this.transform.getRotator().setQuaternion(new Quaternionf(rot.x, rot.y, rot.z, rot.w));
		
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
}
