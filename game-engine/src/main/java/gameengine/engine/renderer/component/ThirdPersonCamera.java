package gameengine.engine.renderer.component;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import gameengine.engine.ITickable;

public class ThirdPersonCamera extends Camera implements ITickable {
	public static final float DEFAULT_DISTANCE = 6f;
	
	private gameengine.engine.physics.Transform subject;
	private float distance;

	public ThirdPersonCamera(Projection projection, Transform subject) {
		super(projection);
		this.subject = subject;
		this.distance = DEFAULT_DISTANCE;
		this.transform = new Transform();
		this.transform.setRotator(new Camera.Rotator(this.transform) {
			
			@Override
			public void rotate(float x, float y) {
				super.rotate(x, y);
				

			}
		});
	}
	
	public ThirdPersonCamera(Projection projection) {
		this(projection, null);
	}
	
	
	@Override
	public void tick(float deltaTime) {
		if( ThirdPersonCamera.this.subject != null ) {
			Vector3f subjectPosition = ThirdPersonCamera.this.subject.getPosition(new Vector3f());
			Quaternionf rotation = ThirdPersonCamera.this.transform.getRotator().getAsQuaternion(new Quaternionf());
			Vector3f offset = rotation.positiveZ(new Vector3f()).mul(ThirdPersonCamera.this.distance);
			ThirdPersonCamera.this.transform.setPosition(
				subjectPosition.x + offset.x, 
				subjectPosition.y + offset.y, 
				subjectPosition.z + offset.z
			);
		}
	}
	
	public void bind(gameengine.engine.physics.Transform subject) {
		this.subject = subject;
	}
	
	public void setDistance(float distance) {
		this.distance = distance;
	}
}
