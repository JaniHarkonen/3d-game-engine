package gameengine.test;

import gameengine.engine.IGameObject;
import gameengine.engine.physics.IHasTransform;
import gameengine.engine.physics.Transform;
import gameengine.engine.renderer.Renderer;
import gameengine.game.component.Model;

public class TestModel implements IGameObject, IHasTransform {
	
	private Transform transform;
	private Model model;

    public TestModel(Model model) {
    	this.transform = new Transform();
    	this.model = model;
    }

    
	@Override
	public void onCreate() {
		this.model.getTransform().possess(this);
	}

	@Override
	public void tick(float deltaTime) {
		
	}
	
	@Override
	public void submitToRenderer(Renderer renderer) {
		this.model.submitToRenderer(renderer);
	}
	
	@Override
	public Transform getTransform() {
		return this.transform;
	}
}
