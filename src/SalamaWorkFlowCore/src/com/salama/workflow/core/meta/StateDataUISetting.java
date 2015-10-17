package com.salama.workflow.core.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.util.Clonable;

public class StateDataUISetting implements Clonable<StateDataUISetting> {
	private List<StateDataFieldUISetting> _fieldUISettings = new ArrayList<StateDataFieldUISetting>();

	@Override
	public StateDataUISetting cloneData() {
		StateDataUISetting setting = new StateDataUISetting();
		
		for(int i = 0; i < this._fieldUISettings.size(); i++) {
			setting._fieldUISettings.add(this._fieldUISettings.get(i).cloneData());
		}
		
		return setting;
	}
	
	public List<StateDataFieldUISetting> getFieldUISettings() {
		return _fieldUISettings;
	}

	public void setFieldUISettings(List<StateDataFieldUISetting> fieldUISettings) {
		_fieldUISettings = fieldUISettings;
	}
}
