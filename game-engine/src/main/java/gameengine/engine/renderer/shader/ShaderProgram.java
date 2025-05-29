package gameengine.engine.renderer.shader;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL46;

public class ShaderProgram {

    private int ID;
    private List<Shader> shaders;

    public ShaderProgram() {
    	this.ID = -1;
    	this.shaders = new ArrayList<>();
    }
    
    public void generate() {
        this.ID = GL46.glCreateProgram();
        
        if( this.ID == 0 ) {
            throw new RuntimeException("ERROR: Failed to create a shader program!");
        }
        
        for( Shader s : this.shaders ) {
        	s.load();
        	s.attach(this);
        }
        
        GL46.glLinkProgram(this.ID);
        
        if( GL46.glGetProgrami(this.ID, GL46.GL_LINK_STATUS) == 0 ) {
            throw new RuntimeException("ERROR: Unable to link shader program: " + GL46.glGetProgramInfoLog(this.ID, 1024));
        }
        
        for( Shader s : this.shaders ) {
        	s.detach(this);
        	s.deload();
        }
    }

    public void bind() {
        GL46.glUseProgram(this.ID);
    }
    
    public void unbind() {
    	GL46.glUseProgram(0);
    }


    public void dispose() {
        unbind();
    	GL46.glDeleteProgram(this.ID);
    }
    
    public void addShader(Shader shader) {
    	this.shaders.add(shader);
    }
    
    public int getID() {
        return this.ID;
    }
}
