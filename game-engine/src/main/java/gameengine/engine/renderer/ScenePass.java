package gameengine.engine.renderer;

import org.lwjgl.opengl.GL46;

import gameengine.engine.IScene;
import gameengine.engine.renderer.shader.Shader;
import gameengine.engine.renderer.shader.ShaderProgram;
import gameengine.engine.renderer.uniform.UniformManager;
import gameengine.game.Game;
import gameengine.util.FileUtils;

public class ScenePass implements IRenderPass {
	public static final String VERTEX_SHADER = "shd-scene-vert";
	public static final String FRAGMENT_SHADER = "shd-scene-frag";
	
    private ShaderProgram shaderProgram;
    private UniformManager uniformsMap;

    public ScenePass() {
    	this.shaderProgram = null;
    }
    
    
    private void createUniforms() {
        uniformsMap = new UniformManager(shaderProgram.getID());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("txtSampler");
        uniformsMap.createUniform("cameraMatrix");
    }
    
    public void setup() {
    	Shader sceneVertex = new Shader("shd-scene-vert", FileUtils.getResourcePath("shader/scene.vert"), GL46.GL_VERTEX_SHADER);
    	Shader sceneFragment = new Shader("shd-scene-frag", FileUtils.getResourcePath("shader/scene.frag"), GL46.GL_FRAGMENT_SHADER);
    	
    	this.shaderProgram = new ShaderProgram();
    	this.shaderProgram.addShader(sceneVertex);
    	this.shaderProgram.addShader(sceneFragment);
    	this.shaderProgram.generate();
    	this.createUniforms();
    }

    public void render(Game game) {
    	IScene worldScene = game.getWorldScene();
        this.shaderProgram.bind();
        this.uniformsMap.setUniform("txtSampler", 0);
        this.uniformsMap.setUniform("projectionMatrix", worldScene.getActiveProjection().getProjMatrix());
        this.uniformsMap.setUniform("cameraMatrix", worldScene.getActiveCamera().getViewMatrix());
        worldScene.render(this);
        GL46.glBindVertexArray(0);
        this.shaderProgram.unbind();
    }
    
    public void dispose() {
        shaderProgram.dispose();
    }


	@Override
	public UniformManager getUniformManager() {
		return this.uniformsMap;
	}
}
