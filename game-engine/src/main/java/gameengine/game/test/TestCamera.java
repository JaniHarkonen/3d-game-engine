package gameengine.game.test;

import org.joml.Vector3f;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.renderer.Camera;
import gameengine.engine.renderer.Projection;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.window.Window;
import gameengine.game.Transform;

public class TestCamera implements IGameObject {
	
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
		// TODO Auto-generated method stub
	}

	@Override
	public void renderShadow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isShadowEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreate() {
		Window window = Engine.getWindow();
		Projection projection = new Projection(window.getWidth(), window.getHeight());
		this.camera = new Camera(projection);
		Engine.getGame().getWorldScene().setActiveCamera(this.camera);
		
		this.transform = new Transform();
		this.camera.getTransform().possess(this.transform);
	}

	@Override
	public void render(ScenePass renderPass) {
		// TODO Auto-generated method stub
		
	}
}
