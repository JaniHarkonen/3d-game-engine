package gameengine.engine.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import gameengine.engine.IRenderable;

public class Mesh implements IRenderable {

    private int numVertices;
    private int vaoId;
    private List<Integer> vboIdList;

    public Mesh(float[] positions, int[] indices) {
        this.numVertices = indices.length;
        vboIdList = new ArrayList<>();

        vaoId = GL46.glGenVertexArrays();
        GL46.glBindVertexArray(vaoId);

        // Positions VBO
        int vboId = GL46.glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer positionsBuffer = MemoryUtil.memCallocFloat(positions.length);
        positionsBuffer.put(0, positions);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, positionsBuffer, GL46.GL_STATIC_DRAW);
        GL46.glEnableVertexAttribArray(0);
        GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);

        // Index VBO
        vboId = GL46.glGenBuffers();
        vboIdList.add(vboId);
        IntBuffer indicesBuffer = MemoryUtil.memCallocInt(indices.length);
        indicesBuffer.put(0, indices);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, vboId);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindVertexArray(0);

        MemoryUtil.memFree(positionsBuffer);
        MemoryUtil.memFree(indicesBuffer);
    }
    
	@Override
	public void render() {
		GL46.glBindVertexArray(this.vaoId);
		GL46.glDrawElements(GL46.GL_TRIANGLES, this.numVertices, GL46.GL_UNSIGNED_INT, 0);
	}

    public void destroy() {
        for( Integer vbo : vboIdList ) {
        	GL46.glDeleteBuffers(vbo);
        }
        
        GL46.glDeleteVertexArrays(vaoId);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public final int getVaoId() {
        return vaoId;
    }
}
