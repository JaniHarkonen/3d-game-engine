package gameengine.engine.window;

import org.lwjgl.glfw.GLFW;

public class Window {
	
	public static final int DEFAULT_WIDTH = 640;
	public static final int DEFAULT_HEIGHT = 480;
	public static final String DEFAULT_TITLE = "3D game engine";
	public static final int DEFAULT_FPS_MAX = 60;
	public static final boolean DEFAULT_IS_RESIZEABLE = true;

	public static final long NULL_ID = -1;

    private long windowID;
    private String title;
    private int width;
    private int height;
    private Input input;
    
    private boolean isResizeable;
    
    public Window() {
    	this.windowID = NULL_ID;
    	this.width = DEFAULT_WIDTH;
    	this.height = DEFAULT_HEIGHT;
    	this.title = DEFAULT_TITLE;
    	this.input = null;
    	
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
        
        this.input = new Input();
        this.input.bind(this);

        GLFW.glfwMakeContextCurrent(this.windowID);
    	GLFW.glfwSwapInterval(0);
        GLFW.glfwShowWindow(this.windowID);
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
    
    public void destroy() {
    	GLFW.glfwDestroyWindow(this.windowID);
    	GLFW.glfwTerminate();
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

    private void onResize(int width, int height) {
    	this.width = width;
    	this.height = height;
    }
    
    private int toGLFW(boolean bool) {
    	return bool ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE;
    }
}
