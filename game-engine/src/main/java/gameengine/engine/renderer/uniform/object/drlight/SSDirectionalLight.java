package gameengine.engine.renderer.uniform.object.drlight;

import org.joml.Vector3f;

import gameengine.engine.renderer.uniform.IShaderStruct;

public class SSDirectionalLight implements IShaderStruct {

	public Vector3f color;
	public float intensity;
    public Vector3f direction;
}
