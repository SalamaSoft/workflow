package com.salama.workflow.core.config;

import java.io.Serializable;

public class PersistenceSetting implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6849415288423211699L;

	private ClassSetting _persistenceService = new ClassSetting();
	
	private String _configLocation = "";


	public ClassSetting getPersistenceService() {
		return _persistenceService;
	}

	public void setPersistenceService(ClassSetting persistenceService) {
		_persistenceService = persistenceService;
	}

	public String getConfigLocation() {
		return _configLocation;
	}

	public void setConfigLocation(String configLocation) {
		_configLocation = configLocation;
	}
	
	
}
