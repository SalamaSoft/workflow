package com.salama.workflow.core.persistence;

public class RoleUser {
	private String _role = "";
	
	private String _userId = "";

	public RoleUser() {
		
	}
	
	public RoleUser(String role, String userId) {
		_role = role;
		_userId = userId;
	}
	

	public String getRole() {
		return _role;
	}

	public void setRole(String role) {
		_role = role;
	}

	public String getUserId() {
		return _userId;
	}

	public void setUserId(String userId) {
		_userId = userId;
	}
	
}
