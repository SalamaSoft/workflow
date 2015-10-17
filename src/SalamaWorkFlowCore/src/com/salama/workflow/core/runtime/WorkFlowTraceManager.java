package com.salama.workflow.core.runtime;

import java.util.LinkedList;

import com.salama.workflow.core.persistence.StateOperation;


public class WorkFlowTraceManager {
	
	private LinkedList<StateOperation> _stateOperationTrace = new LinkedList<StateOperation>();
	
	public WorkFlowTraceManager() {
		
	}
	
	public void addTrace(StateOperation stateOperation) {
		_stateOperationTrace.add(stateOperation);
	}

	public LinkedList<StateOperation> getStateTrace() {
		return _stateOperationTrace;
	}

	public void setStateTrace(LinkedList<StateOperation> stateOperationTrace) {
		_stateOperationTrace = stateOperationTrace;
	}

	public void clear() {
		this._stateOperationTrace.clear();
	}
}
