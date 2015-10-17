package com.salama.approvalflow.meta.designer;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.meta.Role;

public class ApprovalFlow {
	private String _name = "";

	private String _description = "";
	
	private List<ApprovalState> _allStates = new ArrayList<ApprovalState>();

    private List<Role> _allRoles = new ArrayList<Role>();

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

	public List<ApprovalState> getAllStates() {
		return _allStates;
	}

	public void setAllStates(List<ApprovalState> allStates) {
		_allStates = allStates;
	}

	public List<Role> getAllRoles() {
		return _allRoles;
	}

	public void setAllRoles(List<Role> allRoles) {
		_allRoles = allRoles;
	}
    
    
}
