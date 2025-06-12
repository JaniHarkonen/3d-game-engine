package gameengine;

import gameengine.engine.Engine;
import gameengine.logger.ConsoleRecorder;
import gameengine.logger.Logger;

public class Main {

	public static void main(String[] args) {
		Logger.init(
			Logger.LOG_TIMESTAMP | 
			Logger.LOG_SYSTEM | 
			Logger.LOG_CALLER | 
			Logger.LOG_SEVERITY, 
			Logger.WARN
		);
		
		Logger.registerTarget(new ConsoleRecorder());
		Engine.init();
		Engine.setTickRate(60f);
		Engine.start();
		System.out.println("MAIN EXITED!");
	}
}
