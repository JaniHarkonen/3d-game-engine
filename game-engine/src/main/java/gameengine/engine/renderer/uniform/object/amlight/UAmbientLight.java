package gameengine.engine.renderer.uniform.object.amlight;

import gameengine.engine.renderer.uniform.AUniformObject;
import gameengine.engine.renderer.uniform.UFloat1;
import gameengine.engine.renderer.uniform.UVector3f;

public class UAmbientLight extends AUniformObject<SSAmbientLight> {
	
	private UVector3f color;
	private UFloat1 intensity;
	
	public UAmbientLight(String name) {
		super(name);
		
		this.color = new UVector3f();
		this.intensity = new UFloat1();
		
		this
		.addField("color", this.color)
		.addField("intensity", this.intensity);
	}
	
	public UAmbientLight() {
		this("");
	}
	

	@Override
	public void update(SSAmbientLight value) {
		this.color.update(value.color);
		this.intensity.update(value.intensity);
	}
}
