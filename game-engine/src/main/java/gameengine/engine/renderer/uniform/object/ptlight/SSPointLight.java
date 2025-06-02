package gameengine.engine.renderer.uniform.object.ptlight;

import org.joml.Vector3f;
import org.joml.Vector4f;

import gameengine.engine.renderer.uniform.IShaderStruct;
import gameengine.engine.renderer.uniform.object.attenuation.SSAttenuation;

public class SSPointLight implements IShaderStruct {

	public Vector3f position;
	public Vector3f color;
	public float intensity;
	public SSAttenuation attenuation;
}
