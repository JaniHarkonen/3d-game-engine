package gameengine.engine.renderer;

public interface IRenderStrategy<T> {

	public void render(T renderPass);
}
