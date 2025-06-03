package gameengine.engine.asset;

import org.joml.Matrix4f;

public class Animation {
	
	/************************* Frame-class **************************/
	
	public static class Frame {

		private Matrix4f[] boneTransforms;
		
		public Frame(Matrix4f[] boneTransforms) {
			this.boneTransforms = boneTransforms;
		}
		
		
		public void setBoneTransform(int boneIndex, Matrix4f boneTransform) {
			this.boneTransforms[boneIndex] = boneTransform;
		}
		
		public Matrix4f getBoneTransform(int boneIndex) {
			return this.boneTransforms[boneIndex];
		}
		
		public Matrix4f[] getBoneTransforms() {
			return this.boneTransforms;
		}
	}
	
	
	/************************* Animation-class **************************/
	
	private final String name;
	
	private float duration;
	private Frame[] frames;
	
	public Animation(String name) {
		this.duration = 0.0f;
		this.frames = new Frame[0];
		this.name = name;
	}
	
	public Animation(Animation src) {
		this.name = null;
		this.duration = src.duration;
		this.frames = src.frames; // NO DEEP COPY
	}
	
	
	public void populate(float duration, Frame[] frames) {
		this.duration = duration;
		this.frames = frames;
	}
	
	public float getDuration() {
		return this.duration;
	}
	
	public Frame[] getFrames() {
		return this.frames;
	}
	
	public Frame getFrame(int frameIndex) {
		return this.frames[frameIndex];
	}
	
	public int getFrameCount() {
		return this.frames.length;
	}
}
