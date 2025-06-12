package gameengine.test;

import org.lwjgl.glfw.GLFW;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.physics.Transform;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.component.Projection;
import gameengine.engine.renderer.component.ThirdPersonCamera;
import gameengine.engine.window.Input;
import gameengine.engine.window.Window;
import gameengine.game.Car;

public class TestCamera implements IGameObject {	
	private ThirdPersonCamera camera;
	private Car car;
	
	public TestCamera(Car car) {
		Projection projection = new Projection();
    	this.camera = new ThirdPersonCamera(projection);
    	this.car = car;
		//camera.getProjection().setAspectRatio(1 / 1);
		camera.getProjection().setFOV(70f);
		Engine.getGame().getWorldScene().setActiveCamera(camera);
	}

	@Override
	public void tick(float deltaTime) {
    	Window window = Engine.getWindow();
    	this.camera.tick(deltaTime);
    	
    	Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_UP).hashCode(), (e) -> {
    		this.car.accelerate(true);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_DOWN).hashCode(), (e) -> {
			this.car.reverse(true);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_LEFT).hashCode(), (e) -> {
			this.car.steerLeft(true);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_RIGHT).hashCode(), (e) -> {
			this.car.steerRight(true);
		});
		
    	Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_RELEASE, GLFW.GLFW_KEY_UP).hashCode(), (e) -> {
    		this.car.accelerate(false);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_RELEASE, GLFW.GLFW_KEY_DOWN).hashCode(), (e) -> {
			this.car.reverse(false);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_RELEASE, GLFW.GLFW_KEY_LEFT).hashCode(), (e) -> {
			this.car.steerLeft(false);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_RELEASE, GLFW.GLFW_KEY_RIGHT).hashCode(), (e) -> {
			this.car.steerRight(false);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_PRESS, GLFW.GLFW_KEY_SPACE).hashCode(), (e) -> {
			this.car.brake(true);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_RELEASE, GLFW.GLFW_KEY_SPACE).hashCode(), (e) -> {
			this.car.brake(false);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_RELEASE, GLFW.GLFW_KEY_SPACE).hashCode(), (e) -> {
			this.car.releaseBrake();
		});
    	
    	// Mouse pan
    	window.getInput().DEBUGmapInput(24000, (e) -> {
    		this.camera.getTransform().getRotator().rotate((float) e.mouseDeltaY/10, (float) e.mouseDeltaX/10);
    	});
	}

	@Override
	public void submitToRenderer(Renderer renderer) {
		this.camera.submitToRenderer(renderer);
	}

	@Override
	public void onCreate() {/*
		Projection projection = new Projection();
    	ThirdPersonCamera camera = new ThirdPersonCamera(projection);
		//camera.getProjection().setAspectRatio(1 / 1);
		camera.getProjection().setFOV(70f);
		Engine.getGame().getWorldScene().setActiveCamera(camera);*/
		/*Projection projection = new Projection();
		this.camera = new ThirdPersonCamera(projection);
		this.camera.getProjection().setAspectRatio(1 / 1);
		this.camera.getProjection().setFOV(70f);
		Engine.getGame().getWorldScene().setActiveCamera(this.camera);
		
		//this.transform = new Transform();
		this.transform = new Camera.Transform();*/
		//this.camera.getTransform().possess(this);
		//this.transform.setPosition(10, 10, 10);
	}
	
	public ThirdPersonCamera getCamera() {
		return this.camera;
	}
}
