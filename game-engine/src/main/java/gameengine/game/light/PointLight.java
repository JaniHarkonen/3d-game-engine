package gameengine.game.light;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.renderer.uniform.object.ptlight.SSPointLight;
import gameengine.engine.window.Input;

public class PointLight implements IGameObject {
    private Attenuation attenuation;
    private Vector3f color;
    private Vector3f position;
    private float intensity;
    private int pointLightIndex;
    private SSPointLight pointLightStruct;

    public PointLight(Vector3f color, Vector3f position, float intensity, int pointLightIndex) {
        this.attenuation = new Attenuation(0, 0, 1);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
        this.pointLightIndex = pointLightIndex;
        this.pointLightStruct = new SSPointLight();
        this.updateStruct();
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
		renderer.getScenePass().submit(this);
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void render(ScenePass renderPass) {
        Vector4f aux = new Vector4f();
        Vector3f lightPosition = new Vector3f();
        //Vector3f color = new Vector3f();
        //float intensity = 0.0f;
        //float constant = 0.0f;
        //float linear = 0.0f;
        //float exponent = 0.0f;
        aux.set(this.getPosition(), 1);
        aux.mul(Engine.getGame().getWorldScene().getActiveCamera().getTransform().getAsMatrix());
        lightPosition.set(aux.x, aux.y, aux.z);
        //color.set(this.getColor());
        //intensity = this.getIntensity();
        /*Attenuation attenuation = this.getAttenuation();
        constant = attenuation.getConstant();
        linear = attenuation.getLinear();
        exponent = attenuation.getExponent();*/
        
        this.pointLightStruct.position = lightPosition;
        renderPass.uPointLights.update(this.pointLightStruct, this.pointLightIndex);
        /*uniformsMap.setUniform(prefix + ".position", lightPosition);
        uniformsMap.setUniform(prefix + ".color", color);
        uniformsMap.setUniform(prefix + ".intensity", intensity);
        uniformsMap.setUniform(prefix + ".att.constant", constant);
        uniformsMap.setUniform(prefix + ".att.linear", linear);
        uniformsMap.setUniform(prefix + ".att.exponent", exponent);*/
	}
}
