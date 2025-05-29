package gameengine.engine.renderer.vo;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

public class VBO {
	
	private final int type;
	private final int size;
	private final int target;
	private int ID;

	public VBO(int target, int type, int size) {
		this.target = target;
		this.type = type;
		this.size = size;
		this.ID = -1;
	}
	
	
	VBO generate(float[] data) {
        this.bufferStart();
        GL46.glBufferData(this.target, data, GL46.GL_STATIC_DRAW);
        return this;
	}
	
	VBO generate(int[] data) {
		this.bufferStart();
		GL46.glBufferData(this.target, data, GL46.GL_STATIC_DRAW);
		return this;
	}
	
	VBO generate(Vector3f[] data) {
        float[] floatData = new float[data.length * 3];
        
        int index = 0; 
        for( Vector3f v : data ) {
        	floatData[index++] = v.x;
        	floatData[index++] = v.y;
        	floatData[index++] = v.z;
        }
        
        this.generate(floatData);
        return this;
	}
	
	VBO generate(Vector2f[] data) {
        float[] floatData = new float[data.length * 2];
        
        int index = 0; 
        for( Vector2f v : data ) {
        	floatData[index++] = v.x;
        	floatData[index++] = v.y;
        }
        
        this.generate(floatData);
        return this;
	}
	
	private void bufferStart() {
		this.ID = GL46.glGenBuffers();
        this.bind();
	}
	
	void bind() {
		GL46.glBindBuffer(this.target, this.ID);
	}
	
	void enable(int index) {
		this.bind();
		GL46.glEnableVertexAttribArray(index);
        GL46.glVertexAttribPointer(index, this.size, this.type, false, 0, 0);
	}
	
	void dispose() {
		GL46.glDeleteBuffers(this.ID);
	}
}
