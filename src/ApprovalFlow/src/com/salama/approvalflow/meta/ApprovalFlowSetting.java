package com.salama.approvalflow.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.meta.RelevantData;
import com.salama.workflow.core.meta.Role;

public class ApprovalFlowSetting {
	private List<Role> _allRoles = new ArrayList<Role>();
	
	private List<RelevantData> _allStateDataTypes = new ArrayList<RelevantData>();

	public List<Role> getAllRoles() {
		return _allRoles;
	}

	public void setAllRoles(List<Role> allRoles) {
		_allRoles = allRoles;
	}

	public List<RelevantData> getAllStateDataTypes() {
		return _allStateDataTypes;
	}

	public void setAllStateDataTypes(List<RelevantData> allStateDataTypes) {
		_allStateDataTypes = allStateDataTypes;
	}
	
}
