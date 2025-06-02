package gameengine.game.component.light;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import gameengine.engine.Engine;
import gameengine.engine.renderer.IRenderStrategy;
import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.renderer.uniform.object.IHasStruct;
import gameengine.engine.renderer.uniform.object.drlight.SSDirectionalLight;

public class DirectionalLight implements IRenderable, IHasStruct {
	private class SceneRenderer implements IRenderStrategy<ScenePass> {
		@Override
		public void render(ScenePass renderPass) {
	        renderPass.uDirectionalLight.update(getAsStruct());
		}
	}

    private Vector3f direction;
    private LightProperties lightProperties;
    private SSDirectionalLight directionalLightStruct;
    private SceneRenderer sceneRenderer;

    public DirectionalLight(Vector3f color, float intensity, Vector3f direction) {
        this.direction = direction;
        this.lightProperties = new LightProperties(color, intensity);
        this.directionalLightStruct = new SSDirectionalLight();
        this.sceneRenderer = new SceneRenderer();
    }
 

	@Override
	public void submitToRenderer(Renderer renderer) {
		renderer.getScenePass().preRender(this.sceneRenderer);
	}
	
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public Vector3f getDirection() {
        return direction;
    }
    
    public LightProperties getLightProperties() {
    	return this.lightProperties;
    }
    
    public Vector3f getDirectionInCameraCoordinates() {
    	Matrix4f cameraMatrix = Engine.getGame().getWorldScene().getActiveCamera().getTransform().getAsMatrix();
        Vector4f direction = new Vector4f(getDirection(), 0);
        direction.mul(cameraMatrix);
        return new Vector3f(direction.x, direction.y, direction.z);
    }

	@Override
	public SSDirectionalLight getAsStruct() {
		this.directionalLightStruct.color = this.lightProperties.getColor();
		this.directionalLightStruct.intensity = this.lightProperties.getIntensity();
    	this.directionalLightStruct.direction = this.getDirectionInCameraCoordinates();
		return this.directionalLightStruct;
	}
}
