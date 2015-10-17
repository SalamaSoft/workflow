package com.salama.workflow.core;

import java.util.Date;
import java.util.List;

import com.salama.workflow.core.error.AccessNoAuthorizationException;
import com.salama.workflow.core.error.DateExpiredException;
import com.salama.workflow.core.error.PersistenceException;
import com.salama.workflow.core.error.WorkFlowEngineException;
import com.salama.workflow.core.error.XmlSerializeException;
import com.salama.workflow.core.meta.State;
import com.salama.workflow.core.meta.WorkFlow;
import com.salama.workflow.core.persistence.IWorkFlowInstanceFilter;
import com.salama.workflow.core.persistence.RoleUser;
import com.salama.workflow.core.persistence.StateInstance;
import com.salama.workflow.core.persistence.WorkFlowInstance;
import com.salama.workflow.core.persistence.WorkFlowTraceInstance;
import com.salama.workflow.core.runtime.ValueInjectionSet;
import com.salama.workflow.core.runtime.StateEventResult;

public interface IWorkFlowEngine {
	public enum WorkFlowStatus {All, 
		Running, 
		//Suspended, 
		Terminated, 
		Completed};
	
	public void init() throws WorkFlowEngineException;
	
	public void destroy() throws WorkFlowEngineException;

	public List<WorkFlowInstance> findWorkFlow(String role, WorkFlowStatus status, Date creationDateFrom, Date creationDateEnd) 
			throws WorkFlowEngineException;

	public List<WorkFlowInstance> findWorkFlow(String role, WorkFlowStatus status, String workFlowName, Date creationDateFrom, Date creationDateEnd) 
			throws WorkFlowEngineException;

	public List<WorkFlowInstance> findWorkFlow(String role, String workFlowName, Date creationDateFrom, Date creationDateEnd, IWorkFlowInstanceFilter workFlowInstanceFilter) 
			throws WorkFlowEngineException;

	public List<WorkFlowInstance> findWorkFlow(WorkFlowStatus status, Date creationDateFrom, Date creationDateEnd) 
			throws WorkFlowEngineException;

	public List<WorkFlowInstance> findWorkFlow(WorkFlowStatus status, String workFlowName, Date creationDateFrom, Date creationDateEnd) 
			throws WorkFlowEngineException;

	public List<WorkFlowInstance> findWorkFlow(String workFlowName, Date creationDateFrom, Date creationDateEnd, IWorkFlowInstanceFilter workFlowInstanceFilter) 
			throws WorkFlowEngineException;
	
	
	public List<StateInstance> findCurrentState(String role) 
			throws WorkFlowEngineException;

	/**
	 * 
	 * @param role
	 * @param workFlowName
	 * @param valueInjectionToState
	 * @param isSubFlow
	 * @param parentFlowId
	 * @return
	 * @throws AccessNoAuthorizationException
	 * @throws DateExpiredException
	 * @throws PersistenceException
	 * @throws XmlSerializeException
	 * @throws WorkFlowEngineException
	 */
	public WorkFlowInstance createWorkFlow(RoleUser user, String workFlowName, 
			ValueInjectionSet valueInjectionToState, long parentFlowId, String parentActivityName) 
			throws AccessNoAuthorizationException, DateExpiredException, WorkFlowEngineException;

	public void cancelWorkFlow(RoleUser user, long workFlowId) throws AccessNoAuthorizationException, DateExpiredException, WorkFlowEngineException;
	
	public StateEventResult triggerStateEvent(RoleUser user, 
			long workFlowId, String eventName, 
			ValueInjectionSet valueInjectionToState)
					throws AccessNoAuthorizationException, DateExpiredException, WorkFlowEngineException;
	
	public WorkFlowInstance getWorkFlow(String role, long workFlowId) 
			throws AccessNoAuthorizationException, DateExpiredException, WorkFlowEngineException;

	public WorkFlowInstance getWorkFlow(long workFlowId) 
			throws WorkFlowEngineException;
	
	public StateInstance getState(String role, long stateId) 
			throws AccessNoAuthorizationException, DateExpiredException, WorkFlowEngineException;

	public StateInstance getState(long stateId) 
			throws WorkFlowEngineException;

	public void updateState(StateInstance stateInst) throws WorkFlowEngineException;;

	public void updateState(StateInstance stateInst, ValueInjectionSet valueInjectionToState) throws WorkFlowEngineException;;

	public void updateState(long stateId, ValueInjectionSet valueInjectionToState) throws WorkFlowEngineException;;
	
	public WorkFlow getMetaWorkFlow(String workFlowName) 
			throws WorkFlowEngineException;
	
	public void updateMetaWorkFlow(WorkFlow metaData) 
			throws WorkFlowEngineException;
	
	public State getMetaState(String workFlowName, String stateName) 
			throws WorkFlowEngineException;
	
	public void updateMetaState(String workFlowName, State metaData) 
			throws WorkFlowEngineException;
	
	public WorkFlowTraceInstance getWorkFlowTraceInstance(long workFlowId) 
			throws WorkFlowEngineException;
}
