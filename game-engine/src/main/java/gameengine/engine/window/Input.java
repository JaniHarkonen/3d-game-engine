package gameengine.engine.window;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

public class Input {
	
	public class Event {
		public int device;
		public int type;
		public int button;
		public double mouseX;
		public double mouseY;
		public double mouseDeltaX;
		public double mouseDeltaY;
		
		@Override
		public int hashCode() {
			return this.device + this.type + this.button;
		}
	}
	
	
	public static final int DEFAULT_EVENT_QUEUE_MAX_LENGTH = 30;
	
	public static final int DEVICE_KEYBOARD = 10000;
	public static final int DEVICE_MOUSE = 20000;
	
	public static final int EVENT_NONE = 0;
	public static final int EVENT_PRESS = 1000;
	public static final int EVENT_HOLD = 2000;
	public static final int EVENT_RELEASE = 3000;
	public static final int EVENT_MOVE = 4000;
	
	private static final int KEYBOARD_BUTTON = 50;
	private static final int MOUSE_BUTTON = 0;

	private Set<Integer> heldKeys;
	private Set<Integer> heldMouseButtons;
	private double mouseX;
	private double mouseY;
	private double mouseDeltaX;
	private double mouseDeltaY;
	private String keyboardString;
	
	private List<Event> eventQueue;
	private int eventQueueMaxLength;
	
	Input() {
		this.heldKeys = new LinkedHashSet<>();
		this.heldMouseButtons = new LinkedHashSet<>();
		this.mouseX = 0;
		this.mouseY = 0;
		this.mouseDeltaX = 0;
		this.mouseDeltaY = 0;
		this.keyboardString = "";
		
		this.eventQueue = new ArrayList<>();
		this.eventQueueMaxLength = DEFAULT_EVENT_QUEUE_MAX_LENGTH;
	}
	
	
	void bind(Window window) {
		long windowID = window.getID();
		
		GLFW.glfwSetKeyCallback(windowID, (win, key, scancode, action, mods) -> {
			this.keyInput(key, action);
		});
		
		GLFW.glfwSetMouseButtonCallback(windowID, (win, button, action, mods) -> {
			this.mouseInput(button, action);
		});
		
		GLFW.glfwSetCursorPosCallback(windowID, (win, mouseX, mouseY) -> {
			this.mousePosition(mouseX, mouseY);
		});
	}
	
	void poll(/*Controller controller*/) {
		for( Integer key : this.heldKeys ) {
			Event event = new Event();
			event.device = DEVICE_KEYBOARD;
			event.type = EVENT_HOLD;
			event.button = key;
			
			this.eventQueue.add(event);
		}
		
		for( Integer button : this.heldMouseButtons ) {
			Event event = new Event();
			event.device = DEVICE_MOUSE;
			event.type = EVENT_HOLD;
			event.button = button ;
			
			this.eventQueue.add(event);
		}
		
		for( Event e : this.eventQueue ) {
			System.out.println(e.hashCode());
		}
	}
	
	void clear() {
		this.eventQueue.clear();
	}
	
	public void DEBUGmapInput(int keyhash, Consumer<Event> action) {
		for( Event e : this.eventQueue ) {
			if( e.hashCode() == keyhash ) {
				action.accept(e);
			}
		}
	}
	
	public String getKeyboardString() {
		return this.keyboardString;
	}
	
	private void keyInput(int key, int action) {
		this.input(DEVICE_KEYBOARD, this.key(key), action, this.heldKeys);
	}
	
	private void mouseInput(int button, int action) {
		this.input(DEVICE_MOUSE, this.mouseButton(button), action, this.heldMouseButtons);
	}
	
	private void mousePosition(double x, double y) {
		Event event = new Event();
		event.device = DEVICE_MOUSE;
		event.type = EVENT_MOVE;
		event.button = 0;
		
		this.mouseDeltaX = x - this.mouseX;
		this.mouseDeltaY = y - this.mouseY;
		this.mouseX = x;
		this.mouseY = y;
		
		this.queueEvent(event);
	}
	
	private void input(int device, int key, int action, Set<Integer> heldMap) {
		int eventType = EVENT_NONE;
		
		if( action == GLFW.GLFW_RELEASE ) {
			
				// Since the key must have been pressed before being released,
				// emit at least one "hold" event
			if( heldMap.remove(key) ) {
				Event event = new Event();
				event.device = device;
				event.type = EVENT_HOLD;
				event.button = key;
				
				this.eventQueue.add(event);
			}
			
			eventType = EVENT_RELEASE;
		} else if( action == GLFW.GLFW_PRESS ) {
			heldMap.add(key);
			eventType = EVENT_PRESS;
		} else {
			return;
		}
		
		Event event = new Event();
		event.device = device;
		event.type = eventType;
		event.button = key;
		
		this.queueEvent(event);
	}
	
	private void queueEvent(Event event) {
		while( this.eventQueue.size() >= this.eventQueueMaxLength ) {
			this.eventQueue.remove(0);
		}
		
		event.mouseX = this.mouseX;
		event.mouseY = this.mouseY;
		event.mouseDeltaX = this.mouseDeltaX;
		event.mouseDeltaY = this.mouseDeltaY;
		this.eventQueue.add(event);
	}
	
	private int key(int key) {
		return KEYBOARD_BUTTON + key;
	}
	
	private int mouseButton(int button) {
		return MOUSE_BUTTON + button;
	}
}
