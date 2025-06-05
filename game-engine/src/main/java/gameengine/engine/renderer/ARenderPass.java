package gameengine.engine.renderer;

import java.util.ArrayList;
import java.util.List;

import gameengine.engine.renderer.shader.ShaderProgram;
import gameengine.logger.Logger;

abstract class ARenderPass<T> {
	protected List<IRenderStrategy<T>> preRender;
	protected List<IRenderStrategy<T>> render;
	protected ShaderProgram shaderProgram;
	
	protected ARenderPass() {
		this.preRender = new ArrayList<>();
		this.render = new ArrayList<>();
		this.shaderProgram = null;
	}
	
	
	abstract void setup();
	
	protected void reset() {
		this.preRender.clear();
		this.render.clear();
	}
	
	public void preRender(IRenderStrategy<T> renderer) {
		if( renderer == null ) {
			Logger.spam(this, "WARNING: Trying to submit a null render strategy for pre-rendering!");
			return;
		}
		
		this.preRender.add(renderer);
	}
	
	public void submit(IRenderStrategy<T> renderer) {
		if( renderer == null ) {
			Logger.spam(this, "WARNING: Trying to submit a null render strategy!");
			return;
		}
		
		this.render.add(renderer);
	}
	
	abstract void execute();
	
	abstract void dispose();
}
