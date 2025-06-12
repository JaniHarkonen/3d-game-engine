package gameengine.util;

import org.joml.Matrix4f;

import gameengine.engine.physics.Transform;

public final class PhysicsUtils {

	public static com.bulletphysics.linearmath.Transform transformToBulletphysicsTransform(Transform transform) {
		Matrix4f transformMatrix = transform.getAsMatrix();
		javax.vecmath.Matrix4f bulletMatrix = GeometryUtils.matrix4fToJavaxMatrix4f(transformMatrix);
		return new com.bulletphysics.linearmath.Transform(bulletMatrix);
	}
}
