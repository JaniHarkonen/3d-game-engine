package gameengine.game;

import java.util.ArrayList;
import java.util.List;

import gameengine.engine.IGameObject;
import gameengine.engine.IScene;
import gameengine.engine.ITickable;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.component.Camera;

public class Scene implements IScene, ITickable {
	private List<IGameObject> objects;
	private Camera activeCamera;
	
	public Scene() {
		this.objects = new ArrayList<>();
	}

	@Override
	public void tick(float deltaTime) {
		for( IGameObject object : this.objects ) {
			object.tick(deltaTime);
		}
	}

	@Override
	public void submitToRenderer(Renderer renderer) {
		for( IGameObject object : this.objects ) {
			object.submitToRenderer(renderer);
		}
	}
	
	public void addObject(IGameObject object) {
		this.objects.add(object);
		object.onCreate();
	}
	
	public void setActiveCamera(Camera camera) {
		this.activeCamera = camera;
	}
	
	@Override
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
}
