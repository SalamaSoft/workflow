package com.salama.workflow.core.runtime;

import com.salama.workflow.core.IWorkFlowEngine;
import com.salama.workflow.core.meta.Activity;
import com.salama.workflow.core.meta.Transition;
import com.salama.workflow.core.persistence.StateInstance;

public interface IConditionHandler {
	
	public boolean handleCondition(IWorkFlowEngine workFlowEngine, StateInstance state, Activity activity, Transition transition);
	
}
