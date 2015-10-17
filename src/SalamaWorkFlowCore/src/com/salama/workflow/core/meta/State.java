package com.salama.workflow.core.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.util.Clonable;

public class State implements Clonable<State>
{
//    public static final String StateType_Start = "start";
    public static final String StateType_Normal = "normal";
    public static final String StateType_Terminated = "terminated";
    public static final String StateType_Completed = "completed";

    private String _name = "";

    private String _description = "";

    private String _stateType = StateType_Normal;

    /**
     * Return value of initiateActivity will be set to the stateData.
     */
    private Activity _initiateActivity = new Activity();

    private RelevantData _stateData = new RelevantData();
    
    private StateUserDefineSetting _stateUserDefineSetting = new StateUserDefineSetting();
    
    private StateDataUISetting _stateDataUISetting = new StateDataUISetting();

    private List<Role> _accessibleRoles = new ArrayList<Role>();

    //private List<AutoEventTrigger> _autoEventTriggers = new ArrayList<AutoEventTrigger>();
    
    private List<Event> _activityEvents = new ArrayList<Event>();
    
    private List<Activity> _activities = new ArrayList<Activity>();

    @Override
    public State cloneData() {
    	State data = new State();
    	
    	data._name = this._name;
    	data._description = this._description;
    	data._stateType = this._stateType;
    	data._initiateActivity = this._initiateActivity.cloneData();
    	data._stateData = this._stateData.cloneData();

    	data._stateUserDefineSetting = this._stateUserDefineSetting.cloneData();
    	
    	data._stateDataUISetting = this._stateDataUISetting.cloneData();
    	
    	int i;
    	for(i=0; i < this._accessibleRoles.size(); i++) {
    		data._accessibleRoles.add(this._accessibleRoles.get(i).cloneData());
    	}
//    	for(i=0; i < this._autoEventTriggers.size(); i++) {
//    		data._autoEventTriggers.add(this._autoEventTriggers.get(i).cloneData());
//    	}
    	
    	for(i=0; i < this._activityEvents.size(); i++) {
    		data._activityEvents.add(this._activityEvents.get(i).cloneData());
    	}

    	for(i=0; i < this._activities.size(); i++) {
    		data._activities.add(this._activities.get(i).cloneData());
    	}
    	
    	return data;
    }
    
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getStateType() {
		return _stateType;
	}

	public void setStateType(String stateType) {
		_stateType = stateType;
	}

	public RelevantData getStateData() {
		return _stateData;
	}

	public void setStateData(RelevantData stateData) {
		_stateData = stateData;
	}

	public List<Role> getAccessibleRoles() {
		return _accessibleRoles;
	}

	public void setAccessibleRoles(List<Role> accessibleRoles) {
		_accessibleRoles = accessibleRoles;
	}

	public List<Activity> getActivities() {
		return _activities;
	}

	public void setActivities(List<Activity> activities) {
		_activities = activities;
	}

	public Activity getInitiateActivity() {
		return _initiateActivity;
	}

	public void setInitiateActivity(Activity initiateActivity) {
		_initiateActivity = initiateActivity;
	}

	public List<Event> getActivityEvents() {
		return _activityEvents;
	}

	public void setActivityEvents(List<Event> activityEvents) {
		_activityEvents = activityEvents;
	}

	public StateDataUISetting getStateDataUISetting() {
		return _stateDataUISetting;
	}

	public void setStateDataUISetting(StateDataUISetting stateDataUISetting) {
		_stateDataUISetting = stateDataUISetting;
	}

	public StateUserDefineSetting getStateUserDefineSetting() {
		return _stateUserDefineSetting;
	}

	public void setStateUserDefineSetting(
			StateUserDefineSetting stateUserDefineSetting) {
		_stateUserDefineSetting = stateUserDefineSetting;
	}


	
	
//	public List<AutoEventTrigger> getAutoEventTriggers() {
//		return _autoEventTriggers;
//	}
//
//	public void setAutoEventTriggers(List<AutoEventTrigger> autoEventTriggers) {
//		_autoEventTriggers = autoEventTriggers;
//	}
    
}
