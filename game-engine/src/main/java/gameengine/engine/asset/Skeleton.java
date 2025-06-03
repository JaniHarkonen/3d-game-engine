package gameengine.engine.asset;

import java.util.List;

public class Skeleton {

	private List<Bone> bones;
	
	public Skeleton() {
		this.bones = null;
	}
	
	
	public void populate(List<Bone> bones) {
		this.bones = bones;
	}
	
	public List<Bone> getBones() {
		return this.bones;
	}
}
