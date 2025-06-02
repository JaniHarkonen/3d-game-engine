package gameengine.engine.renderer.component;

import gameengine.engine.renderer.IRenderStrategy;
import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.game.component.CameraTransform;
import gameengine.game.component.Transform;

public class Camera implements IRenderable {
	private class SceneRenderer implements IRenderStrategy<ScenePass> {

		@Override
		public void render(ScenePass renderPass) {
			renderPass.uProjection.update(getProjection().getAsMatrix());
			renderPass.uCamera.update(getTransform().getAsMatrix());
		}
	}
	
	private Transform transform;
	private Projection projection;
	private SceneRenderer sceneRenderer;

    public Camera(Projection projection) {
    		// Special transform whose matrix is translated and rotated slightly different
        this.transform = new CameraTransform();
        this.projection = projection;
        this.sceneRenderer = new SceneRenderer();
    }

    
    @Override
	public void submitToRenderer(Renderer renderer) {
    	renderer.getScenePass().preRender(this.sceneRenderer);
	}
    
    public Transform getTransform() {
    	return this.transform;
    }
    
    public Projection getProjection() {
    	return this.projection;
    }
}
