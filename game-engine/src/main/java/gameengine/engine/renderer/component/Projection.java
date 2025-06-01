package gameengine.engine.renderer.component;

import org.joml.Matrix4f;

public class Projection {
    public static final float DEFAULT_FOV = (float) Math.toRadians(60.0f);
    public static final float DEFAULT_ASPECT_RATIO = 16 / 9;
    public static final float DEFAULT_Z_NEAR = 0.01f;
    public static final float DEFAULT_Z_FAR = 1000.f;
    
    private Matrix4f projectionMatrix;
    private float fov;
    private float zNear;
    private float zFar;
    private float aspectRatio;

    public Projection(float fov, float aspectRatio, float zNear, float zFar) {
        this.projectionMatrix = new Matrix4f();
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.zNear = zNear;
        this.zFar = zFar;
    }
    
    public Projection() {
    	this(DEFAULT_FOV, DEFAULT_ASPECT_RATIO, DEFAULT_Z_NEAR, DEFAULT_Z_FAR);
    }
    
    
    private void recalculate() {
    	this.projectionMatrix.setPerspective(this.fov, this.aspectRatio, this.zNear, this.zFar);
    }
    
    public void setAspectRatio(float aspectRatio) {
    	this.aspectRatio = aspectRatio;
    }
    
    public void setFOV(float fov) {
    	this.fov = (float) Math.toRadians(fov);
    }
    
    public Matrix4f getAsMatrix() {
    	this.recalculate();
        return this.projectionMatrix;
    }
    
    public float getFOV() {
    	return this.fov;
    }
    
    public float getZNear() {
    	return this.zNear;
    }
    
    public float getZFar() {
    	return this.zFar;
    }
}
