package gameengine.engine.asset.texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import gameengine.engine.asset.ILoadTask;
/*
public class TextureLoadTask implements ILoadTask<Texture> {
	
	private String name;
	private String path;
	private Texture loadedAsset;
	
	public TextureLoadTask(String name, String path) {
		this.name = name;
		this.path = path;
		this.loadedAsset = null;
	}

	
	@Override
	public Texture load() {
		Texture texture = null;
		String path = this.path;
		
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer bufferWidth = stack.mallocInt(1);
            IntBuffer bufferHeight = stack.mallocInt(1);
            IntBuffer bufferComponents = stack.mallocInt(1);

            ByteBuffer bufferImage = STBImage.stbi_load(path, bufferWidth, bufferHeight, bufferComponents, 4);
            
            if( bufferImage == null ) {
            	System.out.println("Failed to load texture from path '" + path + "'!\n" + STBImage.stbi_failure_reason());
            	return null;
            }
            
            texture = new Texture(this.name, bufferWidth.get(), bufferHeight.get(), bufferImage);
            texture.generate();
        }
        
        this.loadedAsset = texture;
		return texture;
	}
	
	@Override
	public void deload() {
		this.loadedAsset.dispose();
		this.loadedAsset = null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getPath() {
		return this.path;
	}
	
	@Override
	public Texture getLoadedAsset() {
		return this.loadedAsset;
	}
}
*/