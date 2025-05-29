package gameengine.engine.asset.mesh;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

import gameengine.engine.IRenderable;
import gameengine.engine.renderer.IRenderPass;
import gameengine.engine.renderer.vo.VAO;

public class Mesh implements IRenderable {
	
	public static class Face {
		public static final int INDICES_PER_FACE = 3;
		
		public static int[] facesToIndices(Face[] faces) {
			int[] indices = new int[faces.length * INDICES_PER_FACE];
			
			int index = 0;
			for( Face f : faces ) {
				indices[index++] = f.get1();
				indices[index++] = f.get2();
				indices[index++] = f.get3();
			}
			
			return indices;
		}
		
		private int[] indices;
		
		public Face(int i1, int i2, int i3) {
			this.indices = new int[3];
			this.indices[0] = i1;
			this.indices[1] = i2;
			this.indices[2] = i3;
		}
		
		public Face(int[] indices) {
			this.indices = indices;
		}
		
		
		public int getIndex(int index) {
			return this.indices[index];
		}
		
		public int get1() {
			return this.getIndex(0);
		}
		
		public int get2() {
			return this.getIndex(1);
		}
		
		public int get3() {
			return this.getIndex(2);
		}
	}
	
	
	private VAO vao;
    private Vector3f[] vertices;
    private Vector2f[] UVs;
    private Face[] faces;

    public Mesh() {
    	this.vao = null;
    	this.vertices = null;
    	this.UVs = null;
    	this.faces = null;
    }
    
    
    public void populate(Vector3f[] vertices, Vector2f[] UVs, Face[] faces) {
    	this.vertices = vertices;
    	this.UVs = UVs;
    	this.faces = faces;
    	
    	this.vao = new VAO(this);
    	this.vao.generate();
    }
    
	@Override
	public void render(IRenderPass renderPass) {
		this.vao.bind();
		GL46.glDrawElements(GL46.GL_TRIANGLES, this.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0);
	}

    public void dipose() {
    	this.vao.dispose();
    }
    
    public int getVertexCount() {
    	return this.vertices.length;
    }
    
    public Vector3f[] getVertices() {
    	return this.vertices;
    }
    
    public Vector2f[] getUVs() {
    	return this.UVs;
    }
    
    public Face[] getFaces() {
    	return this.faces;
    }
}
