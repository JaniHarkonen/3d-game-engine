package gameengine.engine.renderer;

import java.util.ArrayList;
import java.util.List;

import gameengine.engine.renderer.shader.ShaderProgram;

abstract class ARenderPass<T> {
	protected List<T> submissions;
	protected ShaderProgram shaderProgram;
	//protected UniformManager uniformsMap;
	
	protected ARenderPass() {
		this.submissions = new ArrayList<>();
		this.shaderProgram = null;
		//this.uniformsMap = null;
	}
	
	abstract void setup();
	
	void reset() {
		this.submissions.clear();
	}
	
	public void submit(T object) {
		this.submissions.add(object);
	}
	
	abstract void execute();
	
	abstract void dispose();
	
	/*public UniformManager getUniformManager() {
		return this.uniformsMap;
	}*/
}
