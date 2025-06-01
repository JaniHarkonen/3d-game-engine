package gameengine.engine.renderer.uniform.object.amlight;

import gameengine.engine.renderer.uniform.AUniformObject;
import gameengine.engine.renderer.uniform.UFloat1;
import gameengine.engine.renderer.uniform.UVector3f;

public class UAmbientLight extends AUniformObject<SSAmbientLight> {

	private UVector3f color;
	private UFloat1 intensity;
	
	public UAmbientLight(String name) {
		super(name);
		
		this.intensity = new UFloat1();
		this.color = new UVector3f();
		
		this
		.addField("intensity", this.intensity)
		.addField("color", this.color);
	}
	
	public UAmbientLight() {
		this("");
	}
	

	@Override
	public void update(SSAmbientLight value) {
		this.intensity.update(value.intensity);
		this.color.update(value.color);
	}
}
