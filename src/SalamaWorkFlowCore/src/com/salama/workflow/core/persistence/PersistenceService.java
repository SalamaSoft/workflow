package com.salama.workflow.core.persistence;

import java.util.Date;
import java.util.List;

import com.salama.workflow.core.error.PersistenceException;
import com.salama.workflow.core.meta.WorkFlow;
import com.salama.workflow.core.meta.WorkFlowSetting;

/**
 * All the return values of function are METOXML format  
 * @author liuxg
 *
 */
public interface PersistenceService {
	
	/**
	 * 
	 * @param configFilePath
	 * @throws PersistenceException
	 */
	public void init(String configFilePath) throws PersistenceException;
	
	/**
	 * 
	 * @throws PersistenceException
	 */
	public void destroy() throws PersistenceException;
	
	/**
	 * 
	 * @param workFlowName
	 * @return
	 * @throws PersistenceException
	 */
	public WorkFlow getMetaDataWorkFlow(String workFlowName) throws PersistenceException;
	
	/**
	 * 
	 * @param data
	 * @throws PersistenceException
	 */
	public void setMetaDataWorkFlow(WorkFlow data) throws PersistenceException;

	/**
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	public WorkFlowSetting getMetaDataWorkFlowSetting() throws PersistenceException;
	/**
	 * 
	 * @param data
	 * @throws PersistenceException
	 */
	public void setMetaDataWorkFlowSetting(WorkFlowSetting data) throws PersistenceException;

	/**
	 * 
	 * @param workFlowId
	 * @return
	 * @throws PersistenceException
	 */
	public WorkFlowInstance getWorkFlowInstance(long workFlowId) throws PersistenceException;
	/**
	 * 
	 * @param workFlowId
	 * @param data
	 * @throws PersistenceException
	 */
	public void setWorkFlowInstance(WorkFlowInstance data) throws PersistenceException;
	
	/**
	 * 
	 * @param workFlowId
	 * @return
	 * @throws PersistenceException
	 */
	public WorkFlowTraceInstance getWorkFlowTraceInstance(long workFlowId) throws PersistenceException;
	/**
	 * 
	 * @param workFlowId
	 * @param data
	 * @throws PersistenceException
	 */
	public void setWorkFlowTraceInstance(WorkFlowTraceInstance data) throws PersistenceException;
	
	/**
	 * 
	 * @param workFlowId
	 * @param stateId
	 * @return
	 * @throws PersistenceException
	 */
	public StateInstance getStateInstance(long stateId) throws PersistenceException;
	/**
	 * 
	 * @param workFlowId
	 * @param stateId
	 * @param data
	 * @throws PersistenceException
	 */
	public void setStateInstance(StateInstance data) throws PersistenceException;
	
	/*************** Map Index functions *********************/
	/**
	 * 
	 * @param role
	 * @param workFlowName
	 * @param workFlowId
	 * @throws PersistenceException
	 */
	public void addMapIndexOfRoleWorkFlow(String role, String workFlowName, long workFlowId) throws PersistenceException;
	/**
	 * 
	 * @param role
	 * @param stateId
	 * @throws PersistenceException
	 */
	public void addMapIndexOfRoleCurrentState(String role, long stateId) throws PersistenceException;
	/**
	 * 
	 * @param role
	 * @param stateId
	 * @throws PersistenceException
	 */
	public void deleteMapIndexOfRoleCurrentState(String role, long stateId) throws PersistenceException;
	/**
	 * 
	 * @param role
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 * @throws PersistenceException
	 */
	public List<Long> findWorkFlowIdByRole(String role, Date dateFrom, Date dateTo) throws PersistenceException;
	/**
	 * 
	 * @param role
	 * @param workFlowName
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 * @throws PersistenceException
	 */
	public List<Long> findWorkFlowIdByRole(String role, String workFlowName, Date dateFrom, Date dateTo) throws PersistenceException;

	/**
	 * 
	 * @param role
	 * @return
	 * @throws PersistenceException
	 */
	public List<Long> findCurrentStateIdByRole(String role) throws PersistenceException;

	public List<WorkFlowInstance> findWorkFlow(String workFlowName, Date dateFrom, Date dateTo) throws PersistenceException;

	public List<WorkFlowInstance> findWorkFlow(String workFlowName, Date dateFrom, Date dateTo, IWorkFlowInstanceFilter workFlowInstanceFilter) throws PersistenceException;
	
	public List<Long> findWorkFlowId(Date dateFrom, Date dateTo) throws PersistenceException;
}
