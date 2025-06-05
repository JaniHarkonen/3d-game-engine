package gameengine.engine.renderer;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

import gameengine.engine.renderer.component.Camera;
import gameengine.engine.renderer.component.Projection;
import gameengine.engine.renderer.component.TextureGL;
import gameengine.engine.renderer.cshadow.CascadeShadow;
import gameengine.engine.renderer.cshadow.ShadowBuffer;
import gameengine.engine.renderer.shader.Shader;
import gameengine.engine.renderer.shader.ShaderProgram;
import gameengine.engine.renderer.uniform.UAMatrix4f;
import gameengine.logger.Logger;
import gameengine.util.FileUtils;

public class CascadeShadowPass extends ARenderPass<CascadeShadowPass> {
	public static final String VERTEX_SHADER = "shd-cshadow-vert";
	
	public static final int SHADOW_MAP_CASCADE_COUNT = 3;
	public static final int CASCADE_SHADOW_MAP_WIDTH = 2048;
	public static final int CASCADE_SHADOW_MAP_HEIGHT = 2048;
	
	public final UAMatrix4f uLightView;
	public final UAMatrix4f uObject;
	public final UAMatrix4f uBoneMatrices;
	
	public Camera camera;
	public Vector3f directionalLight;
	
	private CascadeShadow[] cascadeShadows;
	private ShadowBuffer shadowBuffer;

    public CascadeShadowPass() {
    	super();
    	this.uLightView = new UAMatrix4f("uLightView");
    	this.uObject = new UAMatrix4f("uObject");
    	this.uBoneMatrices = new UAMatrix4f("uBoneMatrices");

    	this.cascadeShadows = new CascadeShadow[SHADOW_MAP_CASCADE_COUNT];
    	this.shadowBuffer = new ShadowBuffer();
    	
    	this.camera = new Camera(new Projection());
    	this.directionalLight = null;
    }
    
    
    @Override
    public void setup() {
    	Logger.info(this, "Cascade shadow render pass setup started...");
    	Shader sceneVertex = new Shader(
			VERTEX_SHADER, FileUtils.getResourcePath("shader/cshadow/cshadow.vert"), GL46.GL_VERTEX_SHADER
		);
    	
    	this.shaderProgram = new ShaderProgram();
    	this.shaderProgram.addShader(sceneVertex);
    	this.shaderProgram.generate();
    	Logger.info(this, "Shader program generated.");
    	
    	this.shaderProgram.declareUniform(
			this.uLightView,
			this.uObject,
			this.uBoneMatrices
		);
    	
    	this.shadowBuffer.generate();
    	
    	for( int i = 0; i < SHADOW_MAP_CASCADE_COUNT; i++ ) {
    		this.cascadeShadows[i] = new CascadeShadow(i);
    	}
    }

    @Override
    public void execute() {
    	Logger.spam(this, "Rendering cascade shadows...");
        this.shaderProgram.bind();
        
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, this.shadowBuffer.getDepthMapFBO());
        GL46.glViewport(
			0, 0, CASCADE_SHADOW_MAP_WIDTH, CASCADE_SHADOW_MAP_HEIGHT
		);
        
	    	// Pre-rendering is done only once as cascade shadow rendering iterations are 
	    	// considered to belong in "main" rendering
	    int preRenderCount = 0;
		for( IRenderStrategy<CascadeShadowPass> renderer : this.preRender ) {
	    	renderer.render(this);
	    	preRenderCount++;
	    }
		
			// Skip cascade shadow rendering if no directional light has been assigned
		if( this.directionalLight == null ) {
			Logger.spam(this, "Error: Cannot execute cascade shadow pass due to lacking a directional light!");
			GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
			return;
		}

			// Update shadow cascades
		float offset = 0;
		for( CascadeShadow shadow : this.cascadeShadows ) {
			offset = shadow.update(this.camera, this.directionalLight, offset);
		}
		
        int renderCount = 0;
	    for( int i = 0; i < SHADOW_MAP_CASCADE_COUNT; i++ ) {
	    	GL46.glFramebufferTexture2D(
				GL46.GL_FRAMEBUFFER, 
				GL46.GL_DEPTH_ATTACHMENT, 
				GL46.GL_TEXTURE_2D, 
				((TextureGL) this.shadowBuffer.getDepthMaterial().getTexture(i)).getID(), 
				0
			);
	    	
	    	GL46.glClear(GL46.GL_DEPTH_BUFFER_BIT);
	    	this.uLightView.update(this.cascadeShadows[i].getLightViewMatrix());
	        
	        
	    	for( IRenderStrategy<CascadeShadowPass> renderer : this.render ) {	
	        	renderer.render(this);
	        	renderCount++;
	        }
	    }
        
        GL46.glBindVertexArray(0);
        this.shaderProgram.unbind();
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
        Logger.spam(this, "Scene rendered. Pre: " + preRenderCount + ", main: " + renderCount + ".");
    }
    
    @Override
    public void dispose() {
        this.shaderProgram.dispose();
    }
    
    CascadeShadow[] getCascadeShadows() {
    	return this.cascadeShadows;
    }
    
    ShadowBuffer getShadowBuffer() {
    	return this.shadowBuffer;
    }
}
