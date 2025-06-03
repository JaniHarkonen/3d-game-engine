package gameengine.test;

import org.lwjgl.glfw.GLFW;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.asset.Animation;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.window.Input;
import gameengine.game.component.Animator;
import gameengine.game.component.IHasTransform;
import gameengine.game.component.Model;
import gameengine.game.component.Transform;

public class TestPlayer implements IGameObject, IHasTransform {
	
	private Transform transform;
	private Animator animator;

    public TestPlayer(Model model) {
    	this.transform = new Transform();
    	model.getTransform().possess(this);
    	this.animator = new Animator(model);
    }

    
	@Override
	public void onCreate() {
	}

	@Override
	public void tick(float deltaTime) {
		this.animator.tick(deltaTime);
		
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
