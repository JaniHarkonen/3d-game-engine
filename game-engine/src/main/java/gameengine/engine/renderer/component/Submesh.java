package gameengine.engine.renderer.component;

import java.util.Arrays;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

import gameengine.engine.asset.Defaults;
import gameengine.engine.asset.Mesh;
import gameengine.engine.renderer.vo.VAO;
import gameengine.util.GeometryUtils;

public class Submesh {
	
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
		
		public Face(Face face) {
			this(face.indices);
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
    private Vector3f[] normals;
    private Vector2f[] UVs;
    private Face[] faces;
    private int[] bones;
    private float[] boneWeights;

    public Submesh() {
    	this.vao = null;
    	this.vertices = null;
    	this.normals = null;
    	this.UVs = null;
    	this.faces = null;
    	this.bones = new int[0];
        this.boneWeights = new float[0];
    }
    
    public Submesh(Submesh src) {
    	this.populate(
			GeometryUtils.copyVector3fArray(src.vertices), 
			GeometryUtils.copyVector3fArray(src.normals), 
			GeometryUtils.copyVector2fArray(src.UVs),
			GeometryUtils.copyFaceArray(src.faces), 
			Arrays.copyOf(src.bones, src.bones.length), 
			Arrays.copyOf(src.boneWeights, src.boneWeights.length)
		);
    }
    
    public void populate(
		Vector3f[] vertices, 
		Vector3f[] normals, 
		Vector2f[] UVs, 
		Face[] faces, 
		int[] bones, 
		float[] boneWeights
	) {
    	this.vertices = vertices;
    	this.normals = normals;
    	this.UVs = UVs;
    	this.faces = faces;
    	this.bones = bones;
    	this.boneWeights = boneWeights;
    	
    	this.vao = new VAO(this);
    	this.vao.generate();
    }
    
    public void populate(
		float[] positions,
		int[] indices
	) {
    	Vector3f[] vertices = new Vector3f[positions.length / 3];
    	int index = 0;
    	for( int i = 0; i < vertices.length; i++ ) {
    		vertices[i] = new Vector3f(positions[index++], positions[index++], positions[index++]);
    	}
    	
    	Vector3f[] normals = new Vector3f[vertices.length];
    	for( int i = 0; i < vertices.length; i++ ) {
    		normals[i] = new Vector3f((float) Math.random(), (float) Math.random(), 0);
    	}
    	
    	Vector2f[] UVs = new Vector2f[vertices.length];
    	for( int i = 0; i < vertices.length; i++ ) {
    		UVs[i] = new Vector2f(0);
    	}
    	
    	Face[] faces = new Face[indices.length / 3];
    	index = 0;
    	
    	for( int i = 0; i < faces.length; i++ ) {
    		faces[i] = new Face(indices[index++], indices[index++], indices[index++]);
    	}
    	
    	int[] bones = new int[vertices.length * Mesh.MAX_WEIGHT_COUNT];
    	float[] boneWeights = new float[vertices.length * Mesh.MAX_WEIGHT_COUNT];
    	
    	this.populate(vertices, normals, UVs, faces, bones, boneWeights);
    }

    
	public void render() {
		this.vao.bind();
		GL46.glDrawElements(GL46.GL_TRIANGLES, this.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0);
	}

    public void dipose() {
    	this.vao.dispose();
    }
    
    public void setBones(int[] bones, float[] boneWeights) {
    	this.bones = bones;
    	this.boneWeights = boneWeights;
    }
    
    public int getVertexCount() {
    	return this.vertices.length;
    }
    
    public Vector3f[] getVertices() {
    	return this.vertices;
    }
    
    public float[] getVerticesAsFloatArray() {
    	float[] vertices = new float[this.vertices.length * 3];
    	int index = 0;
    	
    	for( Vector3f vertex : this.vertices ) {
    		vertices[index++] = vertex.x;
    		vertices[index++] = vertex.y;
    		vertices[index++] = vertex.z;
    	}
    	
    	return vertices;
    }
    
    public Vector3f[] getNormals() {
    	return this.normals;
    }
    
    public Vector2f[] getUVs() {
    	return this.UVs;
    }
    
    public Face[] getFaces() {
    	return this.faces;
    }
    
    public int[] getIndices() {
    	int[] indices = new int[this.faces.length * Face.INDICES_PER_FACE];
    	int index = 0;
    	
    	for( Face face : this.faces ) {
    		indices[index++] = face.get1();
    		indices[index++] = face.get2();
    		indices[index++] = face.get3();
    	}
    	
    	return indices;
    }
    
    public int[] getBones() {
    	return this.bones;
    }
    
    public float[] getBoneWeights() {
    	return this.boneWeights;
    }
    
    public boolean isNull() {
    	return this == Defaults.SUBMESH;
    }
}
