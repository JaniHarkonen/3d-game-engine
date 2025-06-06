package gameengine.engine.renderer.uniform;

import org.lwjgl.opengl.GL46;

import gameengine.engine.renderer.shader.ShaderProgram;
import gameengine.logger.Logger;

public abstract class AUniformPrimitive<T> implements IUniform<T> {
	protected int location;
	protected String name;
	
	public AUniformPrimitive(String name) {
		this.location = -1;
		this.name = name;
	}
	
	
	@Override
	public void initialize(ShaderProgram shaderProgram) {
		if( this.name.charAt(0) != 'u' ) {
			Logger.fatal(
				this, 
				"FATAL ERROR: Failed to create uniform '" + this.name + "'!\n" +
				"Uniform name violates naming convention by not starting with 'u'."
			);
		}
		this.location = GL46.glGetUniformLocation(shaderProgram.getID(), this.name);
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
}
