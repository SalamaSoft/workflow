package com.salama.workflow.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkFlowEngineContext implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9116784604192775781L;

	private List<BasePackage> _dataScan = new ArrayList<BasePackage>();
	
	private List<BasePackage> _applicationScan = new ArrayList<BasePackage>();
	
	private PersistenceSetting _persistenceSetting = new PersistenceSetting();

	public List<BasePackage> getDataScan() {
		return _dataScan;
	}

	public void setDataScan(List<BasePackage> dataScan) {
		_dataScan = dataScan;
	}

	public List<BasePackage> getApplicationScan() {
		return _applicationScan;
	}

	public void setApplicationScan(List<BasePackage> applicationScan) {
		_applicationScan = applicationScan;
	}

	public PersistenceSetting getPersistenceSetting() {
		return _persistenceSetting;
	}

	public void setPersistenceSetting(PersistenceSetting persistenceSetting) {
		_persistenceSetting = persistenceSetting;
	}
	
}
