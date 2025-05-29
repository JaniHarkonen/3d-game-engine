package gameengine.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import gameengine.engine.Engine;
import gameengine.engine.IGameObject;
import gameengine.engine.asset.mesh.Mesh;
import gameengine.engine.asset.texture.Texture;
import gameengine.engine.renderer.IRenderPass;
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
		Vector3f[] vertices = new Vector3f[]{
            new Vector3f(-0.5f, 0.5f, 0.5f),
            new Vector3f(-0.5f, -0.5f, 0.5f),
    		new Vector3f(0.5f, -0.5f, 0.5f),
			new Vector3f(0.5f, 0.5f, 0.5f),
			new Vector3f(-0.5f, 0.5f, -0.5f),
			new Vector3f(0.5f, 0.5f, -0.5f),
			new Vector3f(-0.5f, -0.5f, -0.5f),
			new Vector3f(0.5f, -0.5f, -0.5f),
			
			new Vector3f(-0.5f, 0.5f, -0.5f),
			new Vector3f(0.5f, 0.5f, -0.5f),
			new Vector3f(-0.5f, 0.5f, 0.5f),
			new Vector3f(0.5f, 0.5f, 0.5f),

			new Vector3f(0.5f, 0.5f, 0.5f),
			new Vector3f(0.5f, -0.5f, 0.5f),

			new Vector3f(-0.5f, 0.5f, 0.5f),
			new Vector3f(-0.5f, -0.5f, 0.5f),

			new Vector3f(-0.5f, -0.5f, -0.5f),
			new Vector3f(0.5f, -0.5f, -0.5f),
			new Vector3f(-0.5f, -0.5f, 0.5f),
			new Vector3f(0.5f, -0.5f, 0.5f),
        };
		
        Vector2f[] UVs = new Vector2f[]{
            new Vector2f(0.0f, 0.0f),
    		new Vector2f(0.0f, 0.5f),
			new Vector2f(0.5f, 0.5f),
			new Vector2f(0.5f, 0.0f),

			new Vector2f(0.0f, 0.0f),
			new Vector2f(0.5f, 0.0f),
			new Vector2f(0.0f, 0.5f),
			new Vector2f(0.5f, 0.5f),

			new Vector2f(0.0f, 0.5f),
			new Vector2f(0.5f, 0.5f),
			new Vector2f(0.0f, 1.0f),
			new Vector2f(0.5f, 1.0f),

			new Vector2f(0.0f, 0.0f),
			new Vector2f(0.0f, 0.5f),

			new Vector2f(0.5f, 0.0f),
			new Vector2f(0.5f, 0.5f),

			new Vector2f(0.5f, 0.0f),
			new Vector2f(1.0f, 0.0f),
			new Vector2f(0.5f, 0.5f),
			new Vector2f(1.0f, 0.5f),
        };
        
        Mesh.Face[] faces = new Mesh.Face[]{
        	new Mesh.Face(0, 1, 3), new Mesh.Face(3, 1, 2),
        	new Mesh.Face(8, 10, 11), new Mesh.Face(9, 8, 11),
        	new Mesh.Face(12, 13, 7), new Mesh.Face(5, 12, 7),
        	new Mesh.Face(14, 15, 6), new Mesh.Face(4, 14, 6),
        	new Mesh.Face(16, 18, 19), new Mesh.Face(17, 16, 19),
        	new Mesh.Face(4, 6, 7), new Mesh.Face(5, 4, 7)
        };
		
        Mesh mesh = new Mesh();
        mesh.populate(vertices, UVs, faces);
        List<Mesh> meshes = new ArrayList<>();
        meshes.add(mesh);
        
        //Texture texture = new Texture(FileUtils.getResourcePath("texture/texture.png"));
        Texture texture = Engine.getInstance().getGame().getAssets().getTexture("tex-default");
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
