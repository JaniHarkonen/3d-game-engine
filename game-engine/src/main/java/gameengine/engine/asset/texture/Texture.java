package gameengine.engine.asset.texture;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;

import gameengine.engine.asset.IAsset;

public class Texture implements IAsset {
    private final String name;
    private int ID;
    private int width;
    private int height;
    private ByteBuffer image;

    public Texture(String name, int width, int height, ByteBuffer image) {
        this.name = name;
        this.ID = -1;
        this.width = width;
        this.height = height;
        this.image = image;
    }
    
    public void generate() {
        this.ID = GL46.glGenTextures();

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.ID);
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
    	GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.ID);
    }

    public void dispose() {
    	GL46.glDeleteTextures(this.ID);
    	STBImage.stbi_image_free(this.image);
    }
    
    @Override
    public String getName() {
    	return this.name;
    }
}
