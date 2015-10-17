package com.salama.workflow.core;

public interface IWorkFlowEngineContext {
	
	public IWorkFlowEngine getWorkFlowEngine();
	
	public void reload(com.salama.workflow.core.config.WorkFlowEngineContext contextConfig);
	
	public void destroy();
}
