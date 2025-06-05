package gameengine.engine.renderer.cshadow;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import gameengine.engine.renderer.CascadeShadowPass;
import gameengine.engine.renderer.component.Camera;
import gameengine.engine.renderer.component.Projection;

public class CascadeShadow {
	private int cascadeIndex;
	private Matrix4f lightView;
	private float splitDistance;
	
	public CascadeShadow(int cascadeIndex) {
		this.cascadeIndex = cascadeIndex;
		this.lightView = new Matrix4f();
		this.splitDistance = 0.0f;
	}
    
    public float update(Camera activeCamera, Vector3f light, float cascadeOffset) {
        Matrix4f cameraTransform = activeCamera.getTransform().getAsMatrix();
        Projection cameraProjection = activeCamera.getProjection();
        Matrix4f projectionMatrix = cameraProjection.getAsMatrix();
        
        float cascadeSplitLambda = 0.95f;
        float near = cameraProjection.getZNear();
        float far = cameraProjection.getZFar();
        float range = far - near;
        float ratio = far/ near;

        float p = (this.cascadeIndex + 1) / (float) (CascadeShadowPass.SHADOW_MAP_CASCADE_COUNT);
        float log = (float) (near * Math.pow(ratio, p));
        float uniform = near + range * p;
        float d = cascadeSplitLambda * (log - uniform) + uniform;
        float splitLength = (d - near) / range;

        	// Calculate orthographic projection matrix for each cascade
        Vector3f[] frustumCorners = new Vector3f[]{
            new Vector3f(-1.0f, 1.0f, -1.0f),
            new Vector3f(1.0f, 1.0f, -1.0f),
            new Vector3f(1.0f, -1.0f, -1.0f),
            new Vector3f(-1.0f, -1.0f, -1.0f),
            new Vector3f(-1.0f, 1.0f, 1.0f),
            new Vector3f(1.0f, 1.0f, 1.0f),
            new Vector3f(1.0f, -1.0f, 1.0f),
            new Vector3f(-1.0f, -1.0f, 1.0f),
        };

        	// Project frustum corners into world space
        Matrix4f worldMatrix = (new Matrix4f(projectionMatrix).mul(cameraTransform)).invert();
        for( int j = 0; j < 8; j++ ) {
            Vector4f invCorner = new Vector4f(frustumCorners[j], 1.0f).mul(worldMatrix);
            frustumCorners[j] = new Vector3f(
        		invCorner.x / invCorner.w, invCorner.y / invCorner.w, invCorner.z / invCorner.w
    		);
        }

        for( int j = 0; j < 4; j++ ) {
            Vector3f dist = new Vector3f(frustumCorners[j + 4]).sub(frustumCorners[j]);
            frustumCorners[j + 4] = new Vector3f(frustumCorners[j]).add(new Vector3f(dist).mul(splitLength));
            frustumCorners[j] = new Vector3f(frustumCorners[j]).add(new Vector3f(dist).mul(cascadeOffset));
        }

        	// Get frustum center
        Vector3f frustumCenter = new Vector3f(0.0f);
        
        for( int j = 0; j < 8; j++ ) {
            frustumCenter.add(frustumCorners[j]);
        }
        
        float radius = 0.0f;
        frustumCenter.div(8.0f);
        
        for( int j = 0; j < 8; j++ ) {
            float distance = (new Vector3f(frustumCorners[j]).sub(frustumCenter)).length();
            radius = java.lang.Math.max(radius, distance);
        }
        
        radius = (float) Math.ceil(radius * 16.0f) / 16.0f;

        Vector3f maxExtents = new Vector3f(radius);
        Vector3f minExtents = new Vector3f(maxExtents).mul(-1);

        Vector3f lightDirection = (new Vector3f(light).mul(-1)).normalize();
        Vector3f eye = new Vector3f(frustumCenter).sub(new Vector3f(lightDirection).mul(-minExtents.z));
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Matrix4f lightViewMatrix = new Matrix4f().lookAt(eye, frustumCenter, up);
        Matrix4f lightOrthoMatrix = new Matrix4f().ortho(
    		minExtents.x, maxExtents.x, minExtents.y, maxExtents.y, 0.0f, maxExtents.z - minExtents.z, true
		);

        	// Store split distance and matrix in cascade
        this.splitDistance = (near + splitLength * range) * -1.0f;
        this.lightView = lightOrthoMatrix.mul(lightViewMatrix);
        
        return splitLength;
    }
	
	public Matrix4f getLightViewMatrix() {
		return this.lightView;
	}
	
	public float getSplitDistance() {
		return this.splitDistance;
	}
}
