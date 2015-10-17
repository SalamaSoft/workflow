package com.salama.workflow.core.error;

public class PersistenceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4387260068596916651L;

	public PersistenceException() {
		super();
	}

	public PersistenceException(String msg) {
		super(msg);
	}
	
	public PersistenceException(Throwable e) {
		super(e);
	}
	
	public PersistenceException(String msg, Throwable e) {
		super(msg, e);
	}

}
