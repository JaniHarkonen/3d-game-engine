package gameengine.logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class Logger {
	public static final int MUTED = 0;
	public static final int DEBUG = MUTED;
	public static final int FATAL = 1;
	public static final int ERROR = 2;
	public static final int WARN  = 3;
	public static final int INFO  = 4;
	public static final int SPAM = 5;
	
	public static final int LOG_TIMESTAMP = 1;
	public static final int LOG_SYSTEM = 2;
	public static final int LOG_CALLER = 4;
	public static final int LOG_SEVERITY = 8;
	
	private static final String FATAL_ERROR_MESSAGE = "FATAL ERROR OCCURRED! SEE LOGS!";
	
	private static Logger instance;
	
	public static void init(int logFlags, int verbosity) {
		if( instance == null ) {
			instance = new Logger(logFlags, verbosity);
		}
	}
	
	
	private final Map<Integer, String> severityLabels;
	
	private int verbosity;
	private int logFlags;
	private List<ILogRecorder> recorders;
	private String system;
	private Set<String> mutedSystems;
	
	private Logger(int logFlags, int verbosity) {
		this.verbosity = verbosity;
		this.logFlags = logFlags;
		this.recorders = new ArrayList<>();
		this.severityLabels = new HashMap<>();
		this.system = "MAIN";
		this.mutedSystems = new HashSet<>();
		
		this.severityLabels.put(MUTED, "");
		this.severityLabels.put(DEBUG, "Debug");
		this.severityLabels.put(FATAL, "FATAL ERROR");
		this.severityLabels.put(ERROR, "ERROR");
		this.severityLabels.put(WARN, "Warning");
		this.severityLabels.put(INFO, "Info");
	}
	
	public static void registerTarget(ILogRecorder target) {
		instance.recorders.add(target);
	}
	
	private static LoggerMessage createMessage(int severity, Object me) {
		if( instance.verbosity < severity || instance.mutedSystems.contains(instance.system) ) {
			return null;
		}
		
		return new LoggerMessage(instance.system, severity, me);
	}
	
	public static void log(int severity, Object me, Function<LoggerMessage, Boolean> batcher) {
		LoggerMessage loggerMessage = createMessage(severity, me);
		
		if( loggerMessage == null ) {
			return;
		}
		
		boolean isLogged;
		loggerMessage.timestamp();
		isLogged = batcher.apply(loggerMessage);
		
		if( !isLogged || loggerMessage.getMessages().size() == 0 ) {
			return;
		}
		
		loggerMessage.timestamp();
		log(loggerMessage);
	}
	
	public static void log(int severity, Object me, Object... messages) {
		LoggerMessage loggerMessage = createMessage(severity, me);
		
		if( loggerMessage == null ) {
			return;
		}
		
		for( Object message : messages ) {
			loggerMessage.addMessage(message);
		}
		
		loggerMessage.refreshTimestamp();
		log(loggerMessage);
	}
	
	private static void log(LoggerMessage message) {
		for( ILogRecorder recorder : instance.recorders ) {
			recorder.log(instance.logFlags, message);
		}
	}
	
	public static void spam(Object me, Function<LoggerMessage, Boolean> batcher) {
		log(SPAM, me, batcher);
	}
	
	public static void spam(Object me, Object... messages) {
		log(SPAM, me, messages);
	}
	
	public static void info(Object me, Function<LoggerMessage, Boolean> batcher) {
		log(INFO, me, batcher);
	}
	
	public static void info(Object me, Object... messages) {
		log(INFO, me, messages);
	}
	
	public static void warn(Object me, Function<LoggerMessage, Boolean> batcher) {
		log(WARN, me, batcher);
	}
	
	public static void warn(Object me, Object... messages) {
		log(WARN, me, messages);
	}
	
	public static void error(Object me, Function<LoggerMessage, Boolean> batcher) {
		log(ERROR, me, batcher);
	}
	
	public static void error(Object me, Object... messages) {
		log(ERROR, me, messages);
	}
	
	public static void fatal(Object me, Function<LoggerMessage, Boolean> batcher) {
		log(FATAL, me, batcher);
		throw new RuntimeException(FATAL_ERROR_MESSAGE);
	}
	
	public static void fatal(Object me, Object... messages) {
		log(FATAL, me, messages);
		throw new RuntimeException(FATAL_ERROR_MESSAGE);
	}
	
	public static void debug(Object me, Function<LoggerMessage, Boolean> batcher) {
		log(DEBUG, me, batcher);
	}
	
	public static void debug(Object me, Object... messages) {
		log(DEBUG, me, messages);
	}
	
	public static void muteSystem(String system) {
		instance.mutedSystems.add(system);
	}
	
	public static void unmuteSystem(String system) {
		instance.mutedSystems.remove(system);
	}
	
	public static String createSignifierIfLogged(int signifierFlag, String signifier) {
		return ((instance.logFlags & signifierFlag) == signifierFlag) ? "[" + signifier + "]" : "";
	}
	
	public static String formatDateTimeString(LocalDateTime dateTime) {
		return (
			dateTime.getHour() + ":" + 
			dateTime.getMinute() + ":" + 
			dateTime.getSecond() + "." + 
			dateTime.getNano() / 1000000
		);
	}
	
	public static void setSystem(String system) {
		instance.system = system;
	}
	
	public static String getSeverityLabel(int severity) {
		return instance.severityLabels.get(severity);
	}
	
	public static int getVerbosity() {
		return instance.verbosity;
	}
	
	public static boolean doesLog(int logFlags) {
		return (instance.logFlags & logFlags) == logFlags;
	}
}
