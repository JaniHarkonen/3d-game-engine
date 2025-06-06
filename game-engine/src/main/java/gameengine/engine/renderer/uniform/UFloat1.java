package gameengine.engine.renderer.uniform;

import org.lwjgl.opengl.GL46;

public class UFloat1 extends AUniformPrimitive<Float> {

	public UFloat1() {
		this("");
	}
	
	public UFloat1(String name) {
		super(name);
	}

	
	@Override
	public void update(Float value) {
		GL46.glUniform1f(this.location, value);
	}
}
