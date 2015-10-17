package com.salama.approvalflow.runtime.application;

import com.salama.workflow.core.persistence.StateInstance;

public interface IApprovalApp {
	/**
	 * 同意
	 * @param stateInst
	 */
	public void approve(StateInstance stateInst);
	
	/**
	 * 不同意
	 */
	public void disapprove(StateInstance stateInst);

	/**
	 * 初始化状态内容
	 * @param stateInst
	 */
	public void initState(StateInstance stateInst);

}
