package gameengine.engine.renderer.component;

import org.lwjgl.opengl.GL46;

import gameengine.engine.asset.ITexture;
import gameengine.engine.asset.Image;
import gameengine.logger.Logger;

public class TextureGL implements ITexture {
	
	private int ID;
	private int pixelFormat;
	private int internalFormat;
	private int type;
	private boolean hasMipmap;
	private Image image;
	
	public TextureGL(Image image, int internalFormat, int pixelFormat, int type, boolean hasMipmap) {
		this.ID = -1;
		this.internalFormat = internalFormat;
		this.pixelFormat = pixelFormat;
		this.type = type;
		this.hasMipmap = hasMipmap;
		this.image = image;
	}
	
	
    public void generate() {
    	Logger.info(this, "Generating texture graphics...");
        this.ID = GL46.glGenTextures();
        this.bind();
        GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR);
        GL46.glTexImage2D(
    		GL46.GL_TEXTURE_2D, 
    		0, 
    		this.internalFormat, 
    		this.image.getWidth(),
    		this.image.getHeight(),
    		0, 
    		this.pixelFormat, 
    		this.type, 
    		this.image.getBuffer()
		);
        
        if( this.hasMipmap ) {
        	GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
        }
        
        Logger.info(this, "Texture graphics generated.");
    }

    @Override
    public void bind() {
    	GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.ID);
    }
    
    @Override
	public void active(int index) {
    	GL46.glActiveTexture(index);
    	this.bind();
	}
    
    public void dispose() {
    	GL46.glDeleteTextures(this.ID);
    	Logger.info(this, "Texture graphics disposed.");
    	this.ID = -1;
    }
    
    public int getID() {
    	return this.ID;
    }
}
