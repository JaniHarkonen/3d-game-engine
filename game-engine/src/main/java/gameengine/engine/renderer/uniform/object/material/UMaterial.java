package gameengine.engine.renderer.uniform.object.material;

import gameengine.engine.renderer.uniform.AUniformObject;
import gameengine.engine.renderer.uniform.UVector4f;

public class UMaterial extends AUniformObject<SSMaterial> {

	private UVector4f diffuse;
	
	public UMaterial(String name) {
		super(name);
		
		this.diffuse = new UVector4f();
		this.addField("diffuse", this.diffuse);
	}
	
	public UMaterial() {
		this("");
	}
	
	
	@Override
	public void update(SSMaterial value) {
		this.diffuse.update(value.diffuse);
	}
}
