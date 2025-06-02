package gameengine.game.light;

import org.joml.Vector3f;

import gameengine.engine.renderer.IRenderStrategy;
import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.renderer.uniform.object.IHasStruct;
import gameengine.engine.renderer.uniform.object.amlight.SSAmbientLight;

public class AmbientLight implements IRenderable, IHasStruct {
	private class SceneRenderer implements IRenderStrategy<ScenePass> {
		
		@Override
		public void render(ScenePass renderPass) {
			renderPass.uAmbientLight.update(getAsStruct());
		}
	}

    private LightProperties lightProperties;
    private SSAmbientLight ambientLightStruct;
    private SceneRenderer sceneRenderer;

    
    public AmbientLight(Vector3f color, float intensity) {
        this.lightProperties = new LightProperties(color, intensity);
        this.ambientLightStruct = new SSAmbientLight();
        this.sceneRenderer = new SceneRenderer();
    }
    

	@Override
	public void submitToRenderer(Renderer renderer) {
		renderer.getScenePass().preRender(this.sceneRenderer);
	}
    
    @Override
    public SSAmbientLight getAsStruct() {
    	this.ambientLightStruct.color = this.lightProperties.getColor();
    	this.ambientLightStruct.intensity = this.lightProperties.getIntensity();
    	return this.ambientLightStruct;
    }
    
    public LightProperties getProperties() {
    	return this.lightProperties;
    }
}
