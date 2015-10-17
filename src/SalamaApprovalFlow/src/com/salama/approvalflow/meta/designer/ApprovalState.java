package com.salama.approvalflow.meta.designer;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.meta.RelevantData;
import com.salama.workflow.core.meta.Role;
import com.salama.workflow.core.meta.State;
import com.salama.workflow.core.meta.StateDataUISetting;
import com.salama.workflow.core.meta.StateUserDefineSetting;

public class ApprovalState {
    private String _name = "";
    
    private String _description = "";
    
    private String _stateType = State.StateType_Normal;

    private List<Role> _accessibleRoles = new ArrayList<Role>();

    private RelevantData _stateData = new RelevantData();

    private StateUserDefineSetting _stateUserDefineSetting = new StateUserDefineSetting();
    
    private StateDataUISetting _stateDataUISetting = new StateDataUISetting();
    
    private List<ApprovalActivity> _activityList = new ArrayList<ApprovalActivity>();

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

	public List<ApprovalActivity> getActivityList() {
		return _activityList;
	}

	public void setActivityList(List<ApprovalActivity> activityList) {
		_activityList = activityList;
	}

	public List<Role> getAccessibleRoles() {
		return _accessibleRoles;
	}

	public void setAccessibleRoles(List<Role> accessibleRoles) {
		_accessibleRoles = accessibleRoles;
	}

	public StateDataUISetting getStateDataUISetting() {
		return _stateDataUISetting;
	}

	public void setStateDataUISetting(StateDataUISetting stateDataUISetting) {
		_stateDataUISetting = stateDataUISetting;
	}

	public RelevantData getStateData() {
		return _stateData;
	}

	public void setStateData(RelevantData stateData) {
		_stateData = stateData;
	}

	public StateUserDefineSetting getStateUserDefineSetting() {
		return _stateUserDefineSetting;
	}

	public void setStateUserDefineSetting(
			StateUserDefineSetting stateUserDefineSetting) {
		_stateUserDefineSetting = stateUserDefineSetting;
	}
	
}
