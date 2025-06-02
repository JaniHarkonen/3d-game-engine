package gameengine.engine.renderer.uniform.object.splight;

import org.joml.Vector3f;

import gameengine.engine.renderer.uniform.IShaderStruct;
import gameengine.engine.renderer.uniform.object.ptlight.SSPointLight;

public class SSSpotLight implements IShaderStruct {

	public SSPointLight pointLight;
	public Vector3f cone;
	public float threshold;
}
