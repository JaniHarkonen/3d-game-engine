package gameengine.engine.renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL46;

import gameengine.util.FileUtils;

public class ShaderProgram {

    private final int programId;

    public ShaderProgram(List<ShaderModuleData> shaderModuleDataList) {
        programId = GL46.glCreateProgram();
        
        if (programId == 0) {
            throw new RuntimeException("Could not create Shader");
        }

        List<Integer> shaderModules = new ArrayList<>();
        
        for( ShaderModuleData shader : shaderModuleDataList ) {
        	shaderModules.add(this.createShader(FileUtils.readTextFile(shader.getShaderFile()), shader.getShaderType()));
        }

        link(shaderModules);
    }

    public void bind() {
        GL46.glUseProgram(programId);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
        	GL46.glDeleteProgram(programId);
        }
    }

    protected int createShader(String shaderCode, int shaderType) {
        int shaderId = GL46.glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new RuntimeException("Error creating shader. Type: " + shaderType);
        }

        GL46.glShaderSource(shaderId, shaderCode);
        GL46.glCompileShader(shaderId);

        if (GL46.glGetShaderi(shaderId, GL46.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling Shader code: " + GL46.glGetShaderInfoLog(shaderId, 1024));
        }

        GL46.glAttachShader(programId, shaderId);

        return shaderId;
    }

    public int getProgramId() {
        return programId;
    }

    private void link(List<Integer> shaderModules) {
    	GL46.glLinkProgram(programId);
        if( GL46.glGetProgrami(programId, GL46.GL_LINK_STATUS) == 0 ) {
            throw new RuntimeException("Error linking Shader code: " + GL46.glGetProgramInfoLog(programId, 1024));
        }

        for( Integer shader : shaderModules ) {
        	GL46.glDetachShader(programId, shader);
        	GL46.glDeleteShader(shader);
        }
    }

    public void unbind() {
    	GL46.glUseProgram(0);
    }

    public void validate() {
    	GL46.glValidateProgram(programId);
        if (GL46.glGetProgrami(programId, GL46.GL_VALIDATE_STATUS) == 0) {
            throw new RuntimeException("Error validating Shader code: " + GL46.glGetProgramInfoLog(programId, 1024));
        }
    }
}
