package com.salama.workflow.core.persistence;

public interface IStateInstanceFilter {
	public boolean accept(StateInstance stateInst);
}
