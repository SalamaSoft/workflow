package com.salama.workflow.core.error;

public class AccessNoAuthorizationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4226022089888611174L;

	public AccessNoAuthorizationException() {
		super();
	}

	public AccessNoAuthorizationException(String msg) {
		super(msg);
	}
	
	public AccessNoAuthorizationException(Throwable e) {
		super(e);
	}
	
	public AccessNoAuthorizationException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
