package gameengine.engine.renderer.uniform.object.material;

import org.joml.Vector4f;

import gameengine.engine.renderer.component.Material;
import gameengine.engine.renderer.uniform.IShaderStruct;

public class SSMaterial implements IShaderStruct {

	public int hasNormalMap;
	public int hasRoughnessMap;
	public Vector4f ambient = Material.DEFAULT_AMBIENT_COLOR;
	public Vector4f diffuse = Material.DEFAULT_DIFFUSE_COLOR;
	public Vector4f specular = Material.DEFAULT_SPECULAR_COLOR;
	public float reflectance = Material.DEFAULT_REFLECTANCE;
}
