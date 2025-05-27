package gameengine.engine.renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL46;

import gameengine.game.Game;
import gameengine.util.FileUtils;

public class ScenePass {
    private ShaderProgram shaderProgram;

    public ScenePass() {
        List<ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderModuleData(FileUtils.getResourcePath("shader/scene.vert"), GL46.GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderModuleData(FileUtils.getResourcePath("shader/scene.frag"), GL46.GL_FRAGMENT_SHADER));
        shaderProgram = new ShaderProgram(shaderModuleDataList);
    }

    public void render(Game game) {
        shaderProgram.bind();
        game.getWorldScene().render();
        GL46.glBindVertexArray(0);
        shaderProgram.unbind();
    }
    
    public void destroy() {
        shaderProgram.cleanup();
    }
}
