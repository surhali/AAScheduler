package com.jpmc.am.scheduler.messaging.exceptions;

/**
 * An exception class which is invoked when the gateway receives further terminated messages.
 * @author Suryasatish
 *
 */
public class TerminationException extends Exception {

	
	private static final long serialVersionUID = -32456678789989L;
	private static final String ERROR_MESSAGE = "A terminated message is received.";
	
	public TerminationException(String message) {
		super(ERROR_MESSAGE + " " + message);
	}
	
	public TerminationException() {
		super(ERROR_MESSAGE);
	}
}

