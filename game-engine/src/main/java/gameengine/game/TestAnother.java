package gameengine.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.renderer.IRenderPass;
import gameengine.engine.renderer.Mesh;
import gameengine.engine.renderer.Texture;
import gameengine.util.FileUtils;

public class TestAnother implements IGameObject {
    private Matrix4f modelMatrix;
    private Vector3f position;
    private Quaternionf rotation;
    private float scale;
    private Model model;
    private float rot;

    public TestAnother() {
        modelMatrix = new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = 1;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public final void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setRotation(float x, float y, float z, float angle) {
        this.rotation.fromAxisAngleRad(x, y, z, angle);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void updateModelMatrix() {
        modelMatrix.translationRotateScale(position, rotation, scale);
    }

	@Override
	public void tick(float deltaTime) {
		Engine.getInstance().getWindow().getInput().DEBUGmapInput(12315, (e) -> {
			Vector4f position = new Vector4f();
			position.zero();
			//position.x -= 1 * deltaTime;
			//position.y -= 1 * deltaTime;
			position.z -= 1 * deltaTime;
			this.setPosition(position.x + this.getPosition().x, position.y + this.getPosition().y, position.z + this.getPosition().z);
			this.updateModelMatrix();
		});
		
		Engine.getInstance().getWindow().getInput().DEBUGmapInput(12314, (e) -> {
			Vector4f position = new Vector4f();
			position.zero();
			//position.x -= 1 * deltaTime;
			//position.y -= 1 * deltaTime;
			position.z += 1 * deltaTime;
			this.setPosition(position.x + this.getPosition().x, position.y + this.getPosition().y, position.z + this.getPosition().z);
			this.updateModelMatrix();
		});
		
		Engine.getInstance().getWindow().getInput().DEBUGmapInput(12384, (e) -> {
			Vector4f position = new Vector4f();
			position.zero();
			//position.x -= 1 * deltaTime;
			//position.y -= 1 * deltaTime;
			position.w += 1 * deltaTime;
			this.setScale(this.getScale() + position.w);
			this.updateModelMatrix();
		});
		
		Engine.getInstance().getWindow().getInput().DEBUGmapInput(12383, (e) -> {
			Vector4f position = new Vector4f();
			position.zero();
			//position.x -= 1 * deltaTime;
			//position.y -= 1 * deltaTime;
			position.w -= 1 * deltaTime;
			this.setScale(this.getScale() + position.w);
			this.updateModelMatrix();
		});
		
        rot += 10*deltaTime;
        
        if (rot > 360) {
            rot = 0;
        }
        
		this.setRotation(1, 1, 1, (float) Math.toRadians(rot));
		this.updateModelMatrix();
	}

	@Override
	public void render(IRenderPass renderPass) {
		renderPass.getUniformManager().setUniform("modelMatrix", this.getModelMatrix());
		this.model.render(renderPass);
	}

	@Override
	public void onCreate() {
		float[] positions = new float[]{
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,

            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,

            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,

            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,

            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f,
        };
        float[] textCoords = new float[]{
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,

            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,

            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,

            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,

            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,

            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,
        };
        int[] indices = new int[]{
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7
        };
		
        Mesh mesh = new Mesh(positions, textCoords, indices);
        List<Mesh> meshes = new ArrayList<>();
        meshes.add(mesh);
        
        Texture texture = new Texture(FileUtils.getResourcePath("texture/texture.png"));
        Material material = new Material();
        material.setTexture(0, texture);
        
        List<Material> materials = new ArrayList<>();
        materials.add(material);
        this.model = new Model(meshes, materials);
        modelMatrix = new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = 1;
	}
}
