package gameengine.test;

import org.joml.Vector3f;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.component.Camera;
import gameengine.engine.renderer.component.Projection;
import gameengine.engine.window.Window;
import gameengine.game.component.IHasTransform;
import gameengine.game.component.Transform;

public class TestCamera implements IGameObject, IHasTransform {	
	private Camera camera;
	private Transform transform;

	@Override
	public void tick(float deltaTime) {
    	float speed = 10f;
    	Window window = Engine.getWindow();
    	
    	// W
    	window.getInput().DEBUGmapInput(12137, (e) -> {
    		Vector3f c = this.transform.getPosition();
    		Vector3f v = new Vector3f();
    		this.transform.getRotator().getForwardVector(v).mul(deltaTime * speed);
    		this.transform.setPosition(c.x + v.x, c.y + v.y, c.z + v.z);
    	});

    	// A
    	window.getInput().DEBUGmapInput(12115, (e) -> {
    		Vector3f c = this.transform.getPosition();
    		Vector3f v = new Vector3f();
    		this.transform.getRotator().getLeftVector(v).mul(deltaTime * speed);
    		this.transform.setPosition(c.x + v.x, c.y + v.y, c.z + v.z);
    	});
    	
    	// S
    	window.getInput().DEBUGmapInput(12133, (e) -> {
    		Vector3f c = this.transform.getPosition();
    		Vector3f v = new Vector3f();
    		this.transform.getRotator().getBackwardVector(v).mul(deltaTime * speed);
    		this.transform.setPosition(c.x + v.x, c.y + v.y, c.z + v.z);
    	});
    	
    	// D
    	window.getInput().DEBUGmapInput(12118, (e) -> {
    		Vector3f c = this.transform.getPosition();
    		Vector3f v = new Vector3f();
    		this.transform.getRotator().getRightVector(v).mul(deltaTime * speed);
    		this.transform.setPosition(c.x + v.x, c.y + v.y, c.z + v.z);
    	});
    	
    	// Mouse pan
    	window.getInput().DEBUGmapInput(24000, (e) -> {
    		this.transform.getRotator().rotate((float) Math.toRadians(e.mouseDeltaY * 0.1f), (float) Math.toRadians(e.mouseDeltaX * 0.1f), 0);
    	});
	}

	@Override
	public void submitToRenderer(Renderer renderer) {
		this.camera.submitToRenderer(renderer);
	}

	@Override
	public void onCreate() {
		Projection projection = new Projection();
		this.camera = new Camera(projection);
		this.camera.getProjection().setAspectRatio(1/1);
		this.camera.getProjection().setFOV(75f);
		Engine.getGame().getWorldScene().setActiveCamera(this.camera);
		
		this.transform = new Transform();
		this.camera.getTransform().possess(this);
	}

	@Override
	public Transform getTransform() {
		return this.transform;
	}
}
