package com.salama.workflow.core.meta;


public class Constants {
//	private String[] _activityType = new String[] {
//			Activity.ActivityType_Application, Activity.ActivityType_SubFlow};
	
	private String[] _stateType = new String[] {
			//State.StateType_Start, 
			State.StateType_Normal, 
			State.StateType_Terminated, State.StateType_Completed};

	public String[] getStateType() {
		return _stateType;
	}

	public void setStateType(String[] stateType) {
		//this._stateType = stateType;
	}
	
}
