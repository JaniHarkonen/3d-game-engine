package gameengine.engine.renderer;

import org.lwjgl.opengl.GL46;

import gameengine.engine.IGameObject;
import gameengine.engine.renderer.shader.Shader;
import gameengine.engine.renderer.shader.ShaderProgram;
import gameengine.engine.renderer.uniform.UniformManager;
import gameengine.util.FileUtils;

public class ScenePass extends ARenderPass<IGameObject> {
	public static final String VERTEX_SHADER = "shd-scene-vert";
	public static final String FRAGMENT_SHADER = "shd-scene-frag";
	
	Projection projection;
    Camera camera;

    public ScenePass() {
    	super();
    }
    
    
    private void createUniforms() {
        uniformsMap = new UniformManager(shaderProgram.getID());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("txtSampler");
        uniformsMap.createUniform("cameraMatrix");
    }
    
    @Override
    public void setup() {
    	Shader sceneVertex = new Shader(VERTEX_SHADER, FileUtils.getResourcePath("shader/scene.vert"), GL46.GL_VERTEX_SHADER);
    	Shader sceneFragment = new Shader(FRAGMENT_SHADER, FileUtils.getResourcePath("shader/scene.frag"), GL46.GL_FRAGMENT_SHADER);
    	
    	this.shaderProgram = new ShaderProgram();
    	this.shaderProgram.addShader(sceneVertex);
    	this.shaderProgram.addShader(sceneFragment);
    	this.shaderProgram.generate();
    	this.createUniforms();
    }

    @Override
    public void execute() {
        this.shaderProgram.bind();
        this.uniformsMap.setUniform("txtSampler", 0);
        this.uniformsMap.setUniform("projectionMatrix", this.projection.getProjMatrix());
        this.uniformsMap.setUniform("cameraMatrix", this.camera.getViewMatrix());
        
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
