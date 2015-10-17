package com.salama.workflow.core.persistence;

public class StateTraceData {
	private long _workFlowId = 0;
	
	private long _parentFlowId = 0;
	
	private long _stateId = 0;

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

	public long getParentFlowId() {
		return _parentFlowId;
	}

	public void setParentFlowId(long parentFlowId) {
		_parentFlowId = parentFlowId;
	}
	
	
	
}
