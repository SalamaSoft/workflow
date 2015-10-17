package com.salama.workflow.core.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.util.Clonable;

public class SubFlow implements Clonable<SubFlow> {
	private String _workFlowName = "";
	
	private List<SetValue> _setParentStateValueToState = new ArrayList<SetValue>();

	private List<SetValue> _setStateValueToParentState = new ArrayList<SetValue>();

	@Override
	public SubFlow cloneData() {
		SubFlow data = new SubFlow();
    	data._workFlowName = this._workFlowName;

    	int i;
    	for(i = 0; i < this._setParentStateValueToState.size(); i++) {
    		data._setParentStateValueToState.add(this._setParentStateValueToState.get(i).cloneData());
    	}
    	
    	for(i = 0; i < this._setStateValueToParentState.size(); i++) {
    		data._setStateValueToParentState.add(this._setStateValueToParentState.get(i).cloneData());
    	}

    	return data;
	}
	
	public String getWorkFlowName() {
		return _workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		_workFlowName = workFlowName;
	}

	public List<SetValue> getSetParentStateValueToState() {
		return _setParentStateValueToState;
	}

	public void setSetParentStateValueToState(
			List<SetValue> setParentStateValueToState) {
		_setParentStateValueToState = setParentStateValueToState;
	}

	public List<SetValue> getSetStateValueToParentState() {
		return _setStateValueToParentState;
	}

	public void setSetStateValueToParentState(
			List<SetValue> setStateValueToParentState) {
		_setStateValueToParentState = setStateValueToParentState;
	}
	
	
}
