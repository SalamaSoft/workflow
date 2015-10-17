package com.salama.workflow.core.persistence;

import java.io.Serializable;

import com.salama.workflow.core.runtime.StateEventResult;

public class StateOperation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8839378637643245558L;

	private String _eventName = "";
	
//	private String _eventDescription = "";

	private String _activityName = "";

	private String _activityDescription = "";
	
	private StateEventResult _eventResult = new StateEventResult();

	public String getEventName() {
		return _eventName;
	}

	public void setEventName(String eventName) {
		_eventName = eventName;
	}

	public String getActivityName() {
		return _activityName;
	}

	public void setActivityName(String activityName) {
		_activityName = activityName;
	}

	public StateEventResult getEventResult() {
		return _eventResult;
	}

	public void setEventResult(StateEventResult eventResult) {
		_eventResult = eventResult;
	}

//	public String getEventDescription() {
//		return _eventDescription;
//	}
//
//	public void setEventDescription(String eventDescription) {
//		_eventDescription = eventDescription;
//	}

	public String getActivityDescription() {
		return _activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		_activityDescription = activityDescription;
	}
	
}
