package gameengine.game.component;

import gameengine.engine.asset.Mesh;
import gameengine.engine.renderer.CascadeShadowPass;
import gameengine.engine.renderer.IRenderStrategy;
import gameengine.engine.renderer.IRenderable;
import gameengine.engine.renderer.Renderer;
import gameengine.engine.renderer.ScenePass;
import gameengine.engine.renderer.component.Material;
import gameengine.logger.Logger;
import gameengine.util.ArrayUtils;

public class Model implements IRenderable {
	private class SceneRenderer implements IRenderStrategy<ScenePass> {
		
		@Override
		public void render(ScenePass renderPass) {
			renderPass.uObject.update(getTransform().getAsMatrix());
			
			for( int i = 0; i < mesh.getSubmeshCount(); i++ ) {
				Material material = materials[i];
				
				if( material == null ) {
					Logger.spam(
						this, 
						"Warning: Rendering a mesh with no material for its submesh at index " + i + "!"
					);
					continue;
				}
				
				material.bind(ScenePass.SAMPLER_DIFFUSE);
				renderPass.uMaterial.update(material.getAsStruct());
				mesh.renderSubmesh(i);
			}
		}
	}
	
	private class CascadeShadowRenderer implements IRenderStrategy<CascadeShadowPass> {
		
		@Override
		public void render(CascadeShadowPass renderPass) {
			renderPass.uObject.update(getTransform().getAsMatrix());
			
			for( int i = 0; i < mesh.getSubmeshCount(); i++ ) {
				mesh.renderSubmesh(i);
			}
		}
	}
	
	private Mesh mesh;
	private Material[] materials;
	private Transform transform;
	private SceneRenderer sceneRenderer;
	private CascadeShadowRenderer cascadeShadowRenderer;
	
	private Model(Mesh mesh, Material[] materials) {
		this.mesh = mesh;
		this.materials = materials;
		this.transform = new Transform();
		this.sceneRenderer = new SceneRenderer();
		this.cascadeShadowRenderer = new CascadeShadowRenderer();
	}
	
	public Model(Mesh mesh) {
		this(mesh, new Material[mesh.getSubmeshCount()]);
	}
	
	public Model() {
		this(null, new Material[0]);
	}
	
	public Model(Model src) {
		this.mesh = src.mesh;
		this.materials = new Material[src.materials.length];
		this.transform = new Transform(src.transform);
		
		for( int i = 0; i < src.materials.length; i++ ) {
			this.materials[i] = src.materials[i];
		}
	}
	
	
	@Override
	public void submitToRenderer(Renderer renderer) {
		renderer.getCascadeShadowPass().submit(this.cascadeShadowRenderer);
		renderer.getScenePass().submit(this.sceneRenderer);
	}
	
	public void setMaterial(int index, Material material) {
		if( !ArrayUtils.isInBounds(index, this.materials) ) {
			Logger.error(this, "Trying to set the material of a submesh that doesn't exist! Material index: " + index + ".");
			return;
		}
		
		this.materials[index] = material;
	}
	
	public void setMaterial(Material ...material) {
		for( int i = 0; i < material.length; i++ ) {
			this.setMaterial(i, material[i]);
		}
	}
	
	public Transform getTransform() {
		return this.transform;
	}
}
