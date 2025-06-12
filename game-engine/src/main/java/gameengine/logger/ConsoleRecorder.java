package gameengine.logger;

import java.util.List;

public class ConsoleRecorder implements ILogRecorder {
	@Override
	public void log(int logFlags, LoggerMessage loggerMessage) {
		List<Object> messages = loggerMessage.getMessages();
		Object message = messages.get(0);
		String messageString = messages.get(0) == null ? message + "" : message.toString();
		
		for( int i = 1; i < messages.size(); i++ ) {
			message = messages.get(i);
			messageString += "\n" + (message == null ? message + "" : message.toString());
		}
		
		String signTimestamp = Logger.createSignifierIfLogged(
			Logger.LOG_TIMESTAMP, loggerMessage.getTimestamp()
		);
		String signSystem = Logger.createSignifierIfLogged(
			Logger.LOG_SYSTEM, loggerMessage.getSystem()
		);
		String signCaller = Logger.createSignifierIfLogged(
			Logger.LOG_CALLER, loggerMessage.getCaller().toString()
		);
		String signSeverity = Logger.createSignifierIfLogged(
			Logger.LOG_SEVERITY, Logger.getSeverityLabel(loggerMessage.getSeverity())
		);
		
		System.out.println(
			signTimestamp + signSystem + signCaller + signSeverity + ": " + 
			((messages.size() > 1) ? "\n" : "") + messageString
		);
	}
}
