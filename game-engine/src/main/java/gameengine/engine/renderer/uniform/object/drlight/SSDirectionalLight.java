package gameengine.engine.renderer.uniform.object.drlight;

import org.joml.Vector3f;

import gameengine.engine.renderer.uniform.IShaderStruct;

public class SSDirectionalLight implements IShaderStruct {

    public Vector3f color;
    public Vector3f direction;
    public float intensity;
}
