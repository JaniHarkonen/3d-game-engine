package gameengine.game.component.light;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import gameengine.engine.Engine;
import gameengine.engine.renderer.CascadeShadowPass;
import gameengine.engine.renderer.IRenderStrategy;
import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.renderer.uniform.object.IHasStruct;
import gameengine.engine.renderer.uniform.object.drlight.SSDirectionalLight;
import gameengine.game.component.IHasTransform;
import gameengine.game.component.Transform;

public class DirectionalLight implements IRenderable, IHasStruct, IHasTransform {
	private class SceneRenderer implements IRenderStrategy<ScenePass> {
		@Override
		public void render(ScenePass renderPass) {
	        renderPass.uDirectionalLight.update(getAsStruct());
		}
	}
	
	private class CascadeShadowRenderer implements IRenderStrategy<CascadeShadowPass> {
		@Override
		public void render(CascadeShadowPass renderPass) {
	        renderPass.directionalLight = new Vector3f(getTransform().getPosition());
		}
	}

    private Transform transform;
    private LightProperties lightProperties;
    private SSDirectionalLight directionalLightStruct;
    private SceneRenderer sceneRenderer;
    private CascadeShadowRenderer cascadeShadowRenderer;

    public DirectionalLight(Vector3f color, float intensity) {
        this.transform = new Transform();
        this.lightProperties = new LightProperties(color, intensity);
        this.directionalLightStruct = new SSDirectionalLight();
        this.sceneRenderer = new SceneRenderer();
        this.cascadeShadowRenderer = new CascadeShadowRenderer();
    }
 

	@Override
	public void submitToRenderer(Renderer renderer) {
		renderer.getCascadeShadowPass().preRender(this.cascadeShadowRenderer);
		renderer.getScenePass().preRender(this.sceneRenderer);
	}
    
    public LightProperties getLightProperties() {
    	return this.lightProperties;
    }
    
    public Vector3f getPositionInCameraSpace() {
    	Matrix4f cameraMatrix = Engine.getGame().getWorldScene().getActiveCamera().getTransform().getAsMatrix();
        Vector4f position = new Vector4f(this.transform.getPosition(), 0);
        position.mul(cameraMatrix);
        return new Vector3f(position.x, position.y, position.z);
    }

	@Override
	public SSDirectionalLight getAsStruct() {
		this.directionalLightStruct.color = this.lightProperties.getColor();
		this.directionalLightStruct.intensity = this.lightProperties.getIntensity();
    	this.directionalLightStruct.position = this.getPositionInCameraSpace();
		return this.directionalLightStruct;
	}


	@Override
	public Transform getTransform() {
		return this.transform;
	}
}
