package gameengine.engine.renderer.uniform.object.amlight;

import gameengine.engine.renderer.uniform.AUniformObject;
import gameengine.engine.renderer.uniform.UFloat1;
import gameengine.engine.renderer.uniform.UVector3f;

public class UAmbientLight extends AUniformObject<SSAmbientLight> {

	private UFloat1 factor;
	private UVector3f color;
	
	public UAmbientLight(String name) {
		super(name);
		
		this.factor = new UFloat1();
		this.color = new UVector3f();
		
		this
		.addField("factor", this.factor)
		.addField("color", this.color);
	}
	
	public UAmbientLight() {
		this("");
	}
	

	@Override
	public void update(SSAmbientLight value) {
		this.factor.update(value.factor);
		this.color.update(value.color);
	}
}
