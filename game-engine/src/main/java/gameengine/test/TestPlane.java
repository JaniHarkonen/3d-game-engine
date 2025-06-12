package gameengine.test;

import org.joml.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

import gameengine.debug.CollisionDebugger;
import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.asset.Mesh;
import gameengine.engine.physics.Collider;
import gameengine.engine.physics.IPhysicsObject;
import gameengine.engine.physics.Physics;
import gameengine.engine.physics.PhysicsScene;
import gameengine.engine.physics.Transform;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.component.Submesh;
import gameengine.util.GeometryUtils;

public class TestPlane implements IGameObject, IPhysicsObject {
	private Physics physics;
	private Transform transform;
	private Submesh mesh;
	private CollisionDebugger debugger;
	private CollisionShape shape;
	
	public TestPlane(CollisionShape shape) {
		//CollisionShape ground = new StaticPlaneShape(new Vector3f(0, 1, 0), 0.25f);
		//MotionState groundMotion = new DefaultMotionStawo = new RigidBodyConstructionInfo(0, groundMotion, ground, new Vector3f(0, 0, 0));
		//groundConstructionInfo.restitution = 0.0f;
		//groundBody = new RigidBody(groundConstructionInfo);
		//groundBody.setFriction(0.5f);
		//this.physicsWorld.addCollisionObject(groundBody);
		this.transform = new Transform();
		this.debugger = null;
		this.shape = shape;
	}


	@Override
	public void onCreate() {
		BvhTriangleMeshShape collision = (BvhTriangleMeshShape) this.shape;//(BvhTriangleMeshShape) Mesh.class.cast(Engine.getGame().getAssets().get("mesh-road-test")).getCollisionMesh();
		this.debugger = new CollisionDebugger(collision, this.transform);
		collision.setMargin(.05f);
		
		RigidBodyConstructionInfo bodyInfo = new RigidBodyConstructionInfo(0, this.transform, collision, GeometryUtils.vector3fToJavaxVector3f(new Vector3f(0, 0, 0)));
		bodyInfo.restitution = 0.0f;
		//bodyInfo.angularDamping = 0.95f;
		Collider collider = new Collider(collision);
		this.physics = new Physics(this.transform, bodyInfo, collider);
		CollisionObject body = this.physics.getRigidBody();
		body.setFriction(0.5f);
	}
	
	@Override
	public void tick(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitToRenderer(Renderer renderer) {
		//this.debugger.submitToRenderer(renderer);
		//renderer.getScenePass().submit(this.sceneRenderer);
		
	}
	
	@Override
	public Transform getTransform() {
		return this.transform;
	}

	@Override
	public Physics getPhysics() {
		return this.physics;
	}



	@Override
	public void onPhysicsCreate(PhysicsScene scene) {
		// TODO Auto-generated method stub
		
	}
}
