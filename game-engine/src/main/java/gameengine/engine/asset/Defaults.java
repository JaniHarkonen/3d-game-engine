package gameengine.engine.asset;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL46;

import gameengine.engine.renderer.component.Submesh;
import gameengine.engine.renderer.component.TextureGL;
import gameengine.logger.Logger;
import gameengine.util.GeometryShapeUtils;
import gameengine.util.StringUtils;

public final class Defaults {

	public static final Submesh PLANE;
	public static final Submesh BOX;
	public static final Submesh SPHERE;
	public static final Submesh CAPSULE;
	public static final Submesh CYLINDER;
	
	public static final Submesh SUBMESH;
	public static final Image IMAGE;
	public static final TextureGL TEXTURE_GL;
	
	private static boolean isInitialized;
	
	static {
		isInitialized = false;
		
		PLANE = new Submesh();
		BOX = new Submesh();
		SPHERE = new Submesh();
		CAPSULE = new Submesh();
		CYLINDER = new Submesh();
		
		SUBMESH = BOX;
		IMAGE = new Image();
		TEXTURE_GL = new TextureGL(IMAGE, GL46.GL_RGBA, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, true);
	}


	public static void init() {
		if( isInitialized ) {
			Logger.error("Defaults.init()", "Trying to initialize default assets after they've already been initialized!");
			return;
		}
		
		Logger.info("Defaults.init()", "Generating default assets...");
		
		initMeshes();
		initTextures();
		
		isInitialized = true;
		Logger.info("Defaults.init()", "Default assets generated.");
	}
	
	private static void initTextures() {
		int width = 16;
		int height = 16;
		
		ByteBuffer pixels = StringUtils.stringToPixels(
			"0000000000000000" + 
	        "0000000000000000" + 
	        "0000000000000000" + 
	        "0111110000111110" + 
	        "0001000000001000" + 
	        "0001000000001000" + 
	        "0001000000001000" + 
	        "0001000000001000" + 
	        "0001000000001000" + 
	        "0001000000001000" + 
	        "0001000000001000" + 
	        "0001000000001000" + 
	        "0000011111100000" + 
	        "0000000000000000" + 
	        "0000000000000000" + 
	        "0000000000000000", 
			width, height, 4
		);
		IMAGE.setPixels(pixels, width, height);
		TEXTURE_GL.generate();
	}
	
	private static void initMeshes() {
		float r = 1;
		int resolution = 10;
		
			// This also populates SUBMESH, because SUBMESH = PLANE
		GeometryShapeUtils.populatePlane(PLANE, r);
		GeometryShapeUtils.populateSphere(SPHERE, resolution, r);
		GeometryShapeUtils.populateBox(BOX, r, r, r);
		GeometryShapeUtils.populateCylinder(CYLINDER, resolution, r, r);
		GeometryShapeUtils.populateCapsule(CAPSULE, resolution, r, r * 2);
	}
}
