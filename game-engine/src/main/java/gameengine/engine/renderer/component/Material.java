package gameengine.engine.renderer.component;

import org.joml.Vector4f;

import gameengine.engine.asset.ITexture;
import gameengine.engine.asset.Texture;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.uniform.object.material.SSMaterial;
import gameengine.logger.Logger;

public class Material {
	public static final int DIFFUSE = 0;
	
	public static final Vector4f DEFAULT_AMBIENT_COLOR = 
		new Vector4f(0.0f, 0.0f, 0.0f, 1.0f); // pulled from fbx via Assimp
	public static final Vector4f DEFAULT_DIFFUSE_COLOR = 
		new Vector4f(0.0f, 0.0f, 0.0f, 1.0f); // pulled from fbx via Assimp
	public static final Vector4f DEFAULT_SPECULAR_COLOR = 
		new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // pulled from fbx via Assimp
	public static final float DEFAULT_REFLECTANCE = 0.5f;
	
	public static Material create(Texture texture) {
		if( texture == null ) {
			Logger.error("Material.create()", "Trying to create a material based on a null texture!");
			return null;
		}
		
		Material material = new Material();
		material.setTexture(DIFFUSE, texture);
		return material;
	}

	private ITexture[] textures;
	private Vector4f ambientColor;
	private Vector4f diffuseColor;
	private Vector4f specularColor;
	private float reflectance;
	private SSMaterial materialStruct;
	
	public Material(ITexture[] textures) {
		this.textures = textures;
		this.ambientColor = Material.DEFAULT_AMBIENT_COLOR;
		this.diffuseColor = Material.DEFAULT_DIFFUSE_COLOR;
		this.specularColor = Material.DEFAULT_SPECULAR_COLOR;
		this.reflectance = 0.0f;
		this.materialStruct = new SSMaterial();
		this.updateStruct();
	}
	
	public Material() {
		this(new Texture[Renderer.MAX_TEXTURE_COUNT]);
	}
	
	public Material(Material src) {
		this.textures = new Texture[src.textures.length];
		this.ambientColor = new Vector4f(src.ambientColor);
		this.diffuseColor = new Vector4f(src.diffuseColor);
		this.specularColor = new Vector4f(src.specularColor);
		this.reflectance = src.reflectance;
		this.materialStruct = new SSMaterial();
		this.updateStruct();
		
		for( int i = 0; i < src.textures.length; i++ ) {
			this.textures[i] = src.textures[i];
		}
	}
	
	
	private void updateStruct() {
		this.materialStruct.ambient = this.ambientColor;
		this.materialStruct.diffuse = this.diffuseColor;
		this.materialStruct.specular = this.specularColor;
		this.materialStruct.reflectance = this.reflectance;
	}
	
	public void bind(int samplerOffset) {
		for( int i = 0; i < textures.length; i++ ) {
			ITexture texture = textures[i];
			
			if( texture == null ) {
				if( i == 0 ) {
					Logger.spam(this, "Warning: Binding a material with no textures! Material index: " + i + ".");
				}
				
				break;
			}
			
			texture.active(samplerOffset + i);
		}
	}
	
	public void setTexture(int index, Texture texture) {
		this.textures[index] = texture;
	}
	
	public void setAmbientColor(Vector4f ambientColor) {
		this.ambientColor = ambientColor;
	}
	
	public void setDiffuseColor(Vector4f diffuseColor) {
		this.diffuseColor = diffuseColor;
	}
	
	public void setSpecularColor(Vector4f specularColor) {
		this.specularColor = specularColor;
	}
	
	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}
	
	public ITexture[] getTextures() {
		return this.textures;
	}
	
	public ITexture getTexture(int index) {
		return this.textures[index];
	}
	
	public Vector4f getAmbientColor() {
		return this.ambientColor;
	}
	
	public Vector4f getDiffuseColor() {
		return this.diffuseColor;
	}
	
	public Vector4f getSpecularColor() {
		return this.specularColor;
	}
	
	public float getReflectance() {
		return this.reflectance;
	}
	
	public SSMaterial getAsStruct() {
		this.updateStruct();
		return this.materialStruct;
	}
}
