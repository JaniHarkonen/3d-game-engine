package gameengine.engine.asset;

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

import gameengine.engine.renderer.component.Submesh;
import gameengine.logger.Logger;
import gameengine.util.ArrayUtils;
import gameengine.util.GeometryUtils;

public class Mesh implements IAsset {
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
    private int importFlags;
    
    	// If a skeleton is found, it will be placed in this field, otherwise null
    private Skeleton skeleton;

    public Mesh(String name, String path, int importFlags) {
    	this.name = name;
    	this.path = path;
    	this.submeshes = new Submesh[0];
        this.importFlags = importFlags;
        this.skeleton = new Skeleton();
    }
    
    public Mesh(String name, String path) {
    	this(name, path, DEFAULT_IMPORT_FLAGS);
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
		this.submeshes = new Submesh[submeshCount];
		
		Logger.info(this, "Found " + submeshCount + " submeshes.");
		
		List<Bone> boneList = new ArrayList<>();
		PointerBuffer aiMeshBuffer = aiScene.mMeshes();
		
		for( int i = 0; i < submeshCount; i++ ) {
			AIMesh aiMesh = AIMesh.create(aiMeshBuffer.get(i));
			
			Logger.spam(this, "Found submesh '" + aiMesh.mName().dataString() + "' (" + i + 1 + " / " + submeshCount + ").");
			
			Vector3f[] normals = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mNormals());
			//Vector3f[] tangents = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mTangents());
			//Vector3f[] bitangents = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mBitangents());
			Vector2f[] UVs = GeometryUtils.aiVector3DBufferToVector2fArray(aiMesh.mTextureCoords(0));

				// By default, set tangents array should be the same length as the normals array
			/*if( tangents.length == 0 ) {
				tangents = new Vector3f[normals.length];
				Arrays.fill(tangents, new Vector3f(0.0f));
				Logger.warn(this, "Submesh '" + aiMesh.mName().dataString() + "' of mesh '" + this.name + "' contains no tangents!");
			}
			
				// By default, set bitangents array should be the same length as the normals array
			if( bitangents.length == 0 ) {
				bitangents = new Vector3f[normals.length];
				Arrays.fill(bitangents, new Vector3f(0.0f));
				Logger.warn(this, "Submesh '" + aiMesh.mName().dataString() + "' of mesh '" + this.name + "' contains no bitangents!");
			}*/
			
				// Fix UV-coordinates
			for( Vector2f uv : UVs ) {
				uv.y = 1 - uv.y;
			}
			
				// Extract indices
			int faceCount = aiMesh.mNumFaces();
			Submesh.Face[] faces = new Submesh.Face[faceCount];
			AIFace.Buffer aiFaceBuffer = aiMesh.mFaces();
			
			for( int j = 0; j < faceCount; j++ ) {
				AIFace aiFace = aiFaceBuffer.get(j);
				IntBuffer indexBuffer = aiFace.mIndices();
				int[] indices = new int[Submesh.Face.INDICES_PER_FACE];
				int index = 0;
				
				while( indexBuffer.remaining() > 0 ) {
					indices[index++] = indexBuffer.get();
				}
				
				faces[j] = new Submesh.Face(indices);
			}
			
				// Extract vertices and the bones that affect them
			int vertexCount = aiMesh.mNumVertices();
			Map<Integer, List<VertexWeight>> weightSet = this.generateWeightSet(boneList, aiMesh.mBones());
			int[] boneIDs = new int[vertexCount * MAX_WEIGHT_COUNT];
			float[] weights = new float[vertexCount * MAX_WEIGHT_COUNT];
			AIVector3D.Buffer buffer = aiMesh.mVertices();
			Vector3f[] vertices = new Vector3f[buffer.remaining()];
			
			for( int j = 0; buffer.remaining() > 0; j++ ) {
				AIVector3D aiVector = buffer.get();
				vertices[j] = new Vector3f(aiVector.x(), aiVector.y(), aiVector.z());
				this.extractBoneIndicesAndWeights(weights, boneIDs, weightSet, j);
			}
			
			Submesh submesh = new Submesh();
			submesh.populate(vertices, normals, UVs, faces, boneIDs, weights);
			this.submeshes[i] = submesh;
			
			Logger.spam(this, "Vertex count: " + vertices.length, "UV count: " + UVs.length, "Face count: " + faces.length);
		}
		
		this.skeleton.populate(boneList);
		Assimp.aiReleaseImport(aiScene);
		Logger.info(this, "Mesh loaded.");
	}
	
	
	private Map<Integer, List<VertexWeight>> generateWeightSet(
		List<Bone> boneList, PointerBuffer aiBoneBuffer
	) {
		Map<Integer, List<VertexWeight>> weightSet = new HashMap<>();
		
		if( aiBoneBuffer == null ) {
			return weightSet;
		}
		
		while( aiBoneBuffer.remaining() > 0 ) {
			AIBone aiBone = AIBone.create(aiBoneBuffer.get());
			int boneID = boneList.size();
			Bone bone = new Bone(
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
	
	public Skeleton getSkeleton() {
		return this.skeleton;
	}
}
