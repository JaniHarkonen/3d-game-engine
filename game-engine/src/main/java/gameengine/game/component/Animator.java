package gameengine.game.component;

import gameengine.engine.ITickable;
import gameengine.engine.asset.Animation;
import gameengine.engine.renderer.IRenderStrategy;
import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;

public class Animator implements ITickable, IRenderable {
	private class SceneRenderer implements IRenderStrategy<ScenePass> {

		@Override
		public void render(ScenePass renderPass) {
			renderPass.uBoneMatrices.update(getCurrentFrame().getBoneTransforms());
		}
	}

	private Animation animation;
	private Model animatedModel;
	private int currentFrameIndex;
	private float frameTimer;
	private float frameSpeed;
	private int direction;	// Must be 1 or -1
	private boolean isPaused;
	private SceneRenderer sceneRenderer;
	
	public Animator(Model animatedModel) {
		this.animation = null;
		this.animatedModel = animatedModel;
		this.setFrame(0);
		this.frameTimer = 0.0f;
		this.frameSpeed = 1.0f;
		this.direction = 1;
		this.isPaused = false;
		this.sceneRenderer = new SceneRenderer();
	}
	
	
	@Override
	public void tick(float deltaTime) {
		if( this.isPaused ) {
			return;
		}
		
		this.frameTimer += deltaTime;
		
		if( this.frameTimer >= this.frameSpeed ) {
			this.currentFrameIndex += this.direction;
			
			if( this.currentFrameIndex >= 0 && this.currentFrameIndex < this.animation.getFrameCount() ) {
				this.setFrame(this.currentFrameIndex);
			} else {
				this.currentFrameIndex = Math.max(
					0, Math.min(this.animation.getFrameCount() - 1, this.currentFrameIndex)
				);
				this.onFinish();
			}
			this.frameTimer = 0.0f;
		}
	}
	
	@Override
	public void submitToRenderer(Renderer renderer) {
		renderer.getScenePass().submit(this.sceneRenderer);
		this.animatedModel.submitToRenderer(renderer);
	}
	
	public void onFinish() {
		this.pause();
	}
	
	public Animator restart() {
		this.setFrame(0);
		return this;
	}
	
	public Animator play() {
		this.isPaused = false;
		return this;
	}
	
	public Animator pause() {
		this.isPaused = true;
		return this;
	}
	
	public Animator stop() {
		this.pause();
		this.restart();
		return this;
	}
	
	public Animator reverse() {
		this.direction *= -1;
		return this;
	}
	
	public Animator setFrame(int frameIndex) {
		this.currentFrameIndex = frameIndex;
		return this;
	}
	
	public Animator setAnimation(Animation animation) {
		this.animation = animation;
		this.setFrame(0);
		return this;
	}
	
	public Animator setSpeed(float frameDelta) {
		this.frameSpeed = frameDelta;
		return this;
	}
	
	public float getSpeed() {
		return this.frameSpeed;
	}
	
	public Animation.Frame getCurrentFrame() {
		return this.animation.getFrame(this.currentFrameIndex);
	}
	
	public int getCurrentFrameIndex() {
		return this.currentFrameIndex;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}
}
