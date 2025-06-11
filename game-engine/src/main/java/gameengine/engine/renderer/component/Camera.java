package gameengine.engine.renderer.component;

import org.joml.Quaternionf;

import gameengine.engine.renderer.CascadeShadowPass;
import gameengine.engine.renderer.IRenderStrategy;
import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.util.GeometryUtils;

public class Camera implements IRenderable {
	/**
	 * This is a special transform class derived from the generic Transform.
	 * This transform should only be used by the camera, as its transform
	 * matrix is calculated slightly different to account for the fact, that
	 * the camera perspective exists inverse to the viewed scene.
	 */
	public static class Transform extends gameengine.engine.physics.Transform {
		
		public Transform() {
			super();
		}
		
		
		@Override
		protected void recalculate() {
			this.transformMatrix.identity()
			.rotate(this.rotator.getAsQuaternion(new Quaternionf()))
			.translate(-this.position.x - this.origin.x, -this.position.y - this.origin.y, -this.position.z - this.origin.z);
		}
		
		/*
		@Override
		public void possess(IHasTransform possessor) {
			gameengine.engine.physics.Transform transform = possessor.getTransform();
			this.position = transform.getPosition();
			this.scale = transform.getScale();
			
			if( transform instanceof Transform ) {
				this.rotator = transform.getRotator();
			}
		}*/
	}
	
	/**
	 * This is a special rotator class derived from the generic Rotator.
	 * This rotator should only be used by the camera, as the camera
	 * rotations have to be applied slightly different. In the generic
	 * case, the rotation quaternion is derived calculating:
	 * <code>qYaw * qCurrent * qPitch</code>
	 * where:
	 * <b>qYaw</b> is a quaternion representing the object's yaw
	 * <b>qCurrent</b> is the quaternion representing the object's 
	 * current rotation
	 * <b>qPitch</b> is a quaternion representing the object's pitch
	 * 
	 * Camera objects, however, must be rotated slightly different.
	 * Camera rotation is derived by calculating:
	 * <code>qPitch * qCurrent * qYaw</code>
	 */
	public static class Rotator extends gameengine.engine.physics.Rotator {

		public Rotator(Transform transform) {
			super(transform);
		}
		
		
		@Override
		public void rotate(float x, float y) {
			Quaternionf rotationX = new Quaternionf().rotateAxis(GeometryUtils.toRadians(x), 1, 0, 0);
			Quaternionf rotationY = new Quaternionf().rotateAxis(GeometryUtils.toRadians(y), 0, 1, 0);
			this.setQuaternion(rotationX.mul(this.rotation).mul(rotationY));
		}
	}
	
	
	private class SceneRenderer implements IRenderStrategy<ScenePass> {

		@Override
		public void render(ScenePass renderPass) {
			renderPass.uProjection.update(getProjection().getAsMatrix());
			renderPass.uCamera.update(getTransform().getAsMatrix());
		}
	}
	
	private class CascadeShadowRenderer implements IRenderStrategy<CascadeShadowPass> {

		@Override
		public void render(CascadeShadowPass renderPass) {
			renderPass.camera = Camera.this;
		}
	}
	
	protected Transform transform;
	protected Projection projection;
	protected SceneRenderer sceneRenderer;
	protected CascadeShadowRenderer cascadeShadowRenderer;

    public Camera(Projection projection) {
    		// Special transform whose matrix is translated and rotated slightly different
        this.transform = new Transform();
        this.projection = projection;
        this.sceneRenderer = new SceneRenderer();
        this.cascadeShadowRenderer = new CascadeShadowRenderer();
    }

    
    @Override
	public void submitToRenderer(Renderer renderer) {
    	renderer.getCascadeShadowPass().preRender(this.cascadeShadowRenderer);
    	renderer.getScenePass().preRender(this.sceneRenderer);
	}
    
    public Transform getTransform() {
    	return this.transform;
    }
    
    public Projection getProjection() {
    	return this.projection;
    }
}
