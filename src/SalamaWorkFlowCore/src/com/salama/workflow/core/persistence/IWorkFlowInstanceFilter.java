package com.salama.workflow.core.persistence;

public interface IWorkFlowInstanceFilter {
	public boolean accept(WorkFlowInstance workFlowInst);
}
