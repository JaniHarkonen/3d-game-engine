package gameengine.engine.renderer.uniform.object.drlight;

import gameengine.engine.renderer.uniform.AUniformObject;
import gameengine.engine.renderer.uniform.UFloat1;
import gameengine.engine.renderer.uniform.UVector3f;

public class UDirectionalLight extends AUniformObject<SSDirectionalLight> {
    private UVector3f color;
    private UVector3f direction;
	private UFloat1 intensity;
	
	public UDirectionalLight(String name) {
		super(name);
		
		this.color = new UVector3f();
		this.direction = new UVector3f();
		this.intensity = new UFloat1();
		
		this
		.addField("color", this.color)
		.addField("direction", this.direction)
		.addField("intensity", this.intensity);
	}
	
	
	
	public UDirectionalLight() {
		this("");
	}

	
	@Override
	public void update(SSDirectionalLight value) {
		this.color.update(value.color);
		this.direction.update(value.direction);
		this.intensity.update(value.intensity);
	}
}
