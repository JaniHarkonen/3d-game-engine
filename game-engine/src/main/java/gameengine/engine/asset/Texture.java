package gameengine.engine.asset;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import gameengine.engine.renderer.component.TextureGL;
import gameengine.logger.Logger;

public class Texture implements ITexture, IAsset {
    private final String name;
    private String path;
    private Image image;
    private TextureGL graphics;

    public Texture(String name, String path) {
        this.name = name;
        this.path = path;
        this.image = null;
        this.graphics = null;
    }
    
    
	@Override
	public void load() {
		Logger.info(this, "Loading texture '" + this.name + "' from: ", this.path);
		String path = this.path;
		
        try( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer bufferWidth = stack.mallocInt(1);
            IntBuffer bufferHeight = stack.mallocInt(1);
            IntBuffer bufferComponents = stack.mallocInt(1);

            ByteBuffer bufferImage = STBImage.stbi_load(path, bufferWidth, bufferHeight, bufferComponents, 4);
            
            if( bufferImage == null ) {
            	System.out.println("Failed to load texture from path '" + path + "'!\n" + STBImage.stbi_failure_reason());
            	return;
            }
            
            this.image = new Image(bufferImage, bufferWidth.get(), bufferHeight.get());
            this.graphics = new TextureGL(this.image, GL46.GL_RGBA, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, true);
            this.graphics.generate();
            
            Logger.info(this, "Texture loaded.");
        }
	}
	
	@Override
	public void deload() {
		this.graphics.dispose();
		this.image.dispose();
    	this.image = null;
    	Logger.info(this, "Deloaded texture '" + this.name + "'.");
	}
    
	@Override
	public void bind() {
		this.graphics.bind();
	}
	
	@Override
	public void active(int index) {
		this.graphics.active(index);
	}
    
    @Override
    public String getName() {
    	return this.name;
    }
    
    @Override
    public String getPath() {
    	return this.path;
    }
    
    public Image getImage() {
    	return this.image;
    }
}
