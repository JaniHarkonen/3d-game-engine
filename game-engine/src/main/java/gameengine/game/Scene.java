package gameengine.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import gameengine.engine.IGameObject;
import gameengine.engine.IScene;
import gameengine.engine.physics.IPhysicsObject;
import gameengine.engine.physics.PhysicsScene;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.component.Camera;
import gameengine.game.component.light.AmbientLight;
import gameengine.game.component.light.DirectionalLight;

public class Scene implements IScene {
	private List<IGameObject> objects;
	private PhysicsScene physicsScene;
	private Camera activeCamera;
	private AmbientLight ambientLight;
	private DirectionalLight directionalLight;
	
	public Scene() {
		this.objects = new ArrayList<>();
		this.physicsScene = new PhysicsScene();
		this.activeCamera = null;
		this.ambientLight = new AmbientLight(new Vector3f(1, 1, 1), 0.5f);
		this.directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 1.0f);
		this.directionalLight.getTransform().setPosition(100, 100, 100);
	}

	
	@Override
	public void tick(float deltaTime) {
		for( IGameObject object : this.objects ) {
			object.tick(deltaTime);
		}
	}

	@Override
	public void submitToRenderer(Renderer renderer) {
		this.ambientLight.submitToRenderer(renderer);
		this.directionalLight.submitToRenderer(renderer);
		
		for( IGameObject object : this.objects ) {
			object.submitToRenderer(renderer);
		}
	}
	
	public void addObject(IGameObject object) {
		this.objects.add(object);
		object.onCreate();
	}
	
	public void addObject(IPhysicsObject object) {
		this.addObject(object);
		this.physicsScene
	}
	
	public void setActiveCamera(Camera camera) {
		this.activeCamera = camera;
	}
	
	@Override
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
}
