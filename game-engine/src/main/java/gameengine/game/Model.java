package gameengine.game;

import java.nio.IntBuffer;
import java.util.Arrays;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.GL46;

import gameengine.engine.asset.IAsset;
import gameengine.engine.asset.Mesh;
import gameengine.engine.asset.Texture;
import gameengine.engine.renderer.ScenePass;
import gameengine.logger.Logger;
import gameengine.util.GeometryUtils;

public class Model implements IAsset {
	public static final int DEFAULT_IMPORT_FLAGS = (
		Assimp.aiProcess_GenSmoothNormals |
		Assimp.aiProcess_JoinIdenticalVertices |
		Assimp.aiProcess_Triangulate | 
		Assimp.aiProcess_FixInfacingNormals | 
		Assimp.aiProcess_CalcTangentSpace | 
		Assimp.aiProcess_LimitBoneWeights |
		Assimp.aiProcess_PreTransformVertices
	);
	
	private final String name;
	private String path;
    private Mesh[] meshes;
    private Material[] meshMaterials;
    private int importFlags;

    public Model(String name, String path) {
    	this.name = name;
    	this.path = path;
        this.reset();
        this.importFlags = DEFAULT_IMPORT_FLAGS;
    }
    
    
    private void reset() {
    	this.meshes = new Mesh[0];
    	this.meshMaterials = new Material[0];
    }
	
	@Override
	public void load() {
		/*int preTransformVerticesFlag = (
			this.expectedAnimations.size() > 0 ? 
			0 : Assimp.aiProcess_PreTransformVertices
		);*/
		/*AIScene aiScene = Assimp.aiImportFile(
			this.assetPath, 
			this.importFlags | preTransformVerticesFlag
		);*/
		
		Logger.info(this, "Loading model '" + this.name + "' from: ", this.path);
		
        AIScene aiScene = Assimp.aiImportFile(this.path, this.importFlags);
        
        if( aiScene == null ) {
        	Logger.error(this, "Failed to load model '" + this.name + "' from: ", this.path, "Assimp log:", Assimp.aiGetErrorString());
        	return;
        }
		
			//////////////////////////// Extract meshes ////////////////////////////
		int meshCount = aiScene.mNumMeshes();
		this.meshes = new Mesh[meshCount];
		this.meshMaterials = new Material[meshCount];
		
		//List<Bone> boneList = new ArrayList<>();
		PointerBuffer aiMeshBuffer = aiScene.mMeshes();
		
		for( int i = 0; i < meshCount; i++ ) {
			AIMesh aiMesh = AIMesh.create(aiMeshBuffer.get(i));
			
			Logger.spam(this, "Found mesh '" + aiMesh.mName().dataString() + "' (" + i + 1 + " / " + meshCount + ").");
			
			Vector3f[] normals = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mNormals());
			Vector3f[] tangents = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mTangents());
			Vector3f[] bitangents = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mBitangents());
			Vector2f[] UVs = GeometryUtils.aiVector3DBufferToVector2fArray(aiMesh.mTextureCoords(0));

				// By default, set tangents array should be the same length as the normals array
			if( tangents.length == 0 ) {
				tangents = new Vector3f[normals.length];
				Arrays.fill(tangents, new Vector3f(0.0f));
				Logger.warn(this, "Mesh '" + aiMesh.mName().dataString() + "' of model '" + this.name + "' contains no tangents!");
			}
			
				// By default, set bitangents array should be the same length as the normals array
			if( bitangents.length == 0 ) {
				bitangents = new Vector3f[normals.length];
				Arrays.fill(bitangents, new Vector3f(0.0f));
				Logger.warn(this, "Mesh '" + aiMesh.mName().dataString() + "' of model '" + this.name + "' contains no bitangents!");
			}
			
				// Fix UV-coordinates
			for( Vector2f uv : UVs ) {
				uv.y = 1 - uv.y;
			}
			
				// Extract indices
			//List<Mesh.Face> faces = new ArrayList<>();
			int faceCount = aiMesh.mNumFaces();
			Mesh.Face[] faces = new Mesh.Face[faceCount];
			AIFace.Buffer aiFaceBuffer = aiMesh.mFaces();
			
			for( int j = 0; j < faceCount; j++ ) {
				AIFace aiFace = aiFaceBuffer.get(j);
				IntBuffer indexBuffer = aiFace.mIndices();
				int[] indices = new int[Mesh.Face.INDICES_PER_FACE];
				int index = 0;
				
				while( indexBuffer.remaining() > 0 ) {
					indices[index++] = indexBuffer.get();
				}
				
				faces[j] = new Mesh.Face(indices);
				//faces.add(new Mesh.Face(indices));
			}
			
				// Extract vertices and the bones that affect them
			//Map<Integer, List<VertexWeight>> weightSet = this.generateWeightSet(boneList, aiMesh.mBones());
			int vertexCount = aiMesh.mNumVertices();
			//int[] boneIDs = new int[vertexCount * MAX_WEIGHT_COUNT];
			//float[] weights = new float[vertexCount * MAX_WEIGHT_COUNT];
			AIVector3D.Buffer buffer = aiMesh.mVertices();
			Vector3f[] vertices = new Vector3f[buffer.remaining()];
			
			for( int j = 0; buffer.remaining() > 0; j++ ) {
				AIVector3D aiVector = buffer.get();
				vertices[j] = new Vector3f(aiVector.x(), aiVector.y(), aiVector.z());
				//this.extractBoneIndicesAndWeights(weights, boneIDs, weightSet, j);
			}
			
			Mesh resultMesh = new Mesh();
			resultMesh.populate(vertices, UVs, faces);
			this.meshes[i] = resultMesh;
			
			Logger.spam(this, "Vertex count: " + vertices.length, "UV count: " + UVs.length, "Face count: " + faces.length);
			
			/*
			Mesh.Data data = new Mesh.Data();
			data.targetMesh = this.expectedMeshes.get(i);
			data.vertices = vertices; 
			data.normals = normals; 
			data.tangents = tangents; 
			data.bitangents = bitangents;
			data.UVs = UVs;
			data.faces = faces.toArray(new Mesh.Face[faces.size()]);
			data.weights = weights;
			data.boneIDs = boneIDs;
			this.notifyAssetManager(data.targetMesh, data, Application.getApp().getRenderer());
			*/
		}
		
			////////////////////////////Extract animations ////////////////////////////
		/*int animationCount = aiScene.mNumAnimations();
		int expectedAnimationCount = this.expectedAnimations.size();
		
			// Animation count mismatch
		if( animationCount != expectedAnimationCount ) {
			Logger.get().warn(
				this, 
				"Expected " + expectedAnimationCount + " animations, " + 
				"found " + animationCount + "!"
			);
		}
		
		animationCount = Math.min(animationCount, expectedAnimationCount);
		
		if( animationCount > 0 ) {
			Node rootNode = this.buildNodesTree(aiScene.mRootNode(), null);
			Matrix4f globalInverseTransform = GeometryUtils.aiMatrix4ToMatrix4f(
				aiScene.mRootNode().mTransformation()
			).invert();
			PointerBuffer aiAnimations = aiScene.mAnimations();
			for( int i = 0; i < animationCount; i++ ) {
				this.loadAnimation(
					AIAnimation.create(aiAnimations.get(i)), 
					this.expectedAnimations.get(i), 
					boneList, 
					rootNode, 
					globalInverseTransform
				);
			}
		}
		
		Assimp.aiReleaseImport(aiScene);*/
		Logger.info(this, "Model loaded.");
	}
	
	/*
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
			for( int k = 0; k < weightCount; k++ ) {
				AIVertexWeight aiWeight = aiWeights.get(k);
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
		for( int j = 0; j < MAX_WEIGHT_COUNT; j++ ) {
			int index = vertexIndex * MAX_WEIGHT_COUNT + j;
			if( j < weightCount ) {
				VertexWeight weight = weightList.get(j);
				weights[index] = weight.getWeight();
				boneIDs[index] = weight.getBoneID();
			} else {
				weights[index] = 0.0f;
				boneIDs[index] = 0;
			}
		}
	}
	
	private void loadAnimation(
		AIAnimation aiAnimation, 
		Animation targetAnimation, 
		List<Bone> boneList, 
		Node rootNode, 
		Matrix4f globalInverseTransform
	) {
			// Build frame transforms for each key frame of the animation
		Animation.Frame[] frames = new Animation.Frame[targetAnimation.getExpectedFrameCount()];
		for( int i = 0; i < targetAnimation.getExpectedFrameCount(); i++ ) {
			Matrix4f[] boneTransforms = new Matrix4f[MAX_BONE_COUNT];
			Arrays.fill(boneTransforms, IDENTITY_MATRIX);
			Animation.Frame frame = new Animation.Frame(boneTransforms);
			this.buildFrameTransforms(
				aiAnimation, 
				boneList, 
				frame, 
				i, 
				rootNode, 
				rootNode.getNodeTransform(), 
				globalInverseTransform
			);
			frames[i] = frame;
		}
		
		Animation.Data animationData = new Animation.Data();
		animationData.targetAnimation = targetAnimation;
		animationData.duration = (float) aiAnimation.mDuration();
		animationData.frames = frames;
		this.notifyAssetManager(animationData.targetAnimation, animationData, null);
	}
	
	private void buildFrameTransforms(
		AIAnimation aiAnimation, 
		List<Bone> boneList, 
		Animation.Frame frame, 
		int frameIndex, 
		Node node, 
		Matrix4f parentTransform, 
		Matrix4f globalInverseTransform
	) {
		String nodeName = node.getName();
		AINodeAnim aiNodeAnim = this.findAIAnimationNode(aiAnimation, nodeName);
		Matrix4f nodeTransform = node.getNodeTransform();
		
			// Build node transform matrix if this node is associated with an animation node
		if( aiNodeAnim != null ) {
			nodeTransform = this.buildNodeTransform(aiNodeAnim, frameIndex);
		}
		
			// Apply node's transform to each Bone of the Assimp scene
		Matrix4f nodeGlobalTransform = new Matrix4f(parentTransform).mul(nodeTransform);
		for( Bone bone : boneList ) {
			if( bone.getName().equals(nodeName) ) {
				Matrix4f boneTransform = new Matrix4f(globalInverseTransform)
				.mul(nodeGlobalTransform)
				.mul(bone.getOffsetTransform());
				frame.setBoneTransform(bone.getID(), boneTransform);
			}
		}
		
			// Recurse through child nodes
		for( Node childNode : node.getChildren() ) {
			this.buildFrameTransforms(
				aiAnimation, 
				boneList, 
				frame, 
				frameIndex, 
				childNode, 
				nodeGlobalTransform, 
				globalInverseTransform
			);
		}
	}
	
	private Node buildNodesTree(AINode aiNode, Node parentNode) {
		String nodeName = aiNode.mName().dataString();
		Node node = new Node(
			nodeName, parentNode, GeometryUtils.aiMatrix4ToMatrix4f(aiNode.mTransformation())
		);
		
		int childCount = aiNode.mNumChildren();
		for( int i = 0; i < childCount; i++ ) {
			node.addChild(this.buildNodesTree(AINode.create(aiNode.mChildren().get(i)), node));
		}
		
		return node;
	}
	
	private Matrix4f buildNodeTransform(AINodeAnim aiNodeAnim, int frameIndex) {
        Matrix4f nodeTransform = new Matrix4f();
        
        	// Apply frame translation
        int positionCount = aiNodeAnim.mNumPositionKeys();
        if( positionCount > 0 ) {
        	AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
        	AIVector3D aiTranslation = positionKeys.get(Math.min(positionCount - 1, frameIndex)).mValue();
            nodeTransform.translate(aiTranslation.x(), aiTranslation.y(), aiTranslation.z());
        }
        
        	// Apply frame rotation
        int rotationKeyCount = aiNodeAnim.mNumRotationKeys();
        if( rotationKeyCount > 0 ) {
        	AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();
            AIQuaternion aiQuat = rotationKeys.get(Math.min(rotationKeyCount - 1, frameIndex)).mValue();
            Quaternionf quat = new Quaternionf(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
            nodeTransform.rotate(quat);
        }
        
        	// Apply frame scaling
        int scalingKeyCount = aiNodeAnim.mNumScalingKeys();
        if( scalingKeyCount > 0 ) {
        	AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
        	AIVector3D aiScaling = scalingKeys.get(Math.min(scalingKeyCount - 1, frameIndex)).mValue();
            nodeTransform.scale(aiScaling.x(), aiScaling.y(), aiScaling.z());
        }

        return nodeTransform;
	}
	
	private AINodeAnim findAIAnimationNode(AIAnimation aiAnimation, String nodeName) {
		int animationNodeCount = aiAnimation.mNumChannels();
		PointerBuffer aiChannels = aiAnimation.mChannels();
		
		for( int i = 0; i < animationNodeCount; i++ ) {
			AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(i));
			if( nodeName.equals(aiNodeAnim.mNodeName().dataString()) ) {
				return aiNodeAnim;
			}
		}
		
		return null;
	}*/

	@Override
	public void deload() {
		Logger.info(this, "Deloading model '" + this.name + "'...");
		
		for( Mesh m : this.meshes ) {
			m.dipose();
		}
		
		this.reset();
		Logger.info(this, "Model deloaded.");
	}

	public void render(ScenePass scenePass) {
		if( this.meshes.length == 0 ) {
			Logger.spam(this, "WARNING: Attempting to render a model with 0 meshes!");
			return;
		}
		
		if( this.meshMaterials.length == 0 ) {
			Logger.spam(this, "WARNING: Attempting to render a model with 0 mesh materials!");
			return;
		}
		
		if( this.meshes.length != this.meshMaterials.length ) {
			Logger.spam(
				this, 
				"WARNING: Rendering a model with different number of meshes and materials! Meshes: " + 
				this.meshes.length + ", materials: " + this.meshMaterials.length + "."
			);
		}
		
		for( int i = 0; i < this.meshes.length; i++ ) {
			Material material = this.meshMaterials[i];
			Texture[] materialTextures = material.getTextures();
			
			for( int j = 0; j < materialTextures.length; j++ ) {
				Texture texture = materialTextures[j];
				
				if( texture == null ) {
					break;
				}
				
				GL46.glActiveTexture(GL46.GL_TEXTURE0 + j);
				texture.bind();
			}
			
			scenePass.uMaterial.update(material.getAsStruct());
			this.meshes[i].render();
		}
	}
	
	public void setMaterial(int index, Material material) {
		this.meshMaterials[index] = material;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getPath() {
		return this.path;
	}
}
