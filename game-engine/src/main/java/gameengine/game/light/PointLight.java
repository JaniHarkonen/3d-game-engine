package gameengine.game.light;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.renderer.IRenderStrategy;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.renderer.uniform.object.ptlight.SSPointLight;
import gameengine.engine.window.Input;

public class PointLight implements IGameObject {
	private class SceneRenderer implements IRenderStrategy<ScenePass> {

		@Override
		public void render(ScenePass renderPass) {
	        Vector4f aux = new Vector4f();
	        Vector3f lightPosition = new Vector3f();
	        aux.set(getPosition(), 1);
	        aux.mul(Engine.getGame().getWorldScene().getActiveCamera().getTransform().getAsMatrix());
	        lightPosition.set(aux.x, aux.y, aux.z);
	        pointLightStruct.position = lightPosition;
	        renderPass.uPointLights.update(pointLightStruct, pointLightIndex);
		}
	}
	
    private Attenuation attenuation;
    private Vector3f color;
    private Vector3f position;
    private float intensity;
    private int pointLightIndex;
    private SSPointLight pointLightStruct;
    private SceneRenderer sceneRenderer;

    public PointLight(Vector3f color, Vector3f position, float intensity, int pointLightIndex) {
        this.attenuation = new Attenuation(0, 0, 1);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
        this.pointLightIndex = pointLightIndex;
        this.pointLightStruct = new SSPointLight();
        this.sceneRenderer = new SceneRenderer();
        this.updateStruct();
    }

    
    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void tick(float deltaTime) {
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_KP_8).hashCode(), (e) -> {
			this.setPosition(this.getPosition().x, this.getPosition().y + deltaTime*10, this.getPosition().z);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_KP_2).hashCode(), (e) -> {
			this.setPosition(this.getPosition().x, this.getPosition().y - deltaTime*10, this.getPosition().z);
		});
	}

	@Override
	public void submitToRenderer(Renderer renderer) {
		renderer.getScenePass().preRender(this.sceneRenderer);
	}
    
    private void updateStruct() {
    	this.pointLightStruct.color = this.color;
    	this.pointLightStruct.position = this.position;
    	this.pointLightStruct.intensity = this.intensity;
    	this.pointLightStruct.attenuation = this.attenuation.getAsStruct();
    }

    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
        this.updateStruct();
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
        this.updateStruct();
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
        this.updateStruct();
    }
    
    public Attenuation getAttenuation() {
        return attenuation;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }

    public Vector3f getPosition() {
        return position;
    }
}
