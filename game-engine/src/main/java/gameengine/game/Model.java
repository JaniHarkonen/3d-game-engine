package gameengine.game;

import java.util.List;

import org.lwjgl.opengl.GL46;

import gameengine.engine.IRenderable;
import gameengine.engine.asset.Mesh;
import gameengine.engine.renderer.IRenderPass;

public class Model implements IRenderable {
    private List<Mesh> meshList;
    private List<Material> materialList;

    public Model(List<Mesh> meshList, List<Material> materialList) {
        this.meshList = meshList;
        this.materialList = materialList;
    }

    public void destroy() {
        
    }

    public List<Mesh> getMeshList() {
        return meshList;
    }

	@Override
	public void render(IRenderPass renderPass) {
		for( int i = 0; i < this.meshList.size(); i++ ) {
			for( int j = 0; j < this.materialList.get(i).getTextures().length; j++ ) {
				GL46.glActiveTexture(GL46.GL_TEXTURE0 + j);
				
				if( this.materialList.get(i).getTextures()[j] == null ) {
					break;
				}
				
				this.materialList.get(i).getTextures()[j].bind();
			}
			
			this.meshList.get(i).render(renderPass);
		}
	}
}
