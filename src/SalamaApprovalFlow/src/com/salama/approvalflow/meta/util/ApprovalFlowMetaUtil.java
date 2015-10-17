package com.salama.approvalflow.meta.util;

import java.util.List;

import com.salama.approvalflow.meta.designer.ApprovalActivity;
import com.salama.approvalflow.meta.designer.ApprovalActivityNames;
import com.salama.approvalflow.meta.designer.ApprovalFlow;
import com.salama.approvalflow.meta.designer.ApprovalState;
import com.salama.approvalflow.meta.designer.ApprovalTransitionNames;
import com.salama.approvalflow.runtime.application.ApprovalApp;
import com.salama.workflow.core.meta.Activity;
import com.salama.workflow.core.meta.Application;
import com.salama.workflow.core.meta.Condition;
import com.salama.workflow.core.meta.Event;
import com.salama.workflow.core.meta.Param;
import com.salama.workflow.core.meta.Role;
import com.salama.workflow.core.meta.SetValue;
import com.salama.workflow.core.meta.State;
import com.salama.workflow.core.meta.Transition;
import com.salama.workflow.core.meta.WorkFlow;
import com.salama.workflow.core.persistence.StateInstance;

public class ApprovalFlowMetaUtil {
	private ApprovalFlowMetaUtil() {
	}
	
	public static WorkFlow approvalFlowToMeta(ApprovalFlow approvalFlow) {
		WorkFlow workFlowMeta = new WorkFlow();
		
		workFlowMeta.setName(approvalFlow.getName());
		workFlowMeta.setDescription(approvalFlow.getDescription());
		workFlowMeta.setAllRoles(approvalFlow.getAllRoles());
		
		//states
		State state = null;
		State startState = null;
		
		for(int i = 0; i < approvalFlow.getAllStates().size(); i++) {
			state = approvalStateToMeta(approvalFlow.getAllStates().get(i));
			workFlowMeta.getAllStates().add(state);

			if(i == 0) {
				startState = state;
			}
		}
		
		workFlowMeta.setStartState(startState.getName());
		
		return workFlowMeta;
	}
	
	public static State approvalStateToMeta(ApprovalState approvalState) {
		State state = new State();
		
		state.setName(approvalState.getName());
		state.setDescription(approvalState.getDescription());
		state.setAccessibleRoles(approvalState.getAccessibleRoles());
		state.setStateData(approvalState.getStateData());
		state.setStateUserDefineSetting(approvalState.getStateUserDefineSetting());
		state.setStateDataUISetting(approvalState.getStateDataUISetting());
		state.setStateType(approvalState.getStateType());
		
		ApprovalActivity approvalActivity = null;
		Activity activity = null;
		Event event = null;
		
		for(int i =0; i < approvalState.getActivityList().size(); i++) {
			approvalActivity = approvalState.getActivityList().get(i);
			activity = approvalActivityToMeta(state.getName(), approvalState.getAccessibleRoles(), approvalActivity);
			state.getActivities().add(activity);
			
			event = new Event();
			event.setActivityName(activity.getName());
			event.setName(activity.getName());
			
			state.getActivityEvents().add(event);
		}
		
		return state;
	}
	
	public static Activity approvalActivityToMeta(String currentStateName, List<Role> accessibleRoles, ApprovalActivity approvalActivity) {
		Activity activity = new Activity();
		
		activity.setName(approvalActivity.getName());
		activity.setDescription(approvalActivity.getDescription());
		activity.setAccessibleRoles(accessibleRoles);

		Application app = new Application();
		app.setName(approvalActivity.getName());
		app.setDescription(approvalActivity.getDescription());
		app.setApplicationClass(ApprovalApp.class.getName());
		Param param = new Param();
		//stateInstance
		param.setValueExpression("#this");
		param.setParamType(StateInstance.class.getName());
		app.getParams().add(param);
		if(approvalActivity.getName().equals(ApprovalActivityNames.Approve)) {
			app.setMethodName("approve");
		} else if(approvalActivity.getName().equals(ApprovalActivityNames.Disapprove)) {
			app.setMethodName("disapprove");
		}
		
		activity.setApplication(app);

		{
			//Move to next
			Transition transitionMoveToNext = new Transition();
			transitionMoveToNext.setName(ApprovalTransitionNames.MoveToNextState);
			
			SetValue setValue = new SetValue();
			setValue.setSrcExpression("stateData");
			setValue.setDestExpression("stateData");
			transitionMoveToNext.getSetValueToToState().add(setValue);
			transitionMoveToNext.setToState(approvalActivity.getToState());
			
			Condition conditionMoveToNext = new Condition();
			conditionMoveToNext.setBoolExpression(approvalActivity.getBoolExpressionForMoveToNext());
			conditionMoveToNext.getConditionHandler().setHandlerClass(approvalActivity.getCondtionHandlerClass());
			transitionMoveToNext.setConditionExpression(conditionMoveToNext);
	
			activity.getTransitions().add(transitionMoveToNext);
		}
		
		//Stay on current state
		{
			Transition transitionStayOnCurrentState = new Transition();
			transitionStayOnCurrentState.setName(ApprovalTransitionNames.StayOnCurrentState);
			
			SetValue setValueStayOnCurrentState = new SetValue();
			setValueStayOnCurrentState.setSrcExpression("stateData");
			setValueStayOnCurrentState.setDestExpression("stateData");
			transitionStayOnCurrentState.getSetValueToToState().add(setValueStayOnCurrentState);
			
			transitionStayOnCurrentState.setToState(currentStateName);
			
			Condition conditionStayOnCurrentState = new Condition();
			conditionStayOnCurrentState.setBoolExpression("!(" + approvalActivity.getBoolExpressionForMoveToNext() + ")");
			conditionStayOnCurrentState.getConditionHandler().setHandlerClass(approvalActivity.getCondtionHandlerClass());
			
			transitionStayOnCurrentState.setConditionExpression(conditionStayOnCurrentState);

			activity.getTransitions().add(transitionStayOnCurrentState);
		}

		return activity;
	}

	public static ApprovalFlow metaToApprovalFlow(WorkFlow workFlowMeta) {
		ApprovalFlow approvalFlow = new ApprovalFlow();
		
		approvalFlow.setName(workFlowMeta.getName());
		approvalFlow.setDescription(workFlowMeta.getDescription());
		approvalFlow.setAllRoles(workFlowMeta.getAllRoles());
		
		//states
		ApprovalState approvalState = null;
		for(int i = 0; i < workFlowMeta.getAllStates().size(); i++) {
			approvalState = metaToApprovalState(workFlowMeta.getAllStates().get(i));
			approvalFlow.getAllStates().add(approvalState);
		}
		
		return approvalFlow;
	}
	
	public static ApprovalState metaToApprovalState(State state) {
		ApprovalState approvalState = new ApprovalState();
		
		approvalState.setName(state.getName());
		approvalState.setDescription(state.getDescription());
		approvalState.setAccessibleRoles(state.getAccessibleRoles());
		approvalState.setStateData(state.getStateData());
		approvalState.setStateUserDefineSetting(state.getStateUserDefineSetting());
		approvalState.setStateDataUISetting(state.getStateDataUISetting());
		approvalState.setStateType(state.getStateType());
		
		ApprovalActivity approvalActivity = null;
		Activity activity = null;
		
		for(int i =0; i < state.getActivities().size(); i++) {
			activity = state.getActivities().get(i);
			approvalActivity = metaToApprovalActivity(activity);

			approvalState.getActivityList().add(approvalActivity);
		}
		
		return approvalState;
	}
	
	public static ApprovalActivity metaToApprovalActivity(Activity activity) {
		ApprovalActivity approvalActivity = new ApprovalActivity();
		
		approvalActivity.setName(activity.getName());
		approvalActivity.setDescription(activity.getDescription());
		if(activity.getTransitions().size() > 0) {
			for(Transition transition: activity.getTransitions()) {
				if(transition.getName().equals(ApprovalTransitionNames.MoveToNextState)) {
					approvalActivity.setToState(transition.getToState());
					approvalActivity.setBoolExpressionForMoveToNext(transition.getConditionExpression().getBoolExpression());
					if(transition.getConditionExpression().getConditionHandler() != null) {
						approvalActivity.setCondtionHandlerClass(transition.getConditionExpression().getConditionHandler().getHandlerClass());
					}
					break;
				}
			}
		}

		return approvalActivity;
	}
	
}
