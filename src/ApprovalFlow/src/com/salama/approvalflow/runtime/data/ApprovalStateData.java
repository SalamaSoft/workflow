package com.salama.approvalflow.runtime.data;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.persistence.RoleUser;

public abstract class ApprovalStateData {
	private List<RoleUser> approverList = new ArrayList<RoleUser>();
	
	private List<RoleUser> disapproverList = new ArrayList<RoleUser>();

	public abstract boolean isCurrentStateApproved();
	
	public abstract boolean isCurrentStateDisapproved();

	public List<RoleUser> getApproverList() {
		return approverList;
	}

	public void setApproverList(List<RoleUser> approverList) {
		this.approverList = approverList;
	}

	public List<RoleUser> getDisapproverList() {
		return disapproverList;
	}

	public void setDisapproverList(List<RoleUser> disapproverList) {
		this.disapproverList = disapproverList;
	}
	
}
