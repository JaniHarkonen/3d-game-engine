package gameengine.game;

import gameengine.engine.IGameObject;
import gameengine.engine.renderer.Mesh;

public class TestObject implements IGameObject {
	
	private Mesh mesh;
	
	public TestObject() {
		this.mesh = null;
	}
	
	
	@Override
	public void onCreate() {
        float[] positions = new float[]{
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
        };
        
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };
		
        this.mesh = new Mesh(positions, indices);
	}

	@Override
	public void tick(float deltaTime) {
		
	}

	@Override
	public void render() {
		this.mesh.render();
	}

}
