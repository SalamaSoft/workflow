package com.salama.workflow.core.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.util.Clonable;

public class StateDataFieldUISetting implements Clonable<StateDataFieldUISetting>{
	private String _fieldNameExpression = "";
	
	private List<UISetting> _uiSettings = new ArrayList<UISetting>();

	@Override
	public StateDataFieldUISetting cloneData() {
		StateDataFieldUISetting setting = new StateDataFieldUISetting();
		setting._fieldNameExpression = this._fieldNameExpression;
		
		for(int i = 0; i < _uiSettings.size(); i++) {
			setting._uiSettings.add(_uiSettings.get(i).cloneData());
		}
		
		return setting;
	}
	
	public String getFieldNameExpression() {
		return _fieldNameExpression;
	}

	public void setFieldNameExpression(String fieldNameExpression) {
		_fieldNameExpression = fieldNameExpression;
	}

	public List<UISetting> getUiSettings() {
		return _uiSettings;
	}

	public void setUiSettings(List<UISetting> uiSettings) {
		_uiSettings = uiSettings;
	}
	
}
