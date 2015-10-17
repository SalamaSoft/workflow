package com.salama.workflow.core.config;

import java.io.Serializable;

public class BasePackage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7903862896597242642L;

	private String _basePackage = "";
	
	private String _description = "";
	
	/**
	 * Package name, such as java.util
	 * @return Package name
	 */
	public String getBasePackage() {
		return _basePackage;
	}
	
	public void setBasePackage(String basePackage) {
		_basePackage = basePackage;
	}
	
	/**
	 * Description about this package
	 * @return Description about this package
	 */
	public String getDescription() {
		return _description;
	}
	
	public void setDescription(String description) {
		_description = description;
	}
	
	
}
