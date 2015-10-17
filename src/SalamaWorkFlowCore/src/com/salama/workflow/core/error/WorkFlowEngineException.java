package com.salama.workflow.core.error;

public class WorkFlowEngineException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8601997532790605030L;

	public WorkFlowEngineException() {
		super();
	}

	public WorkFlowEngineException(String msg) {
		super(msg);
	}
	
	public WorkFlowEngineException(Throwable e) {
		super(e);
	}
	
	public WorkFlowEngineException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
