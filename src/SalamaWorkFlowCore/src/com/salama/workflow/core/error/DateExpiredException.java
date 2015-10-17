package com.salama.workflow.core.error;

public class DateExpiredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2736277119405303125L;

	public DateExpiredException() {
		super();
	}

	public DateExpiredException(String msg) {
		super(msg);
	}
	
	public DateExpiredException(Throwable e) {
		super(e);
	}
	
	public DateExpiredException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
