package gameengine.game;

import org.lwjgl.opengl.GL46;

import gameengine.engine.asset.Texture;
import gameengine.engine.renderer.ScenePass;
import gameengine.logger.Logger;
import gameengine.util.ArrayUtils;

public class Model {

	private Mesh mesh;
	private Material[] materials;
	
	public Model(Mesh mesh) {
		this.mesh = mesh;
		this.materials = new Material[this.mesh.getSubmeshCount()];
	}
	
	public Model() {
		this.mesh = null;
		this.materials = new Material[0];
	}
	
	
	public void render(ScenePass renderPass) {
		for( int i = 0; i < this.mesh.getSubmeshCount(); i++ ) {
			Material material = this.materials[i];
			
			if( material == null ) {
				Logger.spam(this, "Warning: Rendering a mesh with no material for its submesh at index " + i + "!");
				continue;
			}
			
			Texture[] textures = material.getTextures();
			
			if( textures.length == 0 ) {
				Logger.spam(this, "Warning: Rendering a material with no textures! Material index: " + i + ".");
				continue;
			}
			
			for( int j = 0; j < textures.length; j++ ) {
				Texture texture = textures[j];
				
				if( texture == null ) {
					break;
				}
				
				GL46.glActiveTexture(GL46.GL_TEXTURE0 + j);
				texture.bind();
			}
			
			this.mesh.renderSubmesh(i);
		}
	}
	
	public void setMaterial(int index, Material material) {
		if( !ArrayUtils.isInBounds(index, this.materials) ) {
			Logger.error(this, "Trying to set the material of a submesh that doesn't exist! Material index: " + index + ".");
		}
		
		this.materials[index] = material;
	}
}
