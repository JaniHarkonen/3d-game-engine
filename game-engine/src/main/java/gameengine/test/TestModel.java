package gameengine.test;

import gameengine.engine.IGameObject;
import gameengine.engine.renderer.Renderer;
import gameengine.game.component.Model;
import gameengine.game.component.Transform;

public class TestModel implements IGameObject {
	
	private Transform transform;
	private Model model;

    public TestModel(Model model) {
    	this.transform = new Transform();
    	this.model = model;
    }

    
	@Override
	public void onCreate() {
		this.model.getTransform().possess(this.transform);
	}

	@Override
	public void tick(float deltaTime) {
		
	}
	
	@Override
	public void submitToRenderer(Renderer renderer) {
		this.model.submitToRenderer(renderer);
	}
	
	public Transform getTransform() {
		return this.transform;
	}
}
