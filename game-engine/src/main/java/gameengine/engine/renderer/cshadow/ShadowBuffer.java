package gameengine.engine.renderer.cshadow;

import org.lwjgl.opengl.GL46;

import gameengine.engine.asset.Image;
import gameengine.engine.renderer.CascadeShadowPass;
import gameengine.engine.renderer.component.Material;
import gameengine.engine.renderer.component.TextureGL;
import gameengine.logger.Logger;

public class ShadowBuffer {
	private int fboDepthMap;
	private Material depthMaterial;
	
	public ShadowBuffer() {
		this.fboDepthMap = -1;
		this.depthMaterial = null;
	}
	

	public void generate() {
		Logger.info(this, "Generating cascade shadow buffer...");
			// Generate textures that will be used as depth maps by the cascade shadow
		int cascadeCount = CascadeShadowPass.SHADOW_MAP_CASCADE_COUNT;
		TextureGL[] depthTextures = new TextureGL[cascadeCount];

		for( int i = 0; i < cascadeCount; i++ ) {
			Image image = new Image(null, CascadeShadowPass.CASCADE_SHADOW_MAP_WIDTH, CascadeShadowPass.CASCADE_SHADOW_MAP_HEIGHT);
			TextureGL depthTexture = new TextureGL(
				image, GL46.GL_DEPTH_COMPONENT, GL46.GL_DEPTH_COMPONENT, GL46.GL_FLOAT, false
			);
			depthTexture.generate();
			depthTextures[i] = depthTexture;
		}
		
		this.depthMaterial = new Material(depthTextures);
		
			// Generate frame buffer that will be used to render the cascade shadow
		this.fboDepthMap = GL46.glGenFramebuffers();
		GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, this.fboDepthMap);
		GL46.glFramebufferTexture2D(
			GL46.GL_FRAMEBUFFER, 
			GL46.GL_DEPTH_ATTACHMENT, 
			GL46.GL_TEXTURE_2D, 
			((TextureGL) this.depthMaterial.getTexture(0)).getID(), 
			0
		);
		
		GL46.glDrawBuffer(GL46.GL_NONE);
		GL46.glReadBuffer(GL46.GL_NONE);
		
		if( GL46.glCheckFramebufferStatus(GL46.GL_FRAMEBUFFER) != GL46.GL_FRAMEBUFFER_COMPLETE ) {
			Logger.error(this, "Unable to create a framebuffer for cascade shadows!");
		} else {
			Logger.info(this, "Cascade shadow buffer generated. FBO:" + this.fboDepthMap);
		}
		
		GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
	}
	
	public void bind(int firstTextureIndex) {
		this.depthMaterial.bind(firstTextureIndex);
	}
	
	public void dispose() {
		for( TextureGL depthTexture : (TextureGL[]) this.depthMaterial.getTextures() ) {
			depthTexture.dispose();
		}
		
		GL46.glDeleteFramebuffers(this.fboDepthMap);
		this.fboDepthMap = -1;
		this.depthMaterial = null;
		Logger.info(this, "Cascade shadow buffer disposed.");
	}
	
	public int getDepthMapFBO() {
		return this.fboDepthMap;
	}
	
	public Material getDepthMaterial() {
		return this.depthMaterial;
	}
}
