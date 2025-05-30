package gameengine.engine.renderer.vo;

import org.lwjgl.opengl.GL46;

import gameengine.engine.asset.Mesh;
import gameengine.logger.Logger;

public class VAO {

	private Mesh mesh;
	private int ID;
	private VBO vboVertices;
	private VBO vboUVs;
	private VBO vboIndices;
	
	public VAO(Mesh mesh) {
		this.mesh = mesh;
		this.ID = -1;
		this.vboVertices = null;
		this.vboUVs = null;
		this.vboIndices = null;
	}
	
	
	public void generate() {
		Logger.info(this, "Generating VAO for mesh '" + this.mesh + "'...");
		this.ID = GL46.glGenVertexArrays();
		
        this.bind();
        	this.vboVertices = new VBO(GL46.GL_ARRAY_BUFFER, GL46.GL_FLOAT, 3);
        	this.vboVertices.generate(this.mesh.getVertices());
        	this.vboVertices.enable(0);
        	Logger.info(this, "Vertices VBO @ 0");
        	
        	this.vboUVs = new VBO(GL46.GL_ARRAY_BUFFER, GL46.GL_FLOAT, 2);
        	this.vboUVs.generate(this.mesh.getUVs());
        	this.vboUVs.enable(1);
        	Logger.info(this, "UVs VBO @ 1");
        	
        	this.vboIndices = new VBO(GL46.GL_ELEMENT_ARRAY_BUFFER, GL46.GL_INT, 0);
        	this.vboIndices.generate(Mesh.Face.facesToIndices(this.mesh.getFaces()));
        	Logger.info(this, "Indices VBO OK");
        this.unbind();
	}
	
	public void bind() {
		GL46.glBindVertexArray(this.ID);
	}
	
	public void unbind() {
		GL46.glBindVertexArray(0);
	}
	
	public void dispose() {
		this.unbind();
		this.vboVertices.dispose();
		this.vboUVs.dispose();
		this.vboIndices.dispose();
		GL46.glDeleteVertexArrays(this.ID);
		Logger.info(this, "VAO disposed for mesh '" + this.mesh + "'.");
		this.mesh = null;
	}
}
