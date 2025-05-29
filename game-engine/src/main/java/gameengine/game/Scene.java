package gameengine.game;

import java.util.ArrayList;
import java.util.List;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.IScene;
import gameengine.engine.ITickable;
import gameengine.engine.renderer.Camera;
import gameengine.engine.renderer.IRenderPass;
import gameengine.engine.renderer.Projection;
import gameengine.engine.window.Window;

public class Scene implements IScene, ITickable {
	private List<IGameObject> objects;
	private Projection activeProjection;
	private Camera activeCamera;
	
	public Scene() {
		this.objects = new ArrayList<>();
		
		Window window = Engine.getWindow();
		this.activeProjection = new Projection(window.getWidth(), window.getHeight());
		this.activeCamera = new Camera();
	}

	@Override
	public void tick(float deltaTime) {
		this.activeCamera.DEBUGupdate();
		for( IGameObject object : this.objects ) {
			object.tick(deltaTime);
		}
	}

	@Override
	public void render(IRenderPass renderPass) {
		for( IGameObject object : this.objects ) {
			object.render(renderPass);
		}
	}
	
	public void addObject(IGameObject object) {
		this.objects.add(object);
		object.onCreate();
	}
	
	public void setActiveProjection(Projection projection) {
		this.activeProjection = projection;
	}
	
	@Override
	public Projection getActiveProjection() {
		return this.activeProjection;
	}
	
	@Override
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
	
}
