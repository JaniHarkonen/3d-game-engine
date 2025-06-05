package gameengine.engine.renderer.uniform.object.drlight;

import gameengine.engine.renderer.uniform.AUniformObject;
import gameengine.engine.renderer.uniform.UFloat1;
import gameengine.engine.renderer.uniform.UVector3f;

public class UDirectionalLight extends AUniformObject<SSDirectionalLight> {
    private UVector3f color;
    private UFloat1 intensity;
    private UVector3f position;
	
	public UDirectionalLight(String name) {
		super(name);
		
		this.color = new UVector3f();
		this.intensity = new UFloat1();
		this.position = new UVector3f();
		
		this
		.addField("color", this.color)
		.addField("intensity", this.intensity)
		.addField("position", this.position);
	}
	
	public UDirectionalLight() {
		this("");
	}

	
	@Override
	public void update(SSDirectionalLight value) {
		this.color.update(value.color);
		this.intensity.update(value.intensity);
		this.position.update(value.position);
	}
}
