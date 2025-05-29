package gameengine.engine.renderer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import gameengine.engine.Engine;

public class Camera {
	private Vector3f direction;
    private Vector3f position;
    private Vector3f right;
    private Vector2f rotation;
    private Vector3f up;
    private Matrix4f viewMatrix;

    public Camera() {
        direction = new Vector3f();
        right = new Vector3f();
        up = new Vector3f();
        position = new Vector3f();
        viewMatrix = new Matrix4f();
        rotation = new Vector2f();
    }

    public void addRotation(float x, float y) {
        rotation.add(x, y);
        recalculate();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }
    
    public void DEBUGupdate() {
    	float speed = 0.5f;
    	
    	
    	// W
    	Engine.getInstance().getWindow().getInput().DEBUGmapInput(12137, (e) -> {
    		this.moveForward(speed);
    	});

    	// A
    	Engine.getInstance().getWindow().getInput().DEBUGmapInput(12115, (e) -> {
    		this.moveLeft(speed);
    	});
    	
    	// S
    	Engine.getInstance().getWindow().getInput().DEBUGmapInput(12133, (e) -> {
    		this.moveBackwards(speed);
    	});
    	
    	// D
    	Engine.getInstance().getWindow().getInput().DEBUGmapInput(12118, (e) -> {
    		this.moveRight(speed);
    	});
    	
    	// Mouse pan
    	Engine.getInstance().getWindow().getInput().DEBUGmapInput(24000, (e) -> {
            this.addRotation((float) Math.toRadians(e.mouseDeltaY * 0.1f),
                    (float) Math.toRadians(e.mouseDeltaX * 0.1f));
    	});
    }

    public void moveBackwards(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.sub(direction);
        recalculate();
    }

    public void moveDown(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.sub(up);
        recalculate();
    }

    public void moveForward(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.add(direction);
        recalculate();
    }

    public void moveLeft(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.sub(right);
        recalculate();
    }

    public void moveRight(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.add(right);
        recalculate();
    }

    public void moveUp(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.add(up);
        recalculate();
    }

    private void recalculate() {
        viewMatrix.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        recalculate();
    }

    public void setRotation(float x, float y) {
        rotation.set(x, y);
        recalculate();
    }
}
