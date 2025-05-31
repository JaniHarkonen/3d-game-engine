package gameengine.engine.renderer.uniform.object.cshadow;

import org.joml.Matrix4f;

import gameengine.engine.renderer.uniform.IShaderStruct;

public class SSCascadeShadow implements IShaderStruct {

	public Matrix4f lightView;
    public float splitDistance;
}
