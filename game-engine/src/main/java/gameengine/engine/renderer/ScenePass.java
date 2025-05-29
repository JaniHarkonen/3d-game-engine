package gameengine.engine.renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL46;

import gameengine.engine.IScene;
import gameengine.game.Game;
import gameengine.util.FileUtils;

public class ScenePass implements IRenderPass {
    private ShaderProgram shaderProgram;
    private UniformManager uniformsMap;

    public ScenePass() {
        List<ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderModuleData(FileUtils.getResourcePath("shader/scene.vert"), GL46.GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderModuleData(FileUtils.getResourcePath("shader/scene.frag"), GL46.GL_FRAGMENT_SHADER));
        shaderProgram = new ShaderProgram(shaderModuleDataList);
        this.createUniforms();
    }
    
    
    private void createUniforms() {
        uniformsMap = new UniformManager(shaderProgram.getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("txtSampler");
        uniformsMap.createUniform("cameraMatrix");
    }

    public void render(Game game) {
    	IScene worldScene = game.getWorldScene();
        shaderProgram.bind();
        uniformsMap.setUniform("txtSampler", 0);
        uniformsMap.setUniform("projectionMatrix", worldScene.getActiveProjection().getProjMatrix());
        uniformsMap.setUniform("cameraMatrix", worldScene.getActiveCamera().getViewMatrix());
        worldScene.render(this);
        GL46.glBindVertexArray(0);
        shaderProgram.unbind();
    }
    
    public void destroy() {
        shaderProgram.cleanup();
    }


	@Override
	public UniformManager getUniformManager() {
		return this.uniformsMap;
	}
}
