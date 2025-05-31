package gameengine.engine.renderer.shader;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL46;

import gameengine.engine.renderer.uniform.IUniform;
import gameengine.logger.Logger;

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
        	Logger.fatal(this, "Failed to create shader program!");
        }
        
        for( Shader s : this.shaders ) {
        	Logger.info(this, "Loading shader '" + s.getName() + "' from: ", s.getPath());
        	s.load();
        	s.attach(this);
        	Logger.spam(this, "Attached shader '" + s.getName() + "'.");
        }
        
        GL46.glLinkProgram(this.ID);
        Logger.info(this, "Linked shader program.");
        
        if( GL46.glGetProgrami(this.ID, GL46.GL_LINK_STATUS) == 0 ) {
        	Logger.fatal(this, "Unable to link shader program!", "OpenGL log:", GL46.glGetProgramInfoLog(this.ID, 1024));
        }
        
        for( Shader s : this.shaders ) {
        	Logger.spam(this, "Detaching shader '" + s.getName() + "'.");
        	s.detach(this);
        	s.deload();
        	Logger.info(this, "Deloaded shader '" +s.getName() + "'.");
        }
    }
    
    public void declareUniform(IUniform<?>... uniforms) {
    	for( IUniform<?> u : uniforms ) {
    		u.initialize(this);
    		Logger.info(this, "Declared uniform '" + u.getName() + "'.");
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
