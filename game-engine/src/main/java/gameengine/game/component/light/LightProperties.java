package gameengine.game.component.light;

import org.joml.Vector3f;

public class LightProperties {
	private Vector3f color;
	private float intensity;
    
    public LightProperties(float r, float g, float b, float intensity) {
    	this.color = new Vector3f(r, g, b);
    	this.intensity = intensity;
    }
    
    public LightProperties(Vector3f color, float intensity) {
    	this(color.x, color.y, color.z, intensity);
    }
    
    public LightProperties() {
    	this(1, 1, 1, 1);
    }
    
    
    public void setColor(float r, float g, float b) {
    	this.color.set(r, g, b);
    }
    
    public void setIntensity(float intensity) {
    	this.intensity = intensity;
    }
    
    public Vector3f getColor() {
    	return this.color;
    }
    
    public float getIntensity() {
    	return this.intensity;
    }
}
