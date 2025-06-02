package gameengine.game.light;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.renderer.IRenderStrategy;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.renderer.uniform.object.IHasStruct;
import gameengine.engine.renderer.uniform.object.ptlight.SSPointLight;
import gameengine.engine.window.Input;
import gameengine.game.component.IHasTransform;
import gameengine.game.component.Transform;

public class PointLight implements IGameObject, IHasStruct, IHasTransform {
	private class SceneRenderer implements IRenderStrategy<ScenePass> {

		@Override
		public void render(ScenePass renderPass) {
	        renderPass.uPointLights.update(getAsStruct(), getPointLightIndex());
		}
	}
	
    private Attenuation attenuation;
    private int pointLightIndex;
    private Transform transform;
    private LightProperties lightProperties;
    private SSPointLight pointLightStruct;
    private SceneRenderer sceneRenderer;

    public PointLight(Vector3f color, float intensity, int pointLightIndex) {
        this.attenuation = new Attenuation(0, 0, 1);
        this.pointLightIndex = pointLightIndex;
        this.transform = new Transform();
        this.lightProperties = new LightProperties();
        this.pointLightStruct = new SSPointLight();
        this.sceneRenderer = new SceneRenderer();
    }

    
    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void tick(float deltaTime) {
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_KP_8).hashCode(), (e) -> {
			this.getTransform().shift(0, deltaTime * 10, 0);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_KP_2).hashCode(), (e) -> {
			this.getTransform().shift(0, -deltaTime * 10, 0);
		});
	}

	@Override
	public void submitToRenderer(Renderer renderer) {
		renderer.getScenePass().preRender(this.sceneRenderer);
	}

    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }
    
    public Attenuation getAttenuation() {
        return attenuation;
    }
    
    public int getPointLightIndex() {
    	return this.pointLightIndex;
    }
    
    public LightProperties getLightProperties() {
    	return this.lightProperties;
    }
    
    private Vector3f getPositionInCameraCoordinates() {
    	Vector4f position = new Vector4f(this.getTransform().getPosition(), 1);
    	position.mul(Engine.getGame().getWorldScene().getActiveCamera().getTransform().getAsMatrix());
        return new Vector3f(position.x, position.y, position.z);
    }

	@Override
	public SSPointLight getAsStruct() {
    	this.pointLightStruct.position = this.getPositionInCameraCoordinates();
    	this.pointLightStruct.color = this.lightProperties.getColor();
    	this.pointLightStruct.intensity = this.lightProperties.getIntensity();
    	this.pointLightStruct.attenuation = this.attenuation.getAsStruct();
		return this.pointLightStruct;
	}
	
	@Override
	public Transform getTransform() {
		return this.transform;
	}
}
