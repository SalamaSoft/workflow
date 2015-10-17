package com.salama.workflow.core.error;

public class XmlSerializeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7854041644190641756L;

	public XmlSerializeException() {
		super();
	}

	public XmlSerializeException(String msg) {
		super(msg);
	}
	
	public XmlSerializeException(Throwable e) {
		super(e);
	}
	
	public XmlSerializeException(String msg, Throwable e) {
		super(msg, e);
	}
}
