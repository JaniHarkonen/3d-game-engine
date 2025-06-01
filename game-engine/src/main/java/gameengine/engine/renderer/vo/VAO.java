package gameengine.engine.renderer.vo;

import org.lwjgl.opengl.GL46;

import gameengine.engine.renderer.component.Submesh;
import gameengine.logger.Logger;

public class VAO {

	private Submesh submesh;
	private int ID;
	private VBO vboVertices;
	private VBO vboNormals;
	private VBO vboUVs;
	private VBO vboIndices;
	
	public VAO(Submesh submesh) {
		this.submesh = submesh;
		this.ID = -1;
		this.vboVertices = null;
		this.vboNormals = null;
		this.vboUVs = null;
		this.vboIndices = null;
	}
	
	
	public void generate() {
		Logger.info(this, "Generating VAO for submesh '" + this.submesh + "'...");
		this.ID = GL46.glGenVertexArrays();
		
        this.bind();
        	this.vboVertices = new VBO(GL46.GL_ARRAY_BUFFER, GL46.GL_FLOAT, 3);
        	this.vboVertices.generate(this.submesh.getVertices());
        	this.vboVertices.enable(0);
        	Logger.info(this, "Vertices VBO @ 0");
        	
        	this.vboNormals = new VBO(GL46.GL_ARRAY_BUFFER, GL46.GL_FLOAT, 3);
        	this.vboNormals.generate(this.submesh.getNormals());
        	this.vboNormals.enable(1);
        	Logger.info(this, "Normals VBO @ 1");
        	
        	this.vboUVs = new VBO(GL46.GL_ARRAY_BUFFER, GL46.GL_FLOAT, 2);
        	this.vboUVs.generate(this.submesh.getUVs());
        	this.vboUVs.enable(2);
        	Logger.info(this, "UVs VBO @ 2");
        	
        	this.vboIndices = new VBO(GL46.GL_ELEMENT_ARRAY_BUFFER, GL46.GL_INT, 0);
        	this.vboIndices.generate(Submesh.Face.facesToIndices(this.submesh.getFaces()));
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
		this.vboNormals.dispose();
		this.vboUVs.dispose();
		this.vboIndices.dispose();
		GL46.glDeleteVertexArrays(this.ID);
		Logger.info(this, "VAO disposed for submesh '" + this.submesh + "'.");
		this.submesh = null;
	}
}
