package gameengine.engine.renderer;

import org.lwjgl.opengl.GL46;

import gameengine.engine.IGameObject;
import gameengine.engine.renderer.shader.Shader;
import gameengine.engine.renderer.shader.ShaderProgram;
import gameengine.engine.renderer.uniform.UAMatrix4f;
import gameengine.engine.renderer.uniform.UInteger1;
import gameengine.util.FileUtils;

public class ScenePass extends ARenderPass<IGameObject> {
	public static final String VERTEX_SHADER = "shd-scene-vert";
	public static final String FRAGMENT_SHADER = "shd-scene-frag";
	
	public final UAMatrix4f uProjection;
	public final UAMatrix4f uCamera;
	public final UAMatrix4f uModel;
	public final UInteger1 uDiffuseSampler;
	
    Camera camera;

    public ScenePass() {
    	super();
    	this.uProjection = new UAMatrix4f("uProjection");
    	this.uCamera = new UAMatrix4f("uCamera");
    	this.uModel = new UAMatrix4f("uModel");
    	this.uDiffuseSampler = new UInteger1("uDiffuseSampler");
    }
    
    
    @Override
    public void setup() {
    	Shader sceneVertex = new Shader(VERTEX_SHADER, FileUtils.getResourcePath("shader/scene.vert"), GL46.GL_VERTEX_SHADER);
    	Shader sceneFragment = new Shader(FRAGMENT_SHADER, FileUtils.getResourcePath("shader/scene.frag"), GL46.GL_FRAGMENT_SHADER);
    	
    	this.shaderProgram = new ShaderProgram();
    	this.shaderProgram.addShader(sceneVertex);
    	this.shaderProgram.addShader(sceneFragment);
    	this.shaderProgram.generate();
    	
    	this.shaderProgram.declareUniform(
			this.uProjection,
			this.uCamera,
			this.uModel,
			this.uDiffuseSampler
		);
    }

    @Override
    public void execute() {
        this.shaderProgram.bind();
        this.uDiffuseSampler.update(0);
        this.uProjection.update(this.camera.getProjection().getAsMatrix());
        this.uCamera.update(this.camera.getTransform().getAsMatrix());
        
        for( IGameObject object : this.submissions ) {
        	object.render(this);
        }
        
        GL46.glBindVertexArray(0);
        this.shaderProgram.unbind();
    }
    
    @Override
    public void dispose() {
        this.shaderProgram.dispose();
    }
}
