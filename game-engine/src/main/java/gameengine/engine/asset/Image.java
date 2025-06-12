package gameengine.engine.asset;

import java.nio.ByteBuffer;

import org.lwjgl.stb.STBImage;

public class Image {
	private ByteBuffer pixels;
	private int width;
    private int height;
    
    public Image(ByteBuffer pixels, int width, int height) {
    	this.pixels = pixels;
    	this.width = width;
    	this.height = height;
    }
    
    Image() {
    	this(null, -1, -1);
    }
    
    
    public void dispose() {
    	STBImage.stbi_image_free(this.pixels);
    }
    
    void setPixels(ByteBuffer pixels, int width, int height) {
    	this.pixels = pixels;
    	this.width = width;
    	this.height = height;
    }
    
    public ByteBuffer getPixels() {
    	return this.pixels;
    }
    
    public int getWidth() {
    	return this.width;
    }
    
    public int getHeight() {
    	return this.height;
    }
}
