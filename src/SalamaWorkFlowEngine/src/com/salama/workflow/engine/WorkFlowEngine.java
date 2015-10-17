package com.salama.workflow.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.salama.workflow.core.IWorkFlowEngine;
import com.salama.workflow.core.error.AccessNoAuthorizationException;
import com.salama.workflow.core.error.DateExpiredException;
import com.salama.workflow.core.error.PersistenceException;
import com.salama.workflow.core.error.WorkFlowEngineException;
import com.salama.workflow.core.meta.Activity;
import com.salama.workflow.core.meta.Role;
import com.salama.workflow.core.meta.SetValue;
import com.salama.workflow.core.meta.State;
import com.salama.workflow.core.meta.SubFlow;
import com.salama.workflow.core.meta.Transition;
import com.salama.workflow.core.meta.WorkFlow;
import com.salama.workflow.core.meta.WorkFlowSetting;
import com.salama.workflow.core.persistence.IWorkFlowInstanceFilter;
import com.salama.workflow.core.persistence.PersistenceService;
import com.salama.workflow.core.persistence.RoleUser;
import com.salama.workflow.core.persistence.StateInstance;
import com.salama.workflow.core.persistence.StateOperation;
import com.salama.workflow.core.persistence.StateTraceData;
import com.salama.workflow.core.persistence.WorkFlowInstance;
import com.salama.workflow.core.persistence.WorkFlowTraceInstance;
import com.salama.workflow.core.runtime.StateEventResult;
import com.salama.workflow.core.runtime.ValueInjection;
import com.salama.workflow.core.runtime.ValueInjectionSet;
import com.salama.workflow.core.util.DateFormat;
import com.salama.workflow.engine.util.WorkFlowUtil;

/**
 * About the expression:
 * 		The translator of the expression is OGNL likely. There is the help document: http://commons.apache.org/ognl/language-guide.html
 * 		For example: stateData.XXX, appReturnValue.XXX
 * 
 * 		The context for get the value by expression:
 * 			name: stateData.<propertyName>    value:The value of the property named <propertyName> in stateData
 * 			name: returnValue.<propertyName>  value:The value of the property named <propertyName> in returnValue
 * 		
 * 		The flow of the value transmition: 
 * 			set the value of stateData(from the user invoking or the transition of the previous state) 
 * 			-> invoke initiateActivity.application(use the defined value expression of the params, value from the stateData)
 * 			-> set the returnValue or stateData to the properties of the next state(value from the stateData or returnValue) 
 *  
 * @author liuxg
 *
 */
public class WorkFlowEngine implements IWorkFlowEngine {

	private final static Logger logger = Logger.getLogger(WorkFlowEngine.class);
	
	private WorkFlowEngineContext _context = null;

	private PersistenceService _persistenceService = null;

	/**************************** instance in memory --> **************************/
	private WorkFlowSetting _workFlowSetting = new WorkFlowSetting();
	/**
	 * Key: Name of workflow
	 * Value: Meta setting of workflow
	 */
	private HashMap<String, WorkFlow> _metaMapWorkFlow = new HashMap<String, WorkFlow>();
	/**************************** instance in memory <-- **************************/
	
	private Object _lockForSeq = new Object();
	
	public WorkFlowEngine(WorkFlowEngineContext context) 
	{
		_context = context;
	}
	
	@Override
	public void init()
			throws WorkFlowEngineException {
		_persistenceService = _context.getPersistenceService();

		try {
			initInstanceMemoryInstance();
		} catch(Exception e) {
			logger.error("init()", e);
			throw new WorkFlowEngineException(e);
		}
	}
	
	@Override
	public void destroy()
			throws WorkFlowEngineException {
		_context = null;
		_persistenceService = null;
	}
	
	@Override
	public List<WorkFlowInstance> findWorkFlow(String role, WorkFlowStatus status, Date creationDateFrom, Date creationDateEnd) 
			throws WorkFlowEngineException {
		try {
			logger.debug("findWorkFlow()#1 begin");

			List<WorkFlowInstance> listData = new ArrayList<WorkFlowInstance>();

			List<Long> workFlowIdList = _persistenceService.findWorkFlowIdByRole(role, creationDateFrom, creationDateEnd);

			long workFlowId = 0;
			WorkFlowInstance data = null;
			
			for(int i = 0; i < workFlowIdList.size(); i++) {
				workFlowId = workFlowIdList.get(i);
				
				data = _persistenceService.getWorkFlowInstance(workFlowId);
				
				if(status == WorkFlowStatus.All) {
					listData.add(data);
				} else if((status == WorkFlowStatus.Running) 
						&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_RUNNING)) {
					listData.add(data);
//				} else if((status == WorkFlowStatus.Suspended)
//						&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_SUSPENDED)) {
//					listData.add(data);
				} else if((status == WorkFlowStatus.Terminated)
						&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_TERMINATED)) {
					listData.add(data);
				} else if((status == WorkFlowStatus.Completed)
						&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_COMPLETED)) {
					listData.add(data);
				}
			}
			
			logger.debug("findWorkFlow()#1 end");
			return listData;
		} catch(Exception e) {
			logger.error("findWorkFlow()", e);
			throw new WorkFlowEngineException(e);
		}
	}

	@Override
	public List<WorkFlowInstance> findWorkFlow(String role, WorkFlowStatus status, String workFlowName, Date creationDateFrom, Date creationDateEnd) 
			throws WorkFlowEngineException {
		try {
			logger.debug("findWorkFlow()#2 begin");

			List<WorkFlowInstance> listData = new ArrayList<WorkFlowInstance>();

			List<Long> workFlowIdList = _persistenceService.findWorkFlowIdByRole(role, workFlowName, creationDateFrom, creationDateEnd);

			long workFlowId = 0;
			WorkFlowInstance data = null;
			
			for(int i = 0; i < workFlowIdList.size(); i++) {
				workFlowId = workFlowIdList.get(i);
				
				data = _persistenceService.getWorkFlowInstance(workFlowId);
				
				if(data.getWorkFlowName().equals(workFlowName)) {
					if(status == WorkFlowStatus.All) {
						listData.add(data);
					} else if((status == WorkFlowStatus.Running) 
							&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_RUNNING)) {
						listData.add(data);
//					} else if((status == WorkFlowStatus.Suspended)
//							&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_SUSPENDED)) {
//						listData.add(data);
					} else if((status == WorkFlowStatus.Terminated)
							&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_TERMINATED)) {
						listData.add(data);
					} else if((status == WorkFlowStatus.Completed)
							&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_COMPLETED)) {
						listData.add(data);
					}
				}
			}
			
			logger.debug("findWorkFlow()#2 end");

			return listData;
		} catch(Exception e) {
			logger.error("findWorkFlow()", e);
			throw new WorkFlowEngineException(e);
		}
	}

	@Override
	public List<WorkFlowInstance> findWorkFlow(String role,
			String workFlowName, Date creationDateFrom, Date creationDateEnd,
			IWorkFlowInstanceFilter workFlowInstanceFilter)
			throws WorkFlowEngineException {
		try {
			logger.debug("findWorkFlow()#3 begin");

			List<WorkFlowInstance> listData = new ArrayList<WorkFlowInstance>();

			List<Long> workFlowIdList = _persistenceService.findWorkFlowIdByRole(role, workFlowName, creationDateFrom, creationDateEnd);

			long workFlowId = 0;
			WorkFlowInstance data = null;
			
			for(int i = 0; i < workFlowIdList.size(); i++) {
				workFlowId = workFlowIdList.get(i);
				
				data = _persistenceService.getWorkFlowInstance(workFlowId);

				if(data.getWorkFlowName().equals(workFlowName)) {
					if(workFlowInstanceFilter.accept(data)) {
						listData.add(data);
					}
				}
			}

			logger.debug("findWorkFlow()#3 end");
			return listData;
		} catch(Exception e) {
			logger.error("findWorkFlow()", e);
			throw new WorkFlowEngineException(e);
		}
	}
	
	@Override
	public List<WorkFlowInstance> findWorkFlow(WorkFlowStatus status,
			Date creationDateFrom, Date creationDateEnd)
			throws WorkFlowEngineException {
		try {
			logger.debug("findWorkFlow()#4 begin");

			List<WorkFlowInstance> listData = new ArrayList<WorkFlowInstance>();

			List<Long> workFlowIdList = _persistenceService.findWorkFlowId(creationDateFrom, creationDateEnd);

			long workFlowId = 0;
			WorkFlowInstance data = null;
			
			for(int i = 0; i < workFlowIdList.size(); i++) {
				workFlowId = workFlowIdList.get(i);
				
				data = _persistenceService.getWorkFlowInstance(workFlowId);
				
				if(status == WorkFlowStatus.All) {
					listData.add(data);
				} else if((status == WorkFlowStatus.Running) 
						&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_RUNNING)) {
					listData.add(data);
//				} else if((status == WorkFlowStatus.Suspended)
//						&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_SUSPENDED)) {
//					listData.add(data);
				} else if((status == WorkFlowStatus.Terminated)
						&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_TERMINATED)) {
					listData.add(data);
				} else if((status == WorkFlowStatus.Completed)
						&& (data.getCurrentWorkFlowStatus() == WorkFlowInstance.WORK_FLOW_STATUS_COMPLETED)) {
					listData.add(data);
				}
			}
			
			logger.debug("findWorkFlow()#4 end");
			return listData;
		} catch(Exception e) {
			logger.error("findWorkFlow()", e);
			throw new WorkFlowEngineException(e);
		}
	}

	@Override
	public List<WorkFlowInstance> findWorkFlow(WorkFlowStatus status,
			String workFlowName, Date creationDateFrom, Date creationDateEnd)
			throws WorkFlowEngineException {
		try {
			logger.debug("findWorkFlow()#5 begin");

			if(status == WorkFlowStatus.All) {
				return _persistenceService.findWorkFlow(
						workFlowName, creationDateFrom, creationDateEnd);
			} else {
				int targetStatus = 0;
				
				if(status == WorkFlowStatus.Running) {
					targetStatus = WorkFlowInstance.WORK_FLOW_STATUS_RUNNING;
				} else if(status == WorkFlowStatus.Terminated) {
					targetStatus = WorkFlowInstance.WORK_FLOW_STATUS_TERMINATED;
				} else if(status == WorkFlowStatus.Completed) {
					targetStatus = WorkFlowInstance.WORK_FLOW_STATUS_COMPLETED;
				}
				
				logger.debug("findWorkFlow()#5 end");

				return _persistenceService.findWorkFlow(
						workFlowName, creationDateFrom, creationDateEnd, 
						new WorkFlowInstanceStatusFilter(targetStatus));
			}
		} catch(Exception e) {
			logger.error("findWorkFlow()", e);
			throw new WorkFlowEngineException(e);
		}
	}
	
	@Override
	public List<WorkFlowInstance> findWorkFlow(String workFlowName,
			Date creationDateFrom, Date creationDateEnd,
			IWorkFlowInstanceFilter workFlowInstanceFilter)
			throws WorkFlowEngineException {
		try {
			return _persistenceService.findWorkFlow(workFlowName, creationDateFrom, creationDateEnd, workFlowInstanceFilter);
		} catch (PersistenceException e) {
			throw new WorkFlowEngineException(e);
		}
	}

	@Override
	public StateInstance getState(long stateId) throws WorkFlowEngineException {
		try {
			return _persistenceService.getStateInstance(stateId);
		} catch (PersistenceException e) {
			logger.error("getState()", e);
			throw new WorkFlowEngineException(e);
		}
	}

	@Override
	public void updateState(StateInstance stateInst)
			throws WorkFlowEngineException {
		try {
			_persistenceService.setStateInstance(stateInst);
		} catch(Exception e) {
			logger.error("updateState()", e);
			throw new WorkFlowEngineException(e);
		}
	}
	
	@Override
	public void updateState(StateInstance stateInst,
			ValueInjectionSet valueInjectionToState)
			throws WorkFlowEngineException {
		try {
			WorkFlowUtil.injectValueToState(stateInst, valueInjectionToState);
			_persistenceService.setStateInstance(stateInst);
		} catch(Exception e) {
			logger.error("updateState()", e);
			throw new WorkFlowEngineException(e);
		}
	}
	
	@Override
	public void updateState(long stateId,
			ValueInjectionSet valueInjectionToState)
			throws WorkFlowEngineException {
		try {
			StateInstance stateInst = _persistenceService.getStateInstance(stateId);
			
			updateState(stateInst, valueInjectionToState);
		} catch(Exception e) {
			logger.error("updateState()", e);
			throw new WorkFlowEngineException(e);
		}
	}
	
	@Override
	public WorkFlowInstance getWorkFlow(long workFlowId)
			throws WorkFlowEngineException {
		try {
			return _persistenceService.getWorkFlowInstance(workFlowId);
		} catch (PersistenceException e) {
			logger.error("getState()", e);
			throw new WorkFlowEngineException(e);
		}
	}
	
	@Override
	public List<StateInstance> findCurrentState(String role) 
			throws WorkFlowEngineException {
		try {
			logger.debug("findCurrentState() begin");

			List<StateInstance> listData = new ArrayList<StateInstance>();
			List<Long> idList = _persistenceService.findCurrentStateIdByRole(role);
			
			long stateId = 0;
			StateInstance data = null;
			
			for(int i = 0; i < idList.size(); i++) {
				data = _persistenceService.getStateInstance(stateId);
				
				listData.add(data);
			}
			
			logger.debug("findCurrentState() end");
			return listData;
		} catch(Exception e) {
			logger.error("findWorkFlow()", e);
			throw new WorkFlowEngineException(e);
		}
	}

	/**
	 * 
	 * @param user
	 * @param workFlowName
	 * @param valueInjectionToStateData
	 * @param parentFlowId
	 * @return
	 * @throws AccessNoAuthorizationException
	 * @throws DateExpiredException
	 * @throws WorkFlowEngineException
	 */
	@Override
	public WorkFlowInstance createWorkFlow(RoleUser user, String workFlowName, 
			ValueInjectionSet valueInjectionToStateData, long parentFlowId, String parentActivityName) 
			throws AccessNoAuthorizationException, DateExpiredException, WorkFlowEngineException {
		try {
			logger.debug("createWorkFlow() begin");

			//get meta and check authority
			Date creationDate = new Date();
			WorkFlow workFlowMeta = _metaMapWorkFlow.get(workFlowName);
			
			//Check whether date is expired
			checkWorkFlowDateExpired(workFlowMeta, creationDate);
			
			//Get state meta
			String startStateName = workFlowMeta.getStartState();
			State startStateMeta = WorkFlowUtil.getMetaDataState(workFlowMeta, startStateName);
			
			//check state access authority
			checkRoleAccessAuthorization(startStateMeta.getAccessibleRoles(), user.getRole());
			
			//init workflow instance
			WorkFlowInstance workFlowInst = new WorkFlowInstance();
			workFlowInst.setCreationTime(new Date());
			workFlowInst.setCurrentWorkFlowStatus(WorkFlowInstance.WORK_FLOW_STATUS_RUNNING);
			workFlowInst.setId(getSeq());
			if(parentFlowId > 0) {
				workFlowInst.setParentFlowId(parentFlowId);
				workFlowInst.setParentActivityName(parentActivityName);
			}
			workFlowInst.setWorkFlowName(workFlowName);
			
			//init state 
			StateInstance stateInst = createStateInstance(user, startStateMeta, workFlowInst);
			WorkFlowUtil.injectValueToState(stateInst, valueInjectionToStateData);

			//check activity access authority
			Activity activity = startStateMeta.getInitiateActivity();
			if(activity.getName().trim().length() > 0) {
				checkRoleAccessAuthorization(activity.getAccessibleRoles(), user.getRole());
				
				//An activity for init could not be sub flow, only application type.  
				//excute application
				WorkFlowUtil.doActivityOfApplicationType(this, activity, stateInst, _context.getApplicationClassFinder(), _context.getDataClassFinder());
			} 
			
			//store instance of workFlow
			saveWorkFlowStateOnFlowCreated(user, workFlowMeta, workFlowInst, startStateMeta, stateInst);
			
			logger.debug("createWorkFlow() end");
			
			return workFlowInst;
		} catch (Exception e) {
			logger.error("createWorkFlow()", e);
			throw new WorkFlowEngineException(e);
		}
	}
	
	@Override
	public void cancelWorkFlow(RoleUser user, long workFlowId) 
			throws AccessNoAuthorizationException, DateExpiredException, WorkFlowEngineException {
		try {
			logger.debug("cancelWorkFlow() begin");

			//make workflow status to terminated
			WorkFlowInstance workFlowInst = _persistenceService.getWorkFlowInstance(workFlowId);
			if(workFlowInst == null) {
				return;
			}
			
			workFlowInst.setCurrentWorkFlowStatus(WorkFlowInstance.WORK_FLOW_STATUS_TERMINATED);
			
			//get current state
			StateInstance currentState = _persistenceService.getStateInstance(workFlowInst.getCurrentStateId());
			
			WorkFlow workFlowMeta = _metaMapWorkFlow.get(workFlowInst.getWorkFlowName());
			State stateMeta = WorkFlowUtil.getMetaDataState(workFlowMeta, currentState.getStateInstanceInfo().getStateName());

			//delete role index map of current state
			deleteMapIndexOfRoleCurrentState(stateMeta, currentState.getStateInstanceInfo().getStateId());

			logger.debug("cancelWorkFlow() end");
		} catch(Exception e) {
			throw new WorkFlowEngineException(e);
		}
	}
	
	@Override
	public StateEventResult triggerStateEvent(RoleUser user, 
			long workFlowId, String eventName, 
			ValueInjectionSet valueInjectionToStateData)
					throws AccessNoAuthorizationException, DateExpiredException, WorkFlowEngineException {
		try {
			logger.debug("triggerStateEvent() begin");

			//get workFlowInstance
			WorkFlowInstance workFlowInst = _persistenceService.getWorkFlowInstance(workFlowId);
			StateInstance currentStateInst = _persistenceService.getStateInstance(workFlowInst.getCurrentStateId());
			
			//get meta and check authority
			Date creationDate = new Date();
			WorkFlow workFlowMeta = _metaMapWorkFlow.get(workFlowInst.getWorkFlowName());
			
			//Check whether date is expired
			checkWorkFlowDateExpired(workFlowMeta, creationDate);
			
			//Get state meta
			State currentStateMeta = WorkFlowUtil.getMetaDataState(workFlowMeta, currentStateInst.getStateInstanceInfo().getStateName());
			
			//check state access authority
			checkRoleAccessAuthorization(currentStateMeta.getAccessibleRoles(), user.getRole());

			//inject value to current state
			currentStateInst.getStateInstanceInfo().setUpdateTime(new Date());
			currentStateInst.getStateInstanceInfo().setUpdateUser(user);
			WorkFlowUtil.injectValueToState(currentStateInst, valueInjectionToStateData);

			//check activity access authority
			Activity activity = WorkFlowUtil.getActivityByEvent(currentStateMeta, eventName);
			checkRoleAccessAuthorization(activity.getAccessibleRoles(), user.getRole());
			
			//excute application
			if((activity.getSubFlow() != null) && (activity.getSubFlow().getWorkFlowName().trim().length() > 0)) {
				StateEventResult eventResult = new StateEventResult();
				eventResult.setGotoSubFlow(true);
				eventResult.setSubFlow(activity.getSubFlow());

				//save current state before create sub flow. After sub flow completed, process will return to this state
				StateOperation stateOperation = new StateOperation();
				stateOperation.setActivityName(activity.getName());
				stateOperation.setActivityDescription(activity.getDescription());
				stateOperation.setEventName(eventName);
				stateOperation.setEventResult(eventResult);
				currentStateInst.setStateOperation(stateOperation);
				_persistenceService.setStateInstance(currentStateInst);
				
				//activity of sub flow type, so create a sub flow
				SubFlow subFlow = activity.getSubFlow();
				ValueInjectionSet valueInjectSetToSubFlow = new ValueInjectionSet();
				
				ValueInjection valueInjectTmp = null;
				SetValue setValueTmp = null;
				
				for(int i = 0; i < subFlow.getSetParentStateValueToState().size(); i++) {
					setValueTmp = subFlow.getSetParentStateValueToState().get(i);
					valueInjectTmp = new ValueInjection();
					valueInjectTmp.setTargetExpression(setValueTmp.getDestExpression());
					valueInjectTmp.setValue(WorkFlowUtil.getValueFromState(currentStateInst, setValueTmp.getSrcExpression()));
					
					valueInjectSetToSubFlow.getValueInjectionSet().add(valueInjectTmp);
				}
				
				createWorkFlow(
						user, subFlow.getWorkFlowName(), 
						valueInjectSetToSubFlow, workFlowInst.getId(), activity.getName());
				
				logger.debug("triggerStateEvent() end");

				return eventResult;
			} else {
				//activity of application type
				Transition transition = WorkFlowUtil.doActivityOfApplicationType(this,
						activity, currentStateInst, _context.getApplicationClassFinder(), _context.getDataClassFinder());

				String nextStateName = transition.getToState();
				State nextStateMeta = WorkFlowUtil.getMetaDataState(workFlowMeta, nextStateName);
				//current workflow
				StateInstance nextStateInst = null;

				//move to next state to 
				nextStateInst = createStateInstance(user, nextStateMeta, workFlowInst);
				
				//set value to next state
				WorkFlowUtil.injectValueFromStateToState(currentStateInst, nextStateInst, transition.getSetValueToToState());
				
				//Init next state by init activity
				Activity nextInitStateActivity =  nextStateMeta.getInitiateActivity();
				if(nextInitStateActivity.getName().trim().length() > 0) {
					checkRoleAccessAuthorization(nextInitStateActivity.getAccessibleRoles(), user.getRole());
					
					//excute application
					WorkFlowUtil.doActivityOfApplicationType(this,
							nextInitStateActivity, nextStateInst, _context.getApplicationClassFinder(), _context.getDataClassFinder());
				}

				//store instance of workFlow
				StateEventResult eventResult = new StateEventResult();
				eventResult.setGotoSubFlow(false);
				eventResult.setTransition(transition);

				StateOperation stateOperation = new StateOperation();
				stateOperation.setActivityName(activity.getName());
				stateOperation.setActivityDescription(activity.getDescription());
				stateOperation.setEventName(eventName);
				stateOperation.setEventResult(eventResult);
				currentStateInst.setStateOperation(stateOperation);
				
				if(nextStateMeta.getStateType().equals(State.StateType_Terminated)) {
					workFlowInst.setCurrentWorkFlowStatus(WorkFlowInstance.WORK_FLOW_STATUS_TERMINATED);
				} else if (nextStateMeta.getStateType().equals(State.StateType_Completed)) {
					workFlowInst.setCurrentWorkFlowStatus(WorkFlowInstance.WORK_FLOW_STATUS_COMPLETED);
				}
				saveWorkFlowStateOnMoveToNextState(user, workFlowMeta, workFlowInst, 
						currentStateMeta, currentStateInst, nextStateMeta, nextStateInst);
				
				if(nextStateMeta.getStateType().equals(State.StateType_Terminated)
						|| nextStateMeta.getStateType().equals(State.StateType_Completed)) {
					if(workFlowInst.getParentFlowId() > 0) {
						//back to parent flow
						WorkFlowInstance parentWorkFlowInst = _persistenceService.getWorkFlowInstance(
								workFlowInst.getParentFlowId());
						WorkFlow parentWorkFlowMeta = _metaMapWorkFlow.get(parentWorkFlowInst.getWorkFlowName());
						StateInstance parentCurrentStateInst = _persistenceService.getStateInstance(parentWorkFlowInst.getCurrentStateId());
						State parentCurrentStateMeta = WorkFlowUtil.getMetaDataState(
								parentWorkFlowMeta, parentCurrentStateInst.getStateInstanceInfo().getStateName());
						Activity parentActivity = WorkFlowUtil.getActivityByActivityName(parentCurrentStateMeta, workFlowInst.getParentActivityName());
						//set state value from return value of sub flow
						WorkFlowUtil.injectValueFromStateToState(
								currentStateInst, parentCurrentStateInst, parentActivity.getSubFlow().getSetStateValueToParentState());
						
						//save instance
						saveWorkFlowStateOnMoveToNextState(user, 
								parentWorkFlowMeta, parentWorkFlowInst, 
								nextStateMeta, nextStateInst,
								parentCurrentStateMeta, parentCurrentStateInst);
						
					} else {
						//this is not a sub flow, so delete current state
						deleteMapIndexOfRoleCurrentState(nextStateMeta, nextStateInst.getStateInstanceInfo().getStateId());
					}
					
				}

				logger.debug("triggerStateEvent() end");

				return eventResult;
			}
			
		} catch (Exception e) {
			logger.error("createWorkFlow()", e);
			throw new WorkFlowEngineException(e);
		}
	}
	
	@Override
	public WorkFlowInstance getWorkFlow(String role, long workFlowId) 
			throws AccessNoAuthorizationException, DateExpiredException, WorkFlowEngineException {
		WorkFlowInstance workFlowInst = null;
		try {
			workFlowInst = _persistenceService.getWorkFlowInstance(workFlowId);
		} catch(PersistenceException e) {
			throw new WorkFlowEngineException(e);
		}

		WorkFlow workFlowMeta = _metaMapWorkFlow.get(workFlowInst.getWorkFlowName());
		checkRoleAccessAuthorization(workFlowMeta.getAllRoles(), role);

		return workFlowInst;
	}
	
	@Override
	public StateInstance getState(String role, long stateId) 
			throws AccessNoAuthorizationException, DateExpiredException, WorkFlowEngineException {
		StateInstance stateInst = null;
		try {
			stateInst = _persistenceService.getStateInstance(stateId);
		} catch(PersistenceException e) {
			throw new WorkFlowEngineException(e);
		}

		WorkFlow workFlowMeta = _metaMapWorkFlow.get(stateInst.getStateInstanceInfo().getWorkFlowName());
		State stateMeta = WorkFlowUtil.getMetaDataState(workFlowMeta, stateInst.getStateInstanceInfo().getStateName());
		checkRoleAccessAuthorization(stateMeta.getAccessibleRoles(), role);

		return stateInst;
	}

	@Override
	public WorkFlow getMetaWorkFlow(String workFlowName) 
			throws WorkFlowEngineException {
		return _metaMapWorkFlow.get(workFlowName);
	}
	
	@Override
	public void updateMetaWorkFlow(WorkFlow metaData) 
			throws WorkFlowEngineException {
		try {
			_persistenceService.setMetaDataWorkFlow(metaData);
		} catch(PersistenceException e) {
			throw new WorkFlowEngineException(e);
		}
		_metaMapWorkFlow.put(metaData.getName(), metaData);
	}
	
	@Override
	public State getMetaState(String workFlowName, String stateName) 
			throws WorkFlowEngineException {
		WorkFlow workFlowMeta = _metaMapWorkFlow.get(workFlowName);
		return WorkFlowUtil.getMetaDataState(workFlowMeta, stateName);
	}
	
	@Override
	public void updateMetaState(String workFlowName, State metaData) 
			throws WorkFlowEngineException {
		WorkFlow workFlowMeta = _metaMapWorkFlow.get(workFlowName);
		WorkFlowUtil.setMetaDataState(workFlowMeta, metaData);
	}
	
	@Override
	public WorkFlowTraceInstance getWorkFlowTraceInstance(long workFlowId)
			throws WorkFlowEngineException {
		try {
			return _persistenceService.getWorkFlowTraceInstance(workFlowId);
		} catch (PersistenceException e) {
			throw new WorkFlowEngineException(e);
		}
	}
	
	/********************** Validate *******************************/
	private static void checkWorkFlowDateExpired(WorkFlow flow, Date creationDate) 
	throws DateExpiredException
	{
		int createYMD = Integer.parseInt(DateFormat.formatDateYYYYMMDD(creationDate));
		int beginYMD = Integer.parseInt(flow.getValidBeginDate());
		int endYMD = Integer.parseInt(flow.getValidEndDate());
		
		if(createYMD < beginYMD || createYMD > endYMD) {
			throw new DateExpiredException();
		}
	}
	
	private static void checkRoleAccessAuthorization(List<Role> accessibleRoles, String role) 
	throws AccessNoAuthorizationException
	{
		if(accessibleRoles.size() == 0) {
			return;
		}
		
		for(int i = 0; i < accessibleRoles.size(); i++) {
			if(accessibleRoles.get(i).getName().equals(role)) {
				return;
			}
		}
		
		throw new AccessNoAuthorizationException();
	}
	
	private long getSeq() throws InterruptedException {
		synchronized (_lockForSeq) {
			long seq = System.currentTimeMillis();
			Thread.sleep(1);
			
			return seq;
		}
	}
	
	private void initInstanceMemoryInstance() throws PersistenceException {
		_workFlowSetting = _persistenceService.getMetaDataWorkFlowSetting();  
		
		_metaMapWorkFlow.clear();
		List<String> workFlowNames = _workFlowSetting.getAllWorkFlowNames();
		String workFlowName = null;
		
		for(int i = 0; i < workFlowNames.size(); i++) {
			workFlowName = workFlowNames.get(i);
			
			_metaMapWorkFlow.put(workFlowName, _persistenceService.getMetaDataWorkFlow(workFlowName));
		}
	}

	private StateInstance createStateInstance(RoleUser user, State stateMeta, WorkFlowInstance workFlowInstance) 
			throws ClassNotFoundException, InterruptedException, IllegalAccessException, InstantiationException {
		StateInstance state = new StateInstance();
		state.getStateInstanceInfo().setStateDataClass(stateMeta.getStateData().getDataClass());
		state.getStateInstanceInfo().setStateId(getSeq());
		state.getStateInstanceInfo().setWorkFlowId(workFlowInstance.getId());
		state.getStateInstanceInfo().setWorkFlowName(workFlowInstance.getWorkFlowName());
		state.getStateInstanceInfo().setStateName(stateMeta.getName());
		state.getStateInstanceInfo().setStateDescription(stateMeta.getDescription());
		state.getStateInstanceInfo().setUpdateUser(user);
		Object stateData = _context.getDataClassFinder().findClass(state.getStateInstanceInfo().getStateDataClass()).newInstance();
		state.setStateData(stateData);
		
		return state;
	}

	
	private void saveWorkFlowStateOnFlowCreated(RoleUser user, 
			WorkFlow workFlowMeta, WorkFlowInstance workFlowInst,
			State currentStateMeta, StateInstance currentStateInst) 
			throws PersistenceException {
		//WorkFlow
		workFlowInst.setCurrentStateId(currentStateInst.getStateInstanceInfo().getStateId());
		_persistenceService.setWorkFlowInstance(workFlowInst);

		addMapIndexOfRoleWorkFlow(workFlowMeta, workFlowInst.getId());
		
		//State
		_persistenceService.setStateInstance(currentStateInst);
		addMapIndexOfRoleCurrentState(currentStateMeta, currentStateInst.getStateInstanceInfo().getStateId());

		//trace
		saveTrace(workFlowInst, currentStateInst);
	}

	private void saveWorkFlowStateOnMoveToNextState(RoleUser user, 
			WorkFlow workFlowMeta, WorkFlowInstance workFlowInst,
			State currentStateMeta, StateInstance currentStateInst,
			State nextStateMeta, StateInstance nextStateInst) 
			throws PersistenceException {
		//WorkFlow
		workFlowInst.setCurrentStateId(nextStateInst.getStateInstanceInfo().getStateId());
		_persistenceService.setWorkFlowInstance(workFlowInst);
		
		//State
		_persistenceService.setStateInstance(currentStateInst);
		_persistenceService.setStateInstance(nextStateInst);
		deleteMapIndexOfRoleCurrentState(currentStateMeta, currentStateInst.getStateInstanceInfo().getStateId());
		addMapIndexOfRoleCurrentState(nextStateMeta, nextStateInst.getStateInstanceInfo().getStateId());

		//trace
		saveTrace(workFlowInst, nextStateInst);
	}
	
	private void saveTrace(WorkFlowInstance workFlowInst, StateInstance currentStateInst) throws PersistenceException {
		WorkFlowInstance rootWorkFlowInst = findRootWorkFlow(workFlowInst);

		//The trace instance is corresponding to the root workflow.
		WorkFlowTraceInstance traceInst = _persistenceService.getWorkFlowTraceInstance(rootWorkFlowInst.getId());
		if(traceInst == null) {
			traceInst = new WorkFlowTraceInstance();
			traceInst.setWorkFlowId(rootWorkFlowInst.getId());
		}
		StateTraceData trace = new StateTraceData();
		trace.setWorkFlowId(workFlowInst.getId());
		trace.setParentFlowId(workFlowInst.getParentFlowId());
		trace.setStateId(currentStateInst.getStateInstanceInfo().getStateId());
		
		traceInst.getStateTraceDatas().add(trace);
		_persistenceService.setWorkFlowTraceInstance(traceInst);
	}
	
	private WorkFlowInstance findRootWorkFlow(WorkFlowInstance workFlowInst) throws PersistenceException {
		WorkFlowInstance workFlowRootInst = workFlowInst;
		WorkFlowInstance workFlowInstTmp = workFlowInst;

		while(workFlowInstTmp != null) {
			workFlowRootInst = workFlowInstTmp;
			
			if(workFlowInstTmp.getParentFlowId() <= 0) {
				break;
			}

			workFlowInstTmp = _persistenceService.getWorkFlowInstance(workFlowInstTmp.getParentFlowId());
		}
		
		return workFlowRootInst;
	}

	private void addMapIndexOfRoleWorkFlow(WorkFlow workFlowMeta, long workFlowId) throws PersistenceException {
		for(int i = 0; i < workFlowMeta.getAllRoles().size(); i++) {
			_persistenceService.addMapIndexOfRoleWorkFlow(
					workFlowMeta.getAllRoles().get(i).getName(), 
					workFlowMeta.getName(), 
					workFlowId);
		}
	}

	private void addMapIndexOfRoleCurrentState(State stateMeta, long stateId) throws PersistenceException {
		for(int i = 0; i < stateMeta.getAccessibleRoles().size(); i++) {
			_persistenceService.addMapIndexOfRoleCurrentState(
					stateMeta.getAccessibleRoles().get(i).getName(), 
					stateId);
		}
	}
	
	private void deleteMapIndexOfRoleCurrentState(State stateMeta, long stateId) throws PersistenceException {
		for(int i = 0; i < stateMeta.getAccessibleRoles().size(); i++) {
			_persistenceService.deleteMapIndexOfRoleCurrentState(
					stateMeta.getAccessibleRoles().get(i).getName(), stateId);
		}
	}

	protected class WorkFlowInstanceStatusFilter implements IWorkFlowInstanceFilter {
		private int _targetStatus = 0; 
		
		public WorkFlowInstanceStatusFilter(int targetStatus) {
			_targetStatus = targetStatus;
		}
		
		@Override
		public boolean accept(WorkFlowInstance workFlowInst) {
			if(_targetStatus == workFlowInst.getCurrentWorkFlowStatus()) {
				return true;
			} else {
				return false;
			}
		}
	}
	
}
