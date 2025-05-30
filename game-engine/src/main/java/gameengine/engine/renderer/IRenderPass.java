package gameengine.engine.renderer;

import gameengine.engine.renderer.uniform.UniformManager;

public interface IRenderPass<T> {
	public void setup();
	
	public void reset();
	
	public void submit(T object);
	
	public void execute();
	
	public void dispose();
	
	public UniformManager getUniformManager();
}
