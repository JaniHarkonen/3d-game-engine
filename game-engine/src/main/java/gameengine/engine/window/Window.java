package gameengine.engine.window;

import org.lwjgl.glfw.GLFW;

public class Window {
	
	public static final int DEFAULT_WIDTH = 640;
	public static final int DEFAULT_HEIGHT = 480;
	public static final String DEFAULT_TITLE = "3D game engine";
	public static final boolean DEFAULT_IS_RESIZEABLE = true;

	public static final long NULL_ID = -1;

    private long windowID;
    private String title;
    private int width;
    private int height;
    
    private boolean isResizeable;
    
    public Window() {
    	this.windowID = NULL_ID;
    	this.width = DEFAULT_WIDTH;
    	this.height = DEFAULT_HEIGHT;
    	this.title = DEFAULT_TITLE;
    	
    	this.isResizeable = DEFAULT_IS_RESIZEABLE;
    }
    
    public void setup() {
        if( !GLFW.glfwInit() ) {
            throw new IllegalStateException("Unable to initialize GLFW!");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, toGLFW(this.isResizeable));

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);

        this.windowID = GLFW.glfwCreateWindow(this.width, this.height, this.title, 0, 0);
        if( this.windowID == 0 ) {
            throw new RuntimeException("Failed to create the GLFW window!");
        }
        
        GLFW.glfwSetFramebufferSizeCallback(this.windowID, (win, w, h) -> this.onResize(w, h));

        //GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
         //   keyCallBack(key, action);
        //});

        GLFW.glfwMakeContextCurrent(this.windowID);
    	GLFW.glfwSwapInterval(0);
        GLFW.glfwShowWindow(this.windowID);
    }
    
    public void pollEvents() {
    	GLFW.glfwPollEvents();
    }
    
    public void update() {
    	GLFW.glfwSwapBuffers(this.windowID);
    }
    
    public void requestClose() {
    	GLFW.glfwSetWindowShouldClose(this.windowID, true);
    }
    /*
    public void keyCallBack(int key, int action) {
        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
        	GLFW.glfwSetWindowShouldClose(windowHandle, true); // We will detect this in the rendering loop
        }
    }
    */
    public void destroy() {
    	GLFW.glfwDestroyWindow(this.windowID);
    	GLFW.glfwTerminate();
    }
    
    public Window setDimensions(int width, int height) {
    	this.width = width;
    	this.height = height;
    	return this;
    }
    
    public long getID() {
        return this.windowID;
    }
    
    public Window setTitle(String title) {
    	this.title = title;
    	return this;
    }
    
    public int getWidth() {
    	return this.width;
    }
    
    public int getHeight() {
    	return this.height;
    }
    
    public String getTitle() {
    	return this.title;
    }
    
    public boolean isClosing() {
        return GLFW.glfwWindowShouldClose(this.windowID);
    }
    
    /*
    public boolean isKeyPressed(int keyCode) {
        return GLFW.glfwGetKey(windowHandle, keyCode) == GLFW.GLFW_PRESS;
    }
*/

    
    private void onResize(int width, int height) {
    	this.width = width;
    	this.height = height;
    }
    
    private int toGLFW(boolean bool) {
    	return bool ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE;
    }
}
