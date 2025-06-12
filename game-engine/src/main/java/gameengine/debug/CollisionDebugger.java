package gameengine.debug;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.ByteBufferVertexData;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.collision.shapes.SphereShape;

import gameengine.engine.Engine;
import gameengine.engine.asset.Defaults;
import gameengine.engine.asset.Texture;
import gameengine.engine.physics.Transform;
import gameengine.engine.renderer.IRenderStrategy;
import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.renderer.component.Material;
import gameengine.engine.renderer.component.Submesh;
import gameengine.logger.Logger;
import gameengine.util.GeometryShapeUtils;
import gameengine.util.GeometryUtils;

public class CollisionDebugger implements IRenderable {
	
	public static final int MESH_RESOLUTION = 10;
	
	private class SceneRenderer implements IRenderStrategy<ScenePass> {

		@Override
		public void render(ScenePass renderPass) {
			material.bind(Material.DIFFUSE);
			renderPass.uObject.update(collisionTransform.getAsMatrix());
			debugSubmesh.render();
		}
	}
	
	private CollisionShape shape;
	private Submesh debugSubmesh;
	private Transform collisionTransform;
	private Material material;
	private SceneRenderer sceneRenderer;
	
	public CollisionDebugger(CollisionShape shape, Transform collisionTransform) {
		this.shape = shape;
		this.debugSubmesh = Defaults.SUBMESH;
		this.collisionTransform = collisionTransform;
		this.material = new Material();
		this.material.setTexture(Material.DIFFUSE, (Texture) Engine.getGame().getAssets().get("tex-car-test"));
		this.sceneRenderer = new SceneRenderer();
	}


	@Override
	public void submitToRenderer(Renderer renderer) {
		
			// Generate the mesh before submitting to renderer
		if( this.debugSubmesh.isNull() ) {
			this.generateDebugMesh();
		}
		
		renderer.getScenePass().submit(this.sceneRenderer);
	}
	
	public void generateDebugMesh() {
		this.debugSubmesh = new Submesh();
		
		if( this.shape instanceof CylinderShape ) {
				// Cylinder
			CylinderShape cylinder = (CylinderShape) this.shape;
			Vector3f halfExtents = GeometryUtils.javaxVector3fToVector3f(
				cylinder.getHalfExtentsWithoutMargin(new javax.vecmath.Vector3f())
			);
			GeometryShapeUtils.populateCylinder(this.debugSubmesh, MESH_RESOLUTION, halfExtents.x, halfExtents.y * 2);
		} else if( this.shape instanceof BoxShape ) {
			
				// Box
			BoxShape box = (BoxShape) this.shape;
			Vector3f halfExtents = GeometryUtils.javaxVector3fToVector3f(
				box.getHalfExtentsWithoutMargin(new javax.vecmath.Vector3f())
			);
			GeometryShapeUtils.populateBox(this.debugSubmesh, halfExtents.x, halfExtents.y, halfExtents.z);
		} else if( this.shape instanceof SphereShape ) {
				// Sphere
			GeometryShapeUtils.populateSphere(this.debugSubmesh, MESH_RESOLUTION, ((SphereShape) this.shape).getRadius());
		} else if( this.shape instanceof CapsuleShape ) {
				// Capsule
			CapsuleShape capsule = (CapsuleShape) this.shape;
			GeometryShapeUtils.populateCapsule(this.debugSubmesh, MESH_RESOLUTION, capsule.getRadius(), capsule.getHalfHeight() * 2);
		} else if( this.shape instanceof BvhTriangleMeshShape ) {
				// Custom triangle mesh
			BvhTriangleMeshShape collision = (BvhTriangleMeshShape) this.shape;
			ByteBufferVertexData vd = (ByteBufferVertexData) collision.getMeshInterface().getLockedVertexIndexBase(0);
			FloatBuffer fb = vd.vertexData.asFloatBuffer();
			IntBuffer ib = vd.indexData.asIntBuffer();
			float[] positions = new float[fb.remaining()];
			int[] indices = new int[ib.remaining()];
			
			int index = 0;
			while( fb.remaining() > 0 ) {
				positions[index++] = fb.get();
			}
			
			index = 0;
			while( ib.remaining() > 0 ) {
				indices[index++] = ib.get();
			}
			
			this.debugSubmesh = new Submesh();
			this.debugSubmesh.populate(positions, indices);
		} else {
			Logger.spam(this, "Failed to generate collision debugger mesh! Collision shape not supported.");
		}
	}
}
