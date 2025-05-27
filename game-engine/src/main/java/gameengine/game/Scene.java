package gameengine.game;

import java.util.ArrayList;
import java.util.List;

import gameengine.engine.IGameObject;
import gameengine.engine.IRenderable;
import gameengine.engine.ITickable;

public class Scene implements ITickable, IRenderable {
	private List<IGameObject> objects;
	
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
	public void render() {
		for( IGameObject object : this.objects ) {
			object.render();
		}
	}
	
	public void addObject(IGameObject object) {
		this.objects.add(object);
		object.onCreate();
	}
}
