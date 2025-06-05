package gameengine.engine.asset;

import java.util.List;

import org.joml.Matrix4f;

public class Skeleton {
	public static class Bone {
		private int boneID;
		private String boneName;
		private Matrix4f offsetTransform;
		
		public Bone(int boneID, String boneName, Matrix4f offsetTransform) {
			this.boneID = boneID;
			this.boneName = boneName;
			this.offsetTransform = offsetTransform;
		}
		
		
		public int getID() {
			return this.boneID;
		}
		
		public String getName() {
			return this.boneName;
		}
		
		public Matrix4f getOffsetTransform() {
			return this.offsetTransform;
		}
	}

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
