package gameengine.util;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIVector3D;

import gameengine.engine.renderer.component.Submesh;

public final class GeometryUtils {
	public static final float PI = (float) Math.PI;

	public static Matrix4f aiMatrix4ToMatrix4f(AIMatrix4x4 aiMatrix) {
        Matrix4f result = new Matrix4f();
        result.m00(aiMatrix.a1());
        result.m10(aiMatrix.a2());
        result.m20(aiMatrix.a3());
        result.m30(aiMatrix.a4());
        result.m01(aiMatrix.b1());
        result.m11(aiMatrix.b2());
        result.m21(aiMatrix.b3());
        result.m31(aiMatrix.b4());
        result.m02(aiMatrix.c1());
        result.m12(aiMatrix.c2());
        result.m22(aiMatrix.c3());
        result.m32(aiMatrix.c4());
        result.m03(aiMatrix.d1());
        result.m13(aiMatrix.d2());
        result.m23(aiMatrix.d3());
        result.m33(aiMatrix.d4());

        return result;
	}
	
	public static javax.vecmath.Matrix4f matrix4fToJavaxMatrix4f(Matrix4f matrix4f) {
		javax.vecmath.Matrix4f result = new javax.vecmath.Matrix4f();
		result.m00 = matrix4f.m00();
		result.m01 = matrix4f.m10();
		result.m02 = matrix4f.m20();
		result.m03 = matrix4f.m30();
		
		result.m10 = matrix4f.m01();
		result.m11 = matrix4f.m11();
		result.m12 = matrix4f.m21();
		result.m13 = matrix4f.m31();
		
		result.m20 = matrix4f.m02();
		result.m21 = matrix4f.m12();
		result.m22 = matrix4f.m22();
		result.m23 = matrix4f.m32();
		
		result.m30 = matrix4f.m03();
		result.m31 = matrix4f.m13();
		result.m32 = matrix4f.m23();
		result.m33 = matrix4f.m33();
		
        return result;
	}
	
	public static javax.vecmath.Vector3f vector3fToJavaxVector3f(Vector3f vector3f) {
		return new javax.vecmath.Vector3f(vector3f.x, vector3f.y, vector3f.z);
	}
	
	public static javax.vecmath.Vector3f vector3fToJavaxVector3f(Vector3f vector3f, javax.vecmath.Vector3f dest) {
		dest.set(vector3f.x, vector3f.y, vector3f.z);
		return dest;
	}
	
	public static Vector3f javaxVector3fToVector3f(javax.vecmath.Vector3f vector3f) {
		return new Vector3f(vector3f.x, vector3f.y, vector3f.z);
	}
	
	public static Vector3f[] aiVector3DBufferToVector3fArray(AIVector3D.Buffer buffer) {
		if( buffer == null ) {
			return new Vector3f[0];
		}
		
		Vector3f[] result = new Vector3f[buffer.remaining()];
		
		for( int i = 0; buffer.remaining() > 0; i++ ) {
			AIVector3D aiVector = buffer.get();
			result[i] = new Vector3f(aiVector.x(), aiVector.y(), aiVector.z());
		}
		
		return result;
	}
	
	public static Vector2f[] aiVector3DBufferToVector2fArray(AIVector3D.Buffer buffer) {
		if( buffer == null ) {
			return new Vector2f[0];
		}
		
		Vector2f[] result = new Vector2f[buffer.remaining()];
		for( int i = 0; buffer.remaining() > 0; i++ ) {
			AIVector3D aiVector = buffer.get();
			result[i] = new Vector2f(aiVector.x(), aiVector.y());
		}
		
		return result;
	}
	
	public static float[] vector3fArrayToFloatArray(Vector3f[] vectorArray) {
		float[] result = new float[vectorArray.length * 3];
		for( int i = 0; i < vectorArray.length; i++ ) {
			Vector3f vector = vectorArray[i];
			result[i * 3] = vector.x;
			result[i * 3 + 1] = vector.y;
			result[i * 3 + 2] = vector.z;
		}
		return result;
	}
	
	public static float[] vector2fArrayToFloatArray(Vector2f[] vectorArray) {
		float[] result = new float[vectorArray.length * 2];
		for( int i = 0; i < vectorArray.length; i++ ) {
			Vector2f vector = vectorArray[i];
			result[i * 2] = vector.x;
			result[i * 2 + 1] = vector.y;
		}
		return result;
	}
	
	public static int[] faceArrayToIntArray(Submesh.Face[] faceArray) {
		int[] result = new int[faceArray.length * Submesh.Face.INDICES_PER_FACE];
		for( int i = 0; i < faceArray.length; i++ ) {
			Submesh.Face face = faceArray[i];
			result[i * 3] = face.getIndex(0);
			result[i * 3 + 1] = face.getIndex(1);
			result[i * 3 + 2] = face.getIndex(2);
		}
		return result;
	}
	
	public static Quaternionf getBulletPhysicsTransformRotation(com.bulletphysics.linearmath.Transform transform, Quaternionf dest) {
		javax.vecmath.Quat4f rotation = transform.getRotation(new javax.vecmath.Quat4f());
		dest.set(rotation.x, rotation.y, rotation.z, rotation.w);
		return dest;
	}
	
	public static Vector3f quaternionfToEulerAnglesf(Quaternionf quaternionf, Vector3f dest) {
		float x = quaternionf.x;
		float y = quaternionf.y;
		float z = quaternionf.z;
		float w = quaternionf.w;
		
		float eulerX, eulerY, eulerZ;

			// Roll (x-axis rotation)
		float sinr_cosp = 2 * (w * x + y * z);
		float cosr_cosp = 1 - 2 * (x * x + y * y);
		eulerX = (float) Math.atan2(sinr_cosp, cosr_cosp);

        	// Pitch (y-axis rotation)
        float sinp = 2 * (w * y - z * x);
        
        if( Math.abs(sinp) >= 1 ) {
        	eulerY = (float) Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
        } else {
        	eulerY = (float) Math.asin(sinp);
        }

        	// Yaw (z-axis rotation)
        float siny_cosp = 2 * (w * z + x * y);
        float cosy_cosp = 1 - 2 * (y * y + z * z);
        eulerZ = (float) Math.atan2(siny_cosp, cosy_cosp);
        
        return dest.set(eulerX, eulerY, eulerZ);
	}
	
	public static Vector3f[] copyVector3fArray(Vector3f[] array) {
		Vector3f[] result = new Vector3f[array.length];
		
		for( int i = 0; i < array.length; i++ ) {
			result[i] = new Vector3f(array[i]);
		}
		
		return result;
	}
	
	public static Vector2f[] copyVector2fArray(Vector2f[] array) {
		Vector2f[] result = new Vector2f[array.length];
		
		for( int i = 0; i < array.length; i++ ) {
			result[i] = new Vector2f(array[i]);
		}
		
		return result;
	}
	
	public static Submesh.Face[] copyFaceArray(Submesh.Face[] array) {
		Submesh.Face[] result = new Submesh.Face[array.length];
		
		for( int i = 0; i < array.length; i++ ) {
			result[i] = new Submesh.Face(array[i]);
		}
		
		return result;
	}
}
