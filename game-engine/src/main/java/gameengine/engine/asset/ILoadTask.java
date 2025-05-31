package gameengine.engine.asset;

public interface ILoadTask<T extends IAsset> {

	public T load();
	
	public void deload();
	
	public String getName();
	
	public String getPath();
	
	public T getLoadedAsset();
}
