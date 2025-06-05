package gameengine.engine.asset;

import java.nio.ByteBuffer;

import org.lwjgl.stb.STBImage;

public class Image {
	private ByteBuffer buffer;
	private int width;
    private int height;
    
    public Image(ByteBuffer buffer, int width, int height) {
    	this.buffer = buffer;
    	this.width = width;
    	this.height = height;
    }
    
    
    public void dispose() {
    	STBImage.stbi_image_free(this.buffer);
    }
    
    public ByteBuffer getBuffer() {
    	return this.buffer;
    }
    
    public int getWidth() {
    	return this.width;
    }
    
    public int getHeight() {
    	return this.height;
    }
}
