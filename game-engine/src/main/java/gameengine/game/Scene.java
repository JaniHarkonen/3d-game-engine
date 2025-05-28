package gameengine.game;

import java.util.ArrayList;
import java.util.List;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.IScene;
import gameengine.engine.ITickable;
import gameengine.engine.renderer.Projection;
import gameengine.engine.window.Window;

public class Scene implements IScene, ITickable {
	private List<IGameObject> objects;
	private Projection activeProjection;
	
	public Scene() {
		this.objects = new ArrayList<>();
		
		Window window = Engine.getInstance().getWindow();
		this.activeProjection = new Projection(window.getWidth(), window.getHeight());
	}

	@Override
	public void tick(float deltaTime) {
		for( IGameObject object : this.objects ) {
			object.tick(deltaTime);
		}
	}

	@Override
	public void render() {
		for( IGameObject object : this.objects ) {
			object.render();
		}
	}
	
	public void addObject(IGameObject object) {
		this.objects.add(object);
		object.onCreate();
	}
	
	public void setActiveProjection(Projection projection) {
		this.activeProjection = projection;
	}
	
	public Projection getActiveProjection() {
		return this.activeProjection;
	}
}
