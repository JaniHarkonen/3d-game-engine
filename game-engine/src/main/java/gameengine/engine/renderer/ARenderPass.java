package gameengine.engine.renderer;

import java.util.LinkedList;
import java.util.Queue;

import gameengine.engine.renderer.shader.ShaderProgram;
import gameengine.logger.Logger;

abstract class ARenderPass<T> {
	protected Queue<IRenderStrategy<T>> preRender;
	protected Queue<IRenderStrategy<T>> render;
	protected ShaderProgram shaderProgram;
	
	protected ARenderPass() {
		this.preRender = new LinkedList<>();
		this.render = new LinkedList<>();
		this.shaderProgram = null;
	}
	
	
	abstract void setup();
	
	/*void reset() {
		this.preRender.clear();
		this.render.clear();
	}*/
	
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
