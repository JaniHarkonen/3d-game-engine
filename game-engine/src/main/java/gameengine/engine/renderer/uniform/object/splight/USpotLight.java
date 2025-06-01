package gameengine.engine.renderer.uniform.object.splight;

import gameengine.engine.renderer.uniform.AUniformObject;
import gameengine.engine.renderer.uniform.UFloat1;
import gameengine.engine.renderer.uniform.UVector3f;
import gameengine.engine.renderer.uniform.object.ptlight.UPointLight;

public class USpotLight extends AUniformObject<SSSpotLight> {

	private UPointLight pointLight;
	private UVector3f cone;
	private UFloat1 threshold;
	
	public USpotLight(String name) {
		super(name);
		
		this.pointLight = new UPointLight();
		this.cone = new UVector3f();
		this.threshold = new UFloat1();
		
		this
		.addField("pointLight", this.pointLight)
		.addField("cone", this.cone)
		.addField("threshold", this.threshold);
	}
	
	
	
	public USpotLight() {
		this("");
	}

	
	@Override
	public void update(SSSpotLight value) {
		this.pointLight.update(value.pointLight);
		this.cone.update(value.cone);
		this.threshold.update(value.threshold);
	}
}
