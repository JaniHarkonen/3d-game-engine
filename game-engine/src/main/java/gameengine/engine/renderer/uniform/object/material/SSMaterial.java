package gameengine.engine.renderer.uniform.object.material;

import org.joml.Vector4f;

import gameengine.engine.renderer.uniform.IShaderStruct;

public class SSMaterial implements IShaderStruct {

	public int hasNormalMap;
	public int hasRoughnessMap;
	public Vector4f ambient;
	public Vector4f diffuse;
	public Vector4f specular;
	public float reflectance;
}
