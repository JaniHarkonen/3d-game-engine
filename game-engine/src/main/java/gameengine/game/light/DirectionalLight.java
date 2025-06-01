package gameengine.game.light;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.renderer.uniform.object.drlight.SSDirectionalLight;
import gameengine.engine.window.Input;

public class DirectionalLight implements IGameObject {

    private Vector3f color;
    private Vector3f direction;
    private float intensity;
    private SSDirectionalLight directionalLightStruct;

    public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
        this.directionalLightStruct = new SSDirectionalLight();
        this.updateStruct();
    }
    
    
    private void updateStruct() {
    	this.directionalLightStruct.color = this.color;
    	this.directionalLightStruct.intensity = this.intensity;
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
        this.updateStruct();
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
        this.updateStruct();
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
        this.updateStruct();
    }
    
    public Vector3f getColor() {
        return color;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public float getIntensity() {
        return intensity;
    }


	@Override
	public void tick(float deltaTime) {
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_KP_DIVIDE).hashCode(), (e) -> {
			this.setDirection(this.getDirection().add(deltaTime*10, 0, 0));
		});
		
		Engine.getWindow().getInput().DEBUGmapInput(new Input.Event(Input.DEVICE_KEYBOARD, Input.EVENT_HOLD, GLFW.GLFW_KEY_KP_MULTIPLY).hashCode(), (e) -> {
			this.setDirection(this.getDirection().add(-deltaTime*10, 0, 0));
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
		Matrix4f cameraMatrix = Engine.getGame().getWorldScene().getActiveCamera().getTransform().getAsMatrix();
        Vector4f auxDir = new Vector4f(this.getDirection(), 0);
        auxDir.mul(cameraMatrix);
        Vector3f dir = new Vector3f(auxDir.x, auxDir.y, auxDir.z);
        this.directionalLightStruct.direction = dir;
        renderPass.uDirectionalLight.update(this.directionalLightStruct);
	}
}
