package gameengine.engine.renderer.uniform.object.ptlight;

import gameengine.engine.renderer.uniform.AUniformObject;
import gameengine.engine.renderer.uniform.UFloat1;
import gameengine.engine.renderer.uniform.UVector3f;
import gameengine.engine.renderer.uniform.object.attenuation.UAttenuation;

public class UPointLight extends AUniformObject<SSPointLight> {

	private UVector3f position;
	private UVector3f color;
	private UFloat1 intensity;
	private UAttenuation attenuation;
	
	public UPointLight(String name) {
		super(name);
		
		this.position = new UVector3f();
		this.color = new UVector3f();
		this.intensity = new UFloat1();
		this.attenuation = new UAttenuation();
		
		this
		.addField("position", this.position)
		.addField("color", this.color)
		.addField("intensity", this.intensity)
		.addField("attenuation", this.attenuation);
	}
	
	
	
	public UPointLight() {
		this("");
	}

	
	@Override
	public void update(SSPointLight value) {
		this.position.update(value.position);
		this.color.update(value.color);
		this.intensity.update(value.intensity);
		this.attenuation.update(value.attenuation);
	}
}
