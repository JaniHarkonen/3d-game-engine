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

    private Vector3f color;
    private float intensity;
    private SSAmbientLight ambientLightStruct;
    private SceneRenderer sceneRenderer;

    
    public AmbientLight(Vector3f color, float intensity) {
        this.intensity = intensity;
        this.color = color;
        this.ambientLightStruct = new SSAmbientLight();
        this.sceneRenderer = new SceneRenderer();
    }
    

	@Override
	public void submitToRenderer(Renderer renderer) {
		renderer.getScenePass().preRender(this.sceneRenderer);
	}
    
    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
    
    public Vector3f getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }
    
    @Override
    public SSAmbientLight getAsStruct() {
    	this.ambientLightStruct.color = this.color;
    	this.ambientLightStruct.intensity = this.intensity;
    	return this.ambientLightStruct;
    }
}
