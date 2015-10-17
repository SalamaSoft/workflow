package com.salama.workflow.core.meta;

import com.salama.workflow.core.util.Clonable;

public class AutoEventTrigger implements Clonable<AutoEventTrigger> {
	private String _activityName = "";
	
	private Condition _condition = new Condition();

	@Override
	public AutoEventTrigger cloneData() {
		AutoEventTrigger data = new AutoEventTrigger();
		
		data._activityName = this._activityName;
		data._condition = this._condition.cloneData();
		
		return data;
	}
	
	public Condition getCondition() {
		return _condition;
	}

	public void setCondition(Condition condition) {
		_condition = condition;
	}

	public String getActivityName() {
		return _activityName;
	}

	public void setActivityName(String activityName) {
		_activityName = activityName;
	}
	
	
}
