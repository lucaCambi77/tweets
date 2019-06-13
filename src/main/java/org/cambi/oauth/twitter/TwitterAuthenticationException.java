package org.cambi.oauth.twitter;

public class TwitterAuthenticationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TwitterAuthenticationException() {
		super();
	}
	
	public TwitterAuthenticationException(final String message) {
		super(message);
	}
	
	public TwitterAuthenticationException(final String message, final Throwable t) {
		super(message, t);
	}
	
	public TwitterAuthenticationException(final Throwable t) {
		super(t);
	}
}
