package gameengine.game.light;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.renderer.uniform.object.amlight.SSAmbientLight;
import gameengine.engine.window.Input;

public class AmbientLight implements IGameObject {

    private Vector3f color;
    private float intensity;
    private SSAmbientLight ambientLightStruct;

    public AmbientLight(float intensity, Vector3f color) {
        this.intensity = intensity;
        this.color = color;
        this.ambientLightStruct = new SSAmbientLight();
        this.updateStruct();
    }

    public AmbientLight() {
        this(0.25f, new Vector3f(1.0f, 1.0f, 1.0f));
    }

    
    private void updateStruct() {
    	this.ambientLightStruct.color = this.color;
    	this.ambientLightStruct.intensity = this.intensity;
    }
    
    public Vector3f getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setColor(float r, float g, float b) {
        color.set(r, g, b);
        this.updateStruct();
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
        this.updateStruct();
    }

	@Override
	public void tick(float deltaTime) {
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_KP_ADD).hashCode(), (e) -> {
			this.setIntensity(this.getIntensity() + deltaTime);
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_KP_SUBTRACT).hashCode(), (e) -> {
			this.setIntensity(this.getIntensity() - deltaTime);
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
		renderPass.uAmbientLight.update(this.ambientLightStruct);
	}
}
