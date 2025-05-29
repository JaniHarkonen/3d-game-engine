package gameengine.engine.renderer.shader;

public class ShaderModuleData {
	private String shaderFile;
	private int shaderType;
	
	public ShaderModuleData(String shaderFile, int shaderType) {
		this.shaderFile = shaderFile;
		this.shaderType = shaderType;
	}
	
	
	public String getShaderFile() {
		return this.shaderFile;
	}
	
	public int getShaderType() {
		return this.shaderType;
	}
}
