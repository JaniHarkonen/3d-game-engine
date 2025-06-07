package gameengine.test;

import java.nio.ByteBuffer;

import org.joml.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.asset.Mesh;
import gameengine.engine.physics.Collider;
import gameengine.engine.physics.IPhysicsObject;
import gameengine.engine.physics.Physics;
import gameengine.engine.physics.Transform;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.component.Submesh;
import gameengine.util.GeometryUtils;

public class TestPlane implements IGameObject, IPhysicsObject {
	
	private Physics physics;
	private Transform transform;
	
	public TestPlane() {
		//CollisionShape ground = new StaticPlaneShape(new Vector3f(0, 1, 0), 0.25f);
		//MotionState groundMotion = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1f)));
		//RigidBodyConstructionInfo groundConstructionInfo = new RigidBodyConstructionInfo(0, groundMotion, ground, new Vector3f(0, 0, 0));
		//groundConstructionInfo.restitution = 0.0f;
		//groundBody = new RigidBody(groundConstructionInfo);
		//groundBody.setFriction(0.5f);
		//this.physicsWorld.addCollisionObject(groundBody);
		this.transform = new Transform();
	}


	@Override
	public void onCreate() {
		//CollisionShape collision = new StaticPlaneShape(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(0.5f, 1, 0)), 0.25f);
		/*Submesh mesh = Mesh.class.cast(Engine.getGame().getAssets().get("mesh-outside")).getSubmesh(0);
		int[] indices = mesh.getIndices();
		ByteBuffer bufferIndices = (ByteBuffer) ByteBuffer.allocate(indices.length * Integer.BYTES);
		bufferIndices.asIntBuffer().put(indices).flip();
		ByteBuffer bufferVertices = (ByteBuffer) ByteBuffer.allocate(mesh.getVertexCount() * 3 * Float.BYTES);
		bufferVertices.asFloatBuffer().put(mesh.getVerticesAsFloatArray()).flip();
		//int numTriangles, ByteBuffer triangleIndexBase, int triangleIndexStride, int numVertices, ByteBuffer vertexBase, int vertexStride
		TriangleIndexVertexArray vert = new TriangleIndexVertexArray(
			mesh.getFaces().length, 
			bufferIndices, 
			3 * Integer.BYTES, 
			mesh.getVertexCount(), 
			bufferVertices, 
			3 * Float.BYTES
		);*/
		
		//CollisionShape collision = new BvhTriangleMeshShape(vert, true);
		CollisionShape collision = Mesh.class.cast(Engine.getGame().getAssets().get("mesh-road-test")).getCollisionMesh();
		//Vector3f inertia = new Vector3f(0, 0, 0);
		//Logger.debug(this, this.transform.getPosition().x, this.transform.getPosition().y, this.transform.getPosition().z);
		RigidBodyConstructionInfo bodyInfo = new RigidBodyConstructionInfo(0, this.transform, collision, GeometryUtils.vector3fToJavaxVector3f(new Vector3f(0, 0, 0)));
		//collision.calculateLocalInertia(2.5f, GeometryUtils.vector3fToJavaxVector3f(inertia, bodyInfo.localInertia));
		bodyInfo.restitution = 0.0f;
		//bodyInfo.angularDamping = 0.95f;
		Collider collider = new Collider(collision);
		this.physics = new Physics(this.transform, bodyInfo, collider);
		CollisionObject body = this.physics.getRigidBody();
		body.setFriction(0.5f);
		/*Collider collider = new Collider(new StaticPlaneShape(GeometryUtils.vector3fToJavaxVector3f(new Vector3f(0, 1, 0)), 0.25f));
		this.physics = new CollisionOnlyPhysics(this.transform, collider);
		this.physics.getBody().setRestitution(0);*/
		//CollisionShape ground = new StaticPlaneShape(new Vector3f(0, 1, 0), 0.25f);
		//MotionState groundMotion = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1f)));
		//RigidBodyConstructionInfo groundConstructionInfo = new RigidBodyConstructionInfo(0, groundMotion, ground, new Vector3f(0, 0, 0));
		//groundConstructionInfo.restitution = 0.0f;
		//groundBody = new RigidBody(groundConstructionInfo);
		//groundBody.setFriction(0.5f);
		//this.physicsWorld.addCollisionObject(groundBody);
	}
	
	@Override
	public void tick(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitToRenderer(Renderer renderer) {
		// TODO Auto-generated method stub
		
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
