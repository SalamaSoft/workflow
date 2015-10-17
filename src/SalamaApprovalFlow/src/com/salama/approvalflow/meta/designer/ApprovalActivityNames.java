package com.salama.approvalflow.meta.designer;

public class ApprovalActivityNames {
	
	/**
	 * 同意
	 */
	public final static String Approve = "approve";
	
	/**
	 * 不同意
	 */
	public final static String Disapprove = "disapprove";
	
	public final static String[] getAllTypes() {
		return new String[]{Approve, Disapprove};
	}
}
