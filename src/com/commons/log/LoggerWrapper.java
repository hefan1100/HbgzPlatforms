package com.commons.log;

import org.apache.log4j.Logger;

public class LoggerWrapper {

	private Logger _logger;
	private static final String defaultLogName = "com.hbtelecom.commons.Log";
	
	public LoggerWrapper(String name) {
		_logger = Logger.getLogger(name);
	}
	public void debug(String message, Object... args) {
		_logger.debug(args);
	}

	public void info(Object message) {
		_logger.info(message);
	}

	public void warn(Object message) {
		_logger.warn(message);
	}

	public void trace(Object message) {
		_logger.trace(message);
	}

	public void error(Object message) {
		_logger.error(message);
	}

	public boolean isDebugEnabled() {
		if (_logger == null)
			return false;
		return _logger.isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		if (_logger == null)
			return false;
		return _logger.isInfoEnabled();
	}

	public boolean isTraceEnabled() {
		if (_logger == null)
			return false;
		return _logger.isTraceEnabled();
	}
}
