package gameengine.engine.renderer.shader;

import org.lwjgl.opengl.GL46;

import gameengine.engine.asset.IAsset;
import gameengine.util.FileUtils;

public class Shader implements IAsset {
	
	private final int type;
	private String name;
	private String path;
	private int ID;
	
	public Shader(String name, String path, int type) {
		this.type = type;
		this.name = name;
		this.path = path;
		this.ID = -1;
	}


	@Override
	public void load() {
		String sourceCode = FileUtils.readTextFile(this.path);
        this.ID = GL46.glCreateShader(this.type);
        
        if( this.ID == 0 ) {
            throw new RuntimeException("ERROR: Unable to create shader '" + this.name + "'!");
        }

        GL46.glShaderSource(this.ID, sourceCode);
        GL46.glCompileShader(this.ID);

        if( GL46.glGetShaderi(this.ID, GL46.GL_COMPILE_STATUS) == 0 ) {
            throw new RuntimeException("ERROR: Failed to compile shader: " + GL46.glGetShaderInfoLog(this.ID, 1024));
        }
	}
	
	public void attach(ShaderProgram shaderProgram) {
		GL46.glAttachShader(shaderProgram.getID(), this.ID);
	}
	
	public void detach(ShaderProgram shaderProgram) {
		GL46.glDetachShader(shaderProgram.getID(), this.ID);
	}

	@Override
	public void deload() {
    	GL46.glDeleteShader(this.ID);
	}
	
	@Override
	public String getName() {
		return this.name;
	}
}
