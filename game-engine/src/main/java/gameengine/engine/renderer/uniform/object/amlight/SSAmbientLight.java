package gameengine.engine.renderer.uniform.object.amlight;

import org.joml.Vector3f;

import gameengine.engine.renderer.uniform.IShaderStruct;

public class SSAmbientLight implements IShaderStruct {

	public Vector3f color;
	public float intensity;
}
