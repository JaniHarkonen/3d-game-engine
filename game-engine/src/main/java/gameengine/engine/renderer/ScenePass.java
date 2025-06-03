package gameengine.engine.renderer;

import org.lwjgl.opengl.GL46;

import gameengine.engine.renderer.shader.Shader;
import gameengine.engine.renderer.shader.ShaderProgram;
import gameengine.engine.renderer.uniform.UAMatrix4f;
import gameengine.engine.renderer.uniform.UArray;
import gameengine.engine.renderer.uniform.UInteger1;
import gameengine.engine.renderer.uniform.object.amlight.UAmbientLight;
import gameengine.engine.renderer.uniform.object.drlight.UDirectionalLight;
import gameengine.engine.renderer.uniform.object.material.UMaterial;
import gameengine.engine.renderer.uniform.object.ptlight.SSPointLight;
import gameengine.engine.renderer.uniform.object.ptlight.UPointLight;
import gameengine.engine.renderer.uniform.object.splight.SSSpotLight;
import gameengine.engine.renderer.uniform.object.splight.USpotLight;
import gameengine.logger.Logger;
import gameengine.util.FileUtils;

public class ScenePass extends ARenderPass<ScenePass> {
	public static final String VERTEX_SHADER = "shd-scene-vert";
	public static final String FRAGMENT_SHADER = "shd-scene-frag";
	
	public static final int MAX_POINT_LIGHT_COUNT = 5;
	public static final int MAX_SPOT_LIGHT_COUNT = 5;
	
	public final UAMatrix4f uProjection;
	public final UAMatrix4f uCamera;
	public final UAMatrix4f uObject;
	public final UAMatrix4f uBoneMatrices;
	public final UInteger1 uDiffuseSampler;
	public final UMaterial uMaterial;
	public final UAmbientLight uAmbientLight;
	public final UDirectionalLight uDirectionalLight;
	public final UArray<SSPointLight> uPointLights;
	public final UArray<SSSpotLight> uSpotLights;

    public ScenePass() {
    	super();
    	this.uProjection = new UAMatrix4f("uProjection");
    	this.uCamera = new UAMatrix4f("uCamera");
    	this.uObject = new UAMatrix4f("uObject");
    	this.uBoneMatrices = new UAMatrix4f("uBoneMatrices");
    	this.uDiffuseSampler = new UInteger1("uDiffuseSampler");
    	this.uMaterial = new UMaterial("uMaterial");
    	this.uAmbientLight = new UAmbientLight("uAmbientLight");
    	this.uDirectionalLight = new UDirectionalLight("uDirectionalLight");
    	this.uPointLights = new UArray<>(
			"uPointLights", new UPointLight[MAX_POINT_LIGHT_COUNT]
		);
    	this.uPointLights.fill(() -> new UPointLight());
    	this.uSpotLights = new UArray<>(
			"uSpotLights", new USpotLight[MAX_SPOT_LIGHT_COUNT]
		);
    	this.uSpotLights.fill(() -> new USpotLight());
    }
    
    
    @Override
    public void setup() {
    	Logger.info(this, "Scene render pass setup started...");
    	Shader sceneVertex = new Shader(VERTEX_SHADER, FileUtils.getResourcePath("shader/scene.vert"), GL46.GL_VERTEX_SHADER);
    	Shader sceneFragment = new Shader(FRAGMENT_SHADER, FileUtils.getResourcePath("shader/scene.frag"), GL46.GL_FRAGMENT_SHADER);
    	
    	this.shaderProgram = new ShaderProgram();
    	this.shaderProgram.addShader(sceneVertex);
    	this.shaderProgram.addShader(sceneFragment);
    	this.shaderProgram.generate();
    	Logger.info(this, "Shader program generated.");
    	
    	this.shaderProgram.declareUniform(
			this.uProjection,
			this.uCamera,
			this.uObject,
			this.uBoneMatrices,
			this.uDiffuseSampler,
			this.uMaterial,
			this.uAmbientLight,
			this.uDirectionalLight,
			this.uPointLights,
			this.uSpotLights
		);
    }

    @Override
    public void execute() {
    	Logger.spam(this, "Rendering scene...");
        this.shaderProgram.bind();
        this.uDiffuseSampler.update(0);
        
        int preRenderCount = 0;
        for( IRenderStrategy<ScenePass> renderer : this.preRender ) {
        	renderer.render(this);
        	preRenderCount++;
        }
        
        int renderCount = 0;
        for( IRenderStrategy<ScenePass> renderer : this.render ) {
        	renderer.render(this);
        	renderCount++;
        }
        
        GL46.glBindVertexArray(0);
        this.shaderProgram.unbind();
        Logger.spam(this, "Scene rendered. Pre: " + preRenderCount + ", main: " + renderCount + ".");
    }
    
    @Override
    public void dispose() {
        this.shaderProgram.dispose();
    }
}
