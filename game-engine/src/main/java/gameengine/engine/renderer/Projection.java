package gameengine.engine.renderer;

import org.joml.Matrix4f;

public class Projection {
    public static final float DEFAULT_FOV = (float) Math.toRadians(60.0f);
    public static final float DEFAULT_Z_NEAR = 0.01f;
    public static final float DEFAULT_Z_FAR = 1000.f;
    public static final int DEFAULT_WIDTH = 640;
    public static final int DEFAULT_HEIGHT = 480;

    
    private Matrix4f projectionMatrix;
    private float fov;
    private int width;
    private int height;
    private float zNear;
    private float zFar;

    public Projection(float fov, int width, int height, float zNear, float zFar) {
        this.projectionMatrix = new Matrix4f();
        this.fov = fov;
        this.width = width;
        this.height = height;
        this.zNear = zNear;
        this.zFar = zFar;
    }
    
    public Projection(int width, int height) {
    	this(DEFAULT_FOV, width, height, DEFAULT_Z_NEAR, DEFAULT_Z_FAR);
    }
    
    public Projection() {
    	this(DEFAULT_FOV, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_Z_NEAR, DEFAULT_Z_FAR);
    }
    
    
    private void recalculate() {
    	this.projectionMatrix.setPerspective(this.fov, (float) (this.width / this.height), this.zNear, this.zFar);
    }
    
    public Matrix4f getAsMatrix() {
    	this.recalculate();
        return this.projectionMatrix;
    }
    
    public int getWidth() {
    	return this.width;
    }
    
    public int getHeight() {
    	return this.height;
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
