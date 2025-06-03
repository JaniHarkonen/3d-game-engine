package gameengine.engine.asset;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AssetManager {
	public static class Group {
		private final String name;
		private Map<String, IAsset> assets;
		
		public Group(String name) {
			this.name = name;
			this.assets = new LinkedHashMap<>();
		}
		
		
		public Group put(IAsset asset) {
			this.assets.put(asset.getName(), asset);
			return this;
		}
		
		public IAsset get(String name) {
			return this.assets.get(name);
		}
		
		public void load() {
			for( IAsset a : this.assets.values() ) {
				a.load();
			}
		}
		
		public void deload() {
			for( IAsset a : this.assets.values() ) {
				a.deload();
			}
		}
		
		public String getName() {
			return this.name;
		}
	}

	
	private final Map<String, Group> groups;
	
	public AssetManager() {
		this.groups = new HashMap<>(); 
	}
	
	
	public void registerGroup(Group group) {
		if( this.groups.containsKey(group.getName()) ) {
			System.out.println("ERROR: Trying to create an already existing asset group!");
			return;
		}
		
		this.groups.put(group.getName(), group);
	}
	
	public Group findGroup(String name) {
		return this.groups.get(name);
	}
}
