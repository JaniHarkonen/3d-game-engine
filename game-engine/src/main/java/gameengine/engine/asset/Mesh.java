package gameengine.engine.asset;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVertexWeight;
import org.lwjgl.assimp.Assimp;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;

import gameengine.engine.renderer.component.Submesh;
import gameengine.logger.Logger;
import gameengine.util.ArrayUtils;
import gameengine.util.GeometryUtils;

public class Mesh implements IAsset {
	private class VertexWeight {

		private int boneID;
		private int vertexID;
		private float weight;
		
		public VertexWeight(int boneID, int vertexID, float weight) {
			this.boneID = boneID;
			this.vertexID = vertexID;
			this.weight = weight;
		}
		
		
		public int getBoneID() {
			return this.boneID;
		}
		
		public int getVertexID() {
			return this.vertexID;
		}
		
		public float getWeight() {
			return this.weight;
		}
	}
	
	public static final int DEFAULT_IMPORT_FLAGS = (
		Assimp.aiProcess_GenSmoothNormals |
		Assimp.aiProcess_Triangulate | 
		Assimp.aiProcess_FixInfacingNormals | 
		Assimp.aiProcess_CalcTangentSpace | 
		Assimp.aiProcess_LimitBoneWeights |
		Assimp.aiProcess_PreTransformVertices
	);
	
	public static final int MAX_WEIGHT_COUNT = 4;
	public static final int MAX_BONE_COUNT = 150;
	
	private final String name;
	private String path;
    private Submesh[] submeshes;
    private Skeleton skeleton;	// If a skeleton is found, it will be placed in this field, otherwise null
    private CollisionShape collisionMesh;
    private boolean generateCollision;
    private int importFlags;

    public Mesh(String name, String path, boolean generateCollision, int importFlags) {
    	this.name = name;
    	this.path = path;
    	this.submeshes = new Submesh[0];
    	this.skeleton = new Skeleton();
    	this.collisionMesh = null;
    	this.generateCollision = generateCollision;
        this.importFlags = importFlags;
    }
    
    public Mesh(String name, String path, boolean generateCollision) {
    	this(name, path, generateCollision, DEFAULT_IMPORT_FLAGS);
    }
    
    public Mesh(String name, String path) {
    	this(name, path, false);
    }
    
	
	@Override
	public void load() {
		Logger.info(this, "Loading mesh '" + this.name + "' from: ", this.path);
		
        AIScene aiScene = Assimp.aiImportFile(this.path, this.importFlags);
        
        if( aiScene == null ) {
        	Logger.error(
    			this, 
    			"Failed to load Assimp scene for mesh '" + this.name + "' from: ", 
    			this.path, 
    			"Assimp log:", 
    			Assimp.aiGetErrorString()
			);
        	return;
        }
		
		int submeshCount = aiScene.mNumMeshes();
		PointerBuffer aiMeshBuffer = aiScene.mMeshes();
		IndexedMesh[] collisionSubmeshes = new IndexedMesh[submeshCount];
		List<Skeleton.Bone> boneList = new ArrayList<>();
		this.submeshes = new Submesh[submeshCount];
		
		Logger.info(this, "Found " + submeshCount + " submeshes.");
		
		for( int i = 0; i < submeshCount; i++ ) {
			AIMesh aiMesh = AIMesh.create(aiMeshBuffer.get(i));
			
			Logger.spam(this, "Found submesh '" + aiMesh.mName().dataString() + "' (" + i + 1 + " / " + submeshCount + ").");
			
			Vector3f[] normals = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mNormals());
			Vector2f[] UVs = GeometryUtils.aiVector3DBufferToVector2fArray(aiMesh.mTextureCoords(0));
			
				// Fix UV-coordinates
			for( Vector2f uv : UVs ) {
				uv.y = 1 - uv.y;
			}
			
				// Extract indices
			int faceCount = aiMesh.mNumFaces();
			Submesh.Face[] faces = new Submesh.Face[faceCount];
			AIFace.Buffer aiFaceBuffer = aiMesh.mFaces();
			
				// Indices for possible collision generation (may not be used)
			int[] collisionIndices = new int[faceCount * Submesh.Face.INDICES_PER_FACE];
			int index = 0;
			
			for( int j = 0; j < faceCount; j++ ) {
				AIFace aiFace = aiFaceBuffer.get(j);
				IntBuffer indexBuffer = aiFace.mIndices();
				int[] faceIndices = new int[Submesh.Face.INDICES_PER_FACE];
				int k = 0;
				
				while( indexBuffer.remaining() > 0 ) {
					int gotIndex = indexBuffer.get();
					faceIndices[k++] = gotIndex;
					collisionIndices[index++] = gotIndex;	// For collision
				}
				
				faces[j] = new Submesh.Face(faceIndices);
			}
			
				// Extract vertices and the bones that affect them
			int vertexCount = aiMesh.mNumVertices();
			Map<Integer, List<VertexWeight>> weightSet = this.generateWeightSet(boneList, aiMesh.mBones());
			int[] boneIDs = new int[vertexCount * MAX_WEIGHT_COUNT];
			float[] weights = new float[vertexCount * MAX_WEIGHT_COUNT];
			AIVector3D.Buffer buffer = aiMesh.mVertices();
			Vector3f[] vertices = new Vector3f[buffer.remaining()];
			
				// Extract vertex positions for possible collision generation (may not be used)
			float[] collisionPositions = new float[vertexCount * 3];
			index = 0;
			
			for( int j = 0; buffer.remaining() > 0; j++ ) {
				AIVector3D aiVector = buffer.get();
				vertices[j] = new Vector3f(aiVector.x(), aiVector.y(), aiVector.z());
				
				collisionPositions[index++] = aiVector.x();
				collisionPositions[index++] = aiVector.y();
				collisionPositions[index++] = aiVector.z();
				
				this.extractBoneIndicesAndWeights(weights, boneIDs, weightSet, j);
			}
			
			Submesh submesh = new Submesh();
			submesh.populate(vertices, normals, UVs, faces, boneIDs, weights);
			this.submeshes[i] = submesh;
			
				// Generate collision submesh aka indexed mesh
			if( this.generateCollision ) {
				ByteBuffer bufferIndices = (ByteBuffer) ByteBuffer.allocate(collisionIndices.length * Integer.BYTES);
				bufferIndices.asIntBuffer().put(collisionIndices).flip();
				ByteBuffer bufferVertices = (ByteBuffer) ByteBuffer.allocate(collisionPositions.length * Float.BYTES);
				bufferVertices.asFloatBuffer().put(collisionPositions).flip();
				
				IndexedMesh collisionSubmesh = new IndexedMesh();
				collisionSubmesh.numTriangles = faces.length;
				collisionSubmesh.triangleIndexBase = bufferIndices; 
				collisionSubmesh.triangleIndexStride = 3 * Integer.BYTES;
				collisionSubmesh.numVertices = vertices.length; 
				collisionSubmesh.vertexBase = bufferVertices; 
				collisionSubmesh.vertexStride = 3 * Float.BYTES;
				
				collisionSubmeshes[i] = collisionSubmesh;
			}
			
			Logger.spam(this, "Vertex count: " + vertices.length, "UV count: " + UVs.length, "Face count: " + faces.length);
		}
		
		this.skeleton.populate(boneList);
		
			// Generate collision mesh
		if( this.generateCollision ) {
			TriangleIndexVertexArray collisionGeometry = new TriangleIndexVertexArray();
			
			for( IndexedMesh collisionSubmesh : collisionSubmeshes ) {
				collisionGeometry.addIndexedMesh(collisionSubmesh);
			}
			
			this.collisionMesh = new BvhTriangleMeshShape(collisionGeometry, true);
			this.collisionMesh.setMargin(0.05f);
		}
		
		Assimp.aiReleaseImport(aiScene);
		Logger.info(this, "Mesh loaded.");
	}
	
	
	private Map<Integer, List<VertexWeight>> generateWeightSet(
		List<Skeleton.Bone> boneList, PointerBuffer aiBoneBuffer
	) {
		Map<Integer, List<VertexWeight>> weightSet = new HashMap<>();
		
		if( aiBoneBuffer == null ) {
			return weightSet;
		}
		
		while( aiBoneBuffer.remaining() > 0 ) {
			AIBone aiBone = AIBone.create(aiBoneBuffer.get());
			int boneID = boneList.size();
			Skeleton.Bone bone = new Skeleton.Bone(
				boneID, 
				aiBone.mName().dataString(), 
				GeometryUtils.aiMatrix4ToMatrix4f(aiBone.mOffsetMatrix())
			);
			boneList.add(bone);
			
			int weightCount = aiBone.mNumWeights();
			AIVertexWeight.Buffer aiWeights = aiBone.mWeights();
			
			for( int i = 0; i < weightCount; i++ ) {
				AIVertexWeight aiWeight = aiWeights.get(i);
				VertexWeight weight = new VertexWeight(
					bone.getID(), aiWeight.mVertexId(), aiWeight.mWeight()
				);
				List<VertexWeight> weightList = weightSet.get(weight.getVertexID());
				
				if( weightList == null ) {
					weightList = new ArrayList<>();
					weightSet.put(weight.getVertexID(), weightList);
				}
				
				weightList.add(weight);
			}
		}
		
		aiBoneBuffer.flip(); // Reset the buffer cursor
		return weightSet;
	}
	
	private void extractBoneIndicesAndWeights(
		float[] weights, int[] boneIDs, Map<Integer, List<VertexWeight>> weightSet, int vertexIndex
	) {
		List<VertexWeight> weightList = weightSet.get(vertexIndex);
		int weightCount = (weightList != null) ? weightList.size() : 0;
		
		for( int i = 0; i < MAX_WEIGHT_COUNT; i++ ) {
			int index = vertexIndex * MAX_WEIGHT_COUNT + i;
			if( i < weightCount ) {
				VertexWeight weight = weightList.get(i);
				weights[index] = weight.getWeight();
				boneIDs[index] = weight.getBoneID();
			} else {
				weights[index] = 0.0f;
				boneIDs[index] = 0;
			}
		}
	}

	@Override
	public void deload() {
		Logger.info(this, "Deloading mesh '" + this.name + "'...");
		
		for( Submesh s : this.submeshes ) {
			s.dipose();
		}
		
		this.submeshes = new Submesh[0];
		Logger.info(this, "Mesh deloaded.");
	}
	
	public void renderSubmesh(int index) {
		if( !ArrayUtils.isInBounds(index, this.submeshes) ) {
			Logger.spam(this, "Warning: Trying to renderer a non-existing submesh of mesh '" + this.name + "' with index " + index + "!");
			return;
		}
		
		this.submeshes[index].render();
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getPath() {
		return this.path;
	}
	
	public int getSubmeshCount() {
		return this.submeshes.length;
	}
	
	public Submesh getSubmesh(int index) {
		return this.submeshes[index];
	}
	
	public Skeleton getSkeleton() {
		return this.skeleton;
	}
	
	public CollisionShape getCollisionMesh() {
		return this.collisionMesh;
	}
}
