package gameengine.engine.asset;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {
	public static abstract class AGroup {
		protected String name;
		protected Map<String, ILoadTask<? extends IAsset>> loaders;
		
		protected AGroup(String name) {
			this.name = name;
			this.loaders = new HashMap<>();
		}
		
		
		/**
		 * To be overridden. Contains the loading calls for all assets of the group.
		 */
		public abstract void load();
		
		public void deload() {
				// Deload all assets by default
			for( ILoadTask<?> task : this.loaders.values() ) {
				task.deload();
			}
		}
		
		protected <T extends IAsset> T loadAsset(ILoadTask<T> task) {
			if( this.loaders.containsKey(task.getName()) ) {
				System.out.println("ERROR: Trying to load an already existing asset!");
				return null;
			}
			
			T asset = task.load();
			this.loaders.put(task.getName(), task);
			return asset;
		}
		
		protected void deloadAsset(String name) {
			ILoadTask<?> task = this.loaders.get(name);
			
			if( task == null ) {
				System.out.println("ERROR: Unable to deload non-existing asset!");
				return;
			}
			
			task.deload();
			this.loaders.remove(name);
		}
		
		public IAsset findAsset(String name) {
			ILoadTask<?> task = this.loaders.get(name);
			
			if( task == null ) {
				System.out.println("ERROR: Can't find asset '" + name + "' in asset group '" + this.name + "'!");
				return null;
			}
			
			return task.getLoadedAsset();
		}
		
		public String getName() {
			return this.name;
		}
	}

	
	private final Map<String, AGroup> groups;
	
	public AssetManager() {
		this.groups = new HashMap<>(); 
	}
	
	
	public void registerGroup(AGroup group) {
		if( this.groups.containsKey(group.getName()) ) {
			System.out.println("ERROR: Trying to create an already existing asset group!");
			return;
		}
		
		this.groups.put(group.getName(), group);
	}
	
	public AGroup findGroup(String name) {
		return this.groups.get(name);
	}
}
