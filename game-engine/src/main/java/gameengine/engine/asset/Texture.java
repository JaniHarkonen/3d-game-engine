package gameengine.engine.asset;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Texture implements IAsset {
    private final String name;
    private String path;
    private int graphicsID;
    private int width;
    private int height;
    private ByteBuffer image;

    public Texture(String name, String path) {
        this.name = name;
        this.path = path;
        this.graphicsID = -1;
        this.width = -1;
        this.height = -1;
        this.image = null;
    }
    
    
	@Override
	public void load() {
		String path = this.path;
		
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer bufferWidth = stack.mallocInt(1);
            IntBuffer bufferHeight = stack.mallocInt(1);
            IntBuffer bufferComponents = stack.mallocInt(1);

            ByteBuffer bufferImage = STBImage.stbi_load(path, bufferWidth, bufferHeight, bufferComponents, 4);
            
            if( bufferImage == null ) {
            	System.out.println("Failed to load texture from path '" + path + "'!\n" + STBImage.stbi_failure_reason());
            	return;
            }
            
            this.width = bufferWidth.get();
            this.height = bufferHeight.get();
            this.image = bufferImage;
            this.generate();
        }
	}
	
	@Override
	public void deload() {
		GL46.glDeleteTextures(this.graphicsID);
    	STBImage.stbi_image_free(this.image);
    	this.image = null;
    	this.graphicsID = -1;
	}
    
    private void generate() {
        this.graphicsID = GL46.glGenTextures();

        this.bind();
        GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR);
        GL46.glTexImage2D(
    		GL46.GL_TEXTURE_2D, 
    		0, 
    		GL46.GL_RGBA, 
    		this.width, 
    		this.height, 
    		0, 
    		GL46.GL_RGBA, 
    		GL46.GL_UNSIGNED_BYTE, 
    		this.image
		);
        
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
    }

    public void bind() {
    	GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.graphicsID);
    }
    
    @Override
    public String getName() {
    	return this.name;
    }
}
