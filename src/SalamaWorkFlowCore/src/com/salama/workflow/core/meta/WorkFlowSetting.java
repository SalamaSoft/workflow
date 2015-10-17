package com.salama.workflow.core.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.util.Clonable;

public class WorkFlowSetting implements Clonable<WorkFlowSetting>{
	private Constants _constants = new Constants(); 
	
	private List<String> _allWorkFlowNames = new ArrayList<String>();

	@Override
	public WorkFlowSetting cloneData() {
		WorkFlowSetting data = new WorkFlowSetting();
		
		for(int i = 0; i < _allWorkFlowNames.size(); i++) {
			data._allWorkFlowNames.add(_allWorkFlowNames.get(i));
		}
		
		return data;
	}
	
	public Constants getConstants() {
		return _constants;
	}

	public void setConstants(Constants constants) {
		_constants = constants;
	}

	public List<String> getAllWorkFlowNames() {
		return _allWorkFlowNames;
	}

	public void setAllWorkFlowNames(List<String> allWorkFlowNames) {
		_allWorkFlowNames = allWorkFlowNames;
	}
	
}
