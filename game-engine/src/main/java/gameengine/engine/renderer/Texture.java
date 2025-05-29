package gameengine.engine.renderer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Texture {
	private int textureId;
    private String texturePath;

    public Texture(int width, int height, ByteBuffer buf) {
        this.texturePath = "";
        generateTexture(width, height, buf);
    }

    public Texture(String texturePath) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            this.texturePath = texturePath;
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer buf = STBImage.stbi_load(texturePath, w, h, channels, 4);
            
            if (buf == null) {
                throw new RuntimeException("Image file [" + texturePath + "] not loaded: " + STBImage.stbi_failure_reason());
            }

            int width = w.get();
            int height = h.get();

            generateTexture(width, height, buf);

            STBImage.stbi_image_free(buf);
        }
    }

    public void bind() {
    	GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureId);
    }

    public void cleanup() {
    	GL46.glDeleteTextures(textureId);
    }

    private void generateTexture(int width, int height, ByteBuffer buf) {
        textureId = GL46.glGenTextures();

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureId);
        GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR);
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0,
        		GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buf);
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
    }

    public String getTexturePath() {
        return texturePath;
    }
}
