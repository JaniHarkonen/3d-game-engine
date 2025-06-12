package gameengine.game;

import org.joml.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.vehicle.DefaultVehicleRaycaster;
import com.bulletphysics.dynamics.vehicle.RaycastVehicle;
import com.bulletphysics.dynamics.vehicle.VehicleTuning;

import gameengine.engine.IGameObject;
import gameengine.engine.physics.Collider;
import gameengine.engine.physics.IPhysicsObject;
import gameengine.engine.physics.Physics;
import gameengine.engine.physics.PhysicsScene;
import gameengine.engine.physics.Transform;
import gameengine.engine.renderer.Renderer;
import gameengine.game.component.Model;
import gameengine.util.GeometryUtils;

public class Car implements IGameObject, IPhysicsObject {

	private Model model;
	private Physics physics;
	private Transform transform;
	private Collider collider;
	private Vector3f dimensions;
	private float weight;
	private Vector3f inertia;
	private RaycastVehicle vehicle;
	private int DEBUGsteer;
	private int DEBUGacceleration;
	private int DEBUGbrake;
	
	public Car(float x, float y, float z, float w, float h, float l, float weight, Vector3f inertia) {
		this.model = null;
		this.physics = null;
		this.transform = new Transform(x, y, z);
		this.collider = null;
		this.dimensions = new Vector3f(w, h, l);
		this.weight = weight;
		this.inertia = inertia;
		this.vehicle = null;
		this.DEBUGsteer = 0;
		this.DEBUGacceleration = 0;
		this.DEBUGbrake = 0;
	}
	
	
	@Override
	public void onCreate() {
		
	}
	
	@Override
	public void onPhysicsCreate(PhysicsScene scene) {
		BoxShape shape = new BoxShape(GeometryUtils.vector3fToJavaxVector3f(this.dimensions.div(2)));
		this.collider = new Collider(shape);
		
		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(this.weight, this.transform, shape);
		shape.calculateLocalInertia(this.weight, GeometryUtils.vector3fToJavaxVector3f(this.inertia, info.localInertia));
		info.restitution = 0.0f;
		info.angularDamping = 0.9f;
		
		this.physics = new Physics(this.transform, info, this.collider);
		
		RigidBody body = this.physics.getRigidBody();
		body.setAngularFactor(0.5f);
		body.setFriction(0.2f);
		body.setActivationState(RigidBody.DISABLE_DEACTIVATION);
		
		DefaultVehicleRaycaster raycaster = new DefaultVehicleRaycaster(scene.getPhysicsWorld());
		VehicleTuning tuning = new VehicleTuning();
		//tuning.suspensionStiffness = 20;
		//tuning.suspensionCompression = 15f * tuning.suspensionStiffness;
		//tuning.suspensionDamping = 25f;
		//tuning.maxSuspensionTravelCm = 500f;
		//tuning.frictionSlip = 10;
		this.vehicle = new RaycastVehicle(tuning, body, raycaster);
		
		javax.vecmath.Vector3f wheelAxle = GeometryUtils.vector3fToJavaxVector3f(new Vector3f(1f, 0, 0));
		//javax.vecmath.Vector3f wheelAxle = GeometryUtils.vector3fToJavaxVector3f(new Vector3f(0f, 0, 0));
		javax.vecmath.Vector3f wheelDirection = GeometryUtils.vector3fToJavaxVector3f(new Vector3f(0, -1, 0));
		float suspensionRestLength = 0.3f;
		float wheelRadius = 0.25f;
		
		this.vehicle.addWheel(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(-this.dimensions.x/2, -this.dimensions.y/2, -this.dimensions.z/2)), wheelDirection, wheelAxle, suspensionRestLength, wheelRadius, tuning, true);
		this.vehicle.addWheel(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(this.dimensions.x/2, -this.dimensions.y/2, -this.dimensions.z/2)), wheelDirection, wheelAxle, suspensionRestLength, wheelRadius, tuning, true);
		this.vehicle.addWheel(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(-this.dimensions.x/2, -this.dimensions.y/2, this.dimensions.z/2)), wheelDirection, wheelAxle, suspensionRestLength, wheelRadius, tuning, false);
		this.vehicle.addWheel(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(this.dimensions.x/2, -this.dimensions.y/2, this.dimensions.z/2)), wheelDirection, wheelAxle, suspensionRestLength, wheelRadius, tuning, false);
		
		//this.vehicle.getWheelInfo(0).frictionSlip = 20;
		/*this.vehicle.getWheelInfo(0).rollInfluence = 0.05f;
		this.vehicle.getWheelInfo(1).rollInfluence = 0.05f;
		this.vehicle.getWheelInfo(2).rollInfluence = 0.05f;
		this.vehicle.getWheelInfo(3).rollInfluence = 0.05f;*/
		
		scene.getPhysicsWorld().addVehicle(this.vehicle);
		
		this.model.getTransform().bind(this.transform);
	}
	
	@Override
	public void tick(float deltaTime) {
		this.vehicle.setSteeringValue(this.DEBUGsteer * 0.4f, 0);
		this.vehicle.setSteeringValue(this.DEBUGsteer * 0.4f, 1);
		this.vehicle.applyEngineForce(this.DEBUGacceleration * 3000, 0);
		this.vehicle.applyEngineForce(this.DEBUGacceleration * 3000, 1);
		this.vehicle.setBrake(this.DEBUGbrake * 100f, 0);
		this.vehicle.setBrake(this.DEBUGbrake * 100f, 1);
		this.vehicle.setBrake(this.DEBUGbrake * 100f, 2);
		this.vehicle.setBrake(this.DEBUGbrake * 100f, 3);
	}
	
	public void accelerate(boolean on) {
		if( on ) {
			this.DEBUGacceleration++;
		} else {
			this.DEBUGacceleration--;
		}
	}
	
	public void reverse(boolean on) {
		if( on ) {
			this.DEBUGacceleration--;
		} else {
			this.DEBUGacceleration++;
		}
	}
	
	public void steerLeft(boolean on) {
		if( on ) {
			this.DEBUGsteer++;
		} else {
			this.DEBUGsteer--;
		}
	}
	
	public void steerRight(boolean on) {
		if( on ) {
			this.DEBUGsteer--;
		} else {
			this.DEBUGsteer++;
		}
	}
	
	public void brake(boolean on) {
		if( on ) {
			this.DEBUGbrake++;
		} else {
			this.DEBUGbrake--;
		}
	}
	
	public void releaseBrake() {
		this.vehicle.setBrake(0, 0);
		this.vehicle.setBrake(0, 1);
		this.vehicle.setBrake(0, 2);
		this.vehicle.setBrake(0, 3);
	}

	
	@Override
	public void submitToRenderer(Renderer renderer) {
		this.model.submitToRenderer(renderer);
	}
	
	public void setModel(Model model) {
		this.model = model;
	}

	@Override
	public Transform getTransform() {
		return this.transform;
	}

	@Override
	public Physics getPhysics() {
		return this.physics;
	}
}
