package com.salama.workflow.core.persistence;

import java.util.Date;

public class StateInstanceInfo {
	private long _workFlowId = 0;
	
	private long _stateId = 0;
	
	private String _workFlowName = "";
	
	private String _stateName = "";
	
	private String _stateDescription = "";
	
	private String _stateDataClass = "";

	private Date _updateTime = new Date();
	
	private RoleUser _updateUser = new RoleUser();
	
	public long getWorkFlowId() {
		return _workFlowId;
	}

	public void setWorkFlowId(long workFlowId) {
		_workFlowId = workFlowId;
	}

	public long getStateId() {
		return _stateId;
	}

	public void setStateId(long stateId) {
		_stateId = stateId;
	}

	public String getWorkFlowName() {
		return _workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		_workFlowName = workFlowName;
	}

	public String getStateName() {
		return _stateName;
	}

	public void setStateName(String stateName) {
		_stateName = stateName;
	}

	public String getStateDataClass() {
		return _stateDataClass;
	}

	public void setStateDataClass(String stateDataClass) {
		_stateDataClass = stateDataClass;
	}

	public Date getUpdateTime() {
		return _updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		_updateTime = updateTime;
	}

	public RoleUser getUpdateUser() {
		return _updateUser;
	}

	public void setUpdateUser(RoleUser updateUser) {
		_updateUser = updateUser;
	}

	public String getStateDescription() {
		return _stateDescription;
	}

	public void setStateDescription(String stateDescription) {
		_stateDescription = stateDescription;
	}
	
}
