package gameengine.test;

import gameengine.engine.IGameObject;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
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
	}

	@Override
	public void tick(float deltaTime) {
		
	}
	
	@Override
	public void submitToRenderer(Renderer renderer) {
		renderer.getScenePass().submit(this);
	}

	@Override
	public void render(ScenePass renderPass) {
		renderPass.uObject.update(this.transform.getAsMatrix());
		this.model.render(renderPass);
	}
	
	public Transform getTransform() {
		return this.transform;
	}
}
