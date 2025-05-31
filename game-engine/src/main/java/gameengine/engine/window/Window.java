package gameengine.engine.window;

import org.lwjgl.glfw.GLFW;

import gameengine.logger.Logger;

public class Window {
	
	public static final int DEFAULT_WIDTH = 640;
	public static final int DEFAULT_HEIGHT = 480;
	public static final String DEFAULT_TITLE = "3D game engine";
	public static final int DEFAULT_FPS_MAX = 60;
	public static final boolean DEFAULT_IS_RESIZEABLE = true;
	public static final boolean DEFAULT_IS_CURSOR_ENABLED = true;

	public static final long NULL_ID = -1;

    private long windowID;
    private String title;
    private int width;
    private int height;
    private Input input;
    
    private boolean isResizeable;
    private boolean isCursorEnabled;
    
    public Window() {
    	this.windowID = NULL_ID;
    	this.width = DEFAULT_WIDTH;
    	this.height = DEFAULT_HEIGHT;
    	this.title = DEFAULT_TITLE;
    	this.input = null;
    	
    	this.isResizeable = DEFAULT_IS_RESIZEABLE;
    	this.isCursorEnabled = DEFAULT_IS_CURSOR_ENABLED; 
    }
    
    public void setup() {
        if( !GLFW.glfwInit() ) {
        	Logger.fatal(this, "GLFW initialization failed!");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, toGLFW(this.isResizeable));

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);

        this.windowID = GLFW.glfwCreateWindow(this.width, this.height, this.title, 0, 0);
        
        if( this.windowID == 0 ) {
        	Logger.error(this, "Failed to create window!");
        	return;
        }
        
        Logger.info(this, "Window created.");
        
        int cursor = this.isCursorEnabled ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_DISABLED;
        GLFW.glfwSetInputMode(this.windowID, GLFW.GLFW_CURSOR, cursor);
        GLFW.glfwSetFramebufferSizeCallback(this.windowID, (win, w, h) -> this.onResize(w, h));
        
        this.input = new Input();
        this.input.bind(this);

        GLFW.glfwMakeContextCurrent(this.windowID);
    	GLFW.glfwSwapInterval(0);
        GLFW.glfwShowWindow(this.windowID);
        
        Logger.info(this, "Window setup done.");
    }
    
    public void pollEvents() {
    	GLFW.glfwPollEvents();
    	this.input.poll();
    }
    
    public void update(float deltaTime) {
    	this.input.clear(); // Clear the input event queue
		GLFW.glfwSwapBuffers(this.windowID);
    }
    
    public void requestClose() {
    	GLFW.glfwSetWindowShouldClose(this.windowID, true);
    }
    
    public void dispose() {
    	GLFW.glfwDestroyWindow(this.windowID);
    	GLFW.glfwTerminate();
    	Logger.info(this, "GLFW window disposed and terminated.");
    }
    
    Window setDimensions(int width, int height) {
    	this.width = width;
    	this.height = height;
    	return this;
    }
    
    Window setTitle(String title) {
    	this.title = title;
    	return this;
    }
    
    public Window changeTitle(String title) {
    	this.title = title;
    	GLFW.glfwSetWindowTitle(this.windowID, title);
    	return this;
    }
    
    public Window enableCursor(boolean isEnabled) {
    	this.isCursorEnabled = isEnabled;
    	GLFW.glfwSetInputMode(this.windowID, GLFW.GLFW_CURSOR, this.isCursorEnabled ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_DISABLED);
    	return this;
    }
    
    public long getID() {
        return this.windowID;
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
    
    public Input getInput() {
    	return this.input;
    }
    
    public boolean isCursorEnabled() {
    	return this.isCursorEnabled;
    }

    private void onResize(int width, int height) {
    	this.width = width;
    	this.height = height;
    }
    
    private int toGLFW(boolean bool) {
    	return bool ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE;
    }
}
