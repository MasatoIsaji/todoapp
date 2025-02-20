package com.example.webapp.exception;

/**
 * 業務例外
 * 
 * Service内で業務例外が発生した場合、この例外をcontrollerにthrowする
 * 
 */
public class WebappException extends RuntimeException {
	public WebappException() {

	}

	public WebappException(String message) {
		super(message);
	}

	public WebappException(Throwable cause) {
		super(cause);
	}

	public WebappException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebappException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
