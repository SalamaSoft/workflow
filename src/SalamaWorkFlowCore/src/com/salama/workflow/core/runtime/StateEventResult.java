package com.salama.workflow.core.runtime;

import java.io.Serializable;

import com.salama.workflow.core.meta.SubFlow;
import com.salama.workflow.core.meta.Transition;

public class StateEventResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -870244168627936058L;

	private boolean _isGotoSubFlow = false;
	
	private SubFlow _subFlow = null;
	
	private Transition _transition = null;

	public boolean isGotoSubFlow() {
		return _isGotoSubFlow;
	}

	public void setGotoSubFlow(boolean isGotoSubFlow) {
		_isGotoSubFlow = isGotoSubFlow;
	}

	public SubFlow getSubFlow() {
		return _subFlow;
	}

	public void setSubFlow(SubFlow subFlow) {
		_subFlow = subFlow;
	}

	public Transition getTransition() {
		return _transition;
	}

	public void setTransition(Transition transition) {
		_transition = transition;
	}
	
}
