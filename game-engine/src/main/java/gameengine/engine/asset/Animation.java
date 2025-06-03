package gameengine.engine.asset;

import java.util.Arrays;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuatKey;
import org.lwjgl.assimp.AIQuaternion;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVectorKey;
import org.lwjgl.assimp.Assimp;

import gameengine.logger.Logger;
import gameengine.util.GeometryUtils;

public class Animation implements IAsset {
	
	/************************* Frame-class **************************/
	
	public static class Frame {

		private Matrix4f[] boneTransforms;
		
		public Frame(Matrix4f[] boneTransforms) {
			this.boneTransforms = boneTransforms;
		}
		
		
		public void setBoneTransform(int boneIndex, Matrix4f boneTransform) {
			this.boneTransforms[boneIndex] = boneTransform;
		}
		
		public Matrix4f getBoneTransform(int boneIndex) {
			return this.boneTransforms[boneIndex];
		}
		
		public Matrix4f[] getBoneTransforms() {
			return this.boneTransforms;
		}
	}
	
	
	/************************* Animation-class **************************/
	
	public static final int DEFAULT_IMPORT_FLAGS = (
		Assimp.aiProcess_GenSmoothNormals |
		Assimp.aiProcess_Triangulate | 
		Assimp.aiProcess_FixInfacingNormals | 
		Assimp.aiProcess_CalcTangentSpace | 
		Assimp.aiProcess_LimitBoneWeights
	);
	
	private final String name;
	private String path;
	private Frame[] frames;
	private int importFlags;
	private Skeleton DEBUGskeleton;
	
	public Animation(String name, String path) {
		this.name = name;
		this.path = path;
		this.frames = new Frame[0];
		this.importFlags = DEFAULT_IMPORT_FLAGS;
		this.DEBUGskeleton = null;
	}
	
	
	@Override
	public void load() {
		Logger.info(this, "Loading mesh '" + this.name + "' from: ", this.path);
		
        AIScene aiScene = Assimp.aiImportFile(this.path, this.importFlags);
        
        if( aiScene == null ) {
        	Logger.error(
    			this, 
    			"Failed to load Assimp scene for animation '" + this.name + "' from: ", 
    			this.path, 
    			"Assimp log:", 
    			Assimp.aiGetErrorString()
			);
        	return;
        }
        
		int animationCount = aiScene.mNumAnimations();
		
		if( animationCount == 0 ) {
			Logger.error(this, "Failed to find any animations in Assimp scene!");
			return;
		} else if( animationCount == 1 ) {
			Logger.info(this, "Animation found.");
		} else if( animationCount > 1 ) {
			Logger.warn(
				this, "Found " + animationCount + ", while expecting only 1!", "Only the first animation will be loaded!"
			);
		}
		
		Node rootNode = this.buildNodesTree(aiScene.mRootNode(), null);
		Matrix4f globalInverseTransform = GeometryUtils.aiMatrix4ToMatrix4f(
			aiScene.mRootNode().mTransformation()
		).invert();
		AIAnimation aiAnimation = AIAnimation.create(aiScene.mAnimations().get(0));
		
			// Build frame transforms for each key frame of the animation
		int frameCount = this.findMaxFrameCount(aiAnimation);
		Animation.Frame[] frames = new Animation.Frame[frameCount];
		
		for( int i = 0; i < frameCount; i++ ) {
			Matrix4f[] boneTransforms = new Matrix4f[Mesh.MAX_BONE_COUNT];
			Arrays.fill(boneTransforms, new Matrix4f());
			Animation.Frame frame = new Animation.Frame(boneTransforms);
			this.buildFrameTransforms(
				aiAnimation, 
				this.DEBUGskeleton.getBones(), 
				frame, 
				i, 
				rootNode, 
				rootNode.getNodeTransform(), 
				globalInverseTransform
			);
			frames[i] = frame;
		}
		
		this.populate(frames);
		
		Assimp.aiReleaseImport(aiScene);
		Logger.info(this, "Animation loaded.");
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
	
    private int findMaxFrameCount(AIAnimation aiAnimation) {
        int frameCount = 0;
        int channelCount = aiAnimation.mNumChannels();
        PointerBuffer aiChannels = aiAnimation.mChannels();
        
        for( int i = 0; i < channelCount; i++ ) {
            AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(i));
            int positionKeyCount = aiNodeAnim.mNumPositionKeys();
            int rotationKeyCount = aiNodeAnim.mNumRotationKeys();
            int scalingKeyCount = aiNodeAnim.mNumScalingKeys();
            int maxKeys = Math.max(Math.max(positionKeyCount, rotationKeyCount), scalingKeyCount);
            frameCount = Math.max(frameCount, maxKeys);
        }

        return frameCount;
    }

	@Override
	public void deload() {
		this.frames = new Frame[0];
	}
	
	public void populate(Frame[] frames) {
		this.frames = frames;
	}
	
	public void DEBUGsetSkeleton(Skeleton skeleton) {
		this.DEBUGskeleton = skeleton;
	}
	
	public Frame[] getFrames() {
		return this.frames;
	}
	
	public Frame getFrame(int frameIndex) {
		return this.frames[frameIndex];
	}
	
	public int getFrameCount() {
		return this.frames.length;
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
