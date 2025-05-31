package gameengine.game;

import org.joml.Vector4f;

import gameengine.engine.asset.Texture;
import gameengine.engine.renderer.uniform.object.material.SSMaterial;

public class Material {
	public static final int DIFFUSE = 0;
	
	public static final int DEFAULT_TEXTURE_SLOT_COUNT = 1;
	public static final Vector4f DEFAULT_AMBIENT_COLOR = 
		new Vector4f(0.0f, 0.0f, 0.0f, 1.0f); // pulled from fbx via Assimp
	public static final Vector4f DEFAULT_DIFFUSE_COLOR = 
		new Vector4f(0.8f, 0.8f, 0.8f, 1.0f); // pulled from fbx via Assimp
	public static final Vector4f DEFAULT_SPECULAR_COLOR = 
		new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // pulled from fbx via Assimp
	public static final float DEFAULT_REFLECTANCE = 0.0f;

	private Texture[] textureSlot;
	private Vector4f ambientColor;
	private Vector4f diffuseColor;
	private Vector4f specularColor;
	private float reflectance;
	private SSMaterial materialStruct;
	
	public Material() {
		this.textureSlot = new Texture[Material.DEFAULT_TEXTURE_SLOT_COUNT];
		this.ambientColor = Material.DEFAULT_AMBIENT_COLOR;
		this.diffuseColor = Material.DEFAULT_DIFFUSE_COLOR;
		this.specularColor = Material.DEFAULT_SPECULAR_COLOR;
		this.reflectance = 0.0f;
		this.materialStruct = new SSMaterial();
	}
	
	
	private void updateStruct() {
		this.materialStruct.ambient = this.ambientColor;
		this.materialStruct.diffuse = this.diffuseColor;
		this.materialStruct.specular = this.specularColor;
		this.materialStruct.reflectance = this.reflectance;
	}
	
	public void setTexture(int textureSlot, Texture texture) {
		this.textureSlot[textureSlot] = texture;
	}
	
	public void setAmbientColor(Vector4f ambientColor) {
		this.ambientColor = ambientColor;
		this.updateStruct();
	}
	
	public void setDiffuseColor(Vector4f diffuseColor) {
		this.diffuseColor = diffuseColor;
		this.updateStruct();
	}
	
	public void setSpecularColor(Vector4f specularColor) {
		this.specularColor = specularColor;
		this.updateStruct();
	}
	
	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
		this.updateStruct();
	}
	
	public Texture[] getTextures() {
		return this.textureSlot;
	}
	
	public Texture getTexture(int textureSlot) {
		return this.textureSlot[textureSlot];
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
		return this.materialStruct;
	}
}
