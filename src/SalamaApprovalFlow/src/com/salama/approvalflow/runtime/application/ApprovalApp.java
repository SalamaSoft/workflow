package com.salama.approvalflow.runtime.application;

import com.salama.approvalflow.runtime.data.ApprovalStateData;
import com.salama.workflow.core.persistence.StateInstance;

public class ApprovalApp implements IApprovalApp {
	/**
	 * 同意
	 */
	public void approve(StateInstance stateInst) {
		ApprovalStateData stateData = (ApprovalStateData) stateInst.getStateData();
		stateData.getApproverList().add(stateInst.getStateInstanceInfo().getUpdateUser());
	}
	
	/**
	 * 不同意
	 */
	public void disapprove(StateInstance stateInst) {
		ApprovalStateData stateData = (ApprovalStateData) stateInst.getStateData();
		stateData.getDisapproverList().add(stateInst.getStateInstanceInfo().getUpdateUser());
	}

	/**
	 * 
	 * @param stateInst
	 */
	public void initState(StateInstance stateInst) {
		//Do nothing
	}
}
