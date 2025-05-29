package gameengine;

import gameengine.engine.Engine;

public class Main {

	public static void main(String[] args) {
		Engine engine = Engine.createInstance();
		engine.setTickRate(60f);
		engine.start();
		System.out.println("MAIN EXITED!");
	}
}
