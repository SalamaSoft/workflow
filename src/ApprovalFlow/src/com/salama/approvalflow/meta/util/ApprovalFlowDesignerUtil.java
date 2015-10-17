package com.salama.approvalflow.meta.util;

import java.util.List;

import com.salama.approvalflow.meta.designer.ApprovalActivity;
import com.salama.approvalflow.meta.designer.ApprovalActivityNames;
import com.salama.approvalflow.meta.designer.ApprovalFlow;
import com.salama.approvalflow.meta.designer.ApprovalState;
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

public class ApprovalFlowDesignerUtil {
	private ApprovalFlowDesignerUtil() {
	}
	
	public static WorkFlow approvalFlowToMeta(ApprovalFlow approvalFlow) {
		WorkFlow workFlowMeta = new WorkFlow();
		
		workFlowMeta.setName(approvalFlow.getName());
		workFlowMeta.setAllRoles(approvalFlow.getAllRoles());
		
		//states
		State state = null;
		for(int i = 0; i < approvalFlow.getAllStates().size(); i++) {
			state = approvalStateToMeta(approvalFlow.getAllStates().get(i));
			workFlowMeta.getAllStates().add(state);
		}
		
		return workFlowMeta;
	}
	
	public static State approvalStateToMeta(ApprovalState approvalState) {
		State state = new State();
		
		state.setName(approvalState.getName());
		state.setAccessibleRoles(approvalState.getAccessibleRoles());
		state.setStateData(approvalState.getStateData());
		state.setStateDataUISetting(approvalState.getStateDataUISetting());
		state.setStateType(approvalState.getStateType());
		
		ApprovalActivity approvalActivity = null;
		Activity activity = null;
		Event event = null;
		
		for(int i =0; i < approvalState.getActivityList().size(); i++) {
			approvalActivity = approvalState.getActivityList().get(i);
			activity = approvalActivityToMeta(approvalState.getAccessibleRoles(), approvalActivity);
			state.getActivities().add(activity);
			
			event = new Event();
			event.setActivityName(activity.getName());
			event.setName(activity.getName());
			
			state.getActivityEvents().add(event);
		}
		
		return state;
	}
	
	public static Activity approvalActivityToMeta(List<Role> accessibleRoles, ApprovalActivity approvalActivity) {
		Activity activity = new Activity();
		
		activity.setName(approvalActivity.getName());
		activity.setAccessibleRoles(accessibleRoles);

		Application app = new Application();
		app.setName(approvalActivity.getName());
		app.setApplicationClass(ApprovalApp.class.getName());
		Param param = new Param();
		//stateInstance
		param.setValueExpression("#this");
		param.setParamType(StateInstance.class.getName());
		app.getParams().add(param);
		
		activity.setApplication(app);

		Transition transition = new Transition();
		
		SetValue setValue = new SetValue();
		setValue.setSrcExpression("stateData");
		setValue.setDestExpression("stateData");
		transition.getSetValueToToState().add(setValue);
		transition.setToState(approvalActivity.getToState());
		
		Condition condition = new Condition();
		if(approvalActivity.getName().equals(ApprovalActivityNames.Approve)) {
			app.setMethodName("approve");
			condition.setBoolExpression("stateData.isCurrentStateApproved()");
		} else if(approvalActivity.getName().equals(ApprovalActivityNames.Disapprove)) {
			app.setMethodName("disapprove");
			condition.setBoolExpression("stateData.disapprove()");
		}
		
		transition.setConditionExpression(condition);
		
		activity.getTransitions().add(transition);
		
		return activity;
	}

	public static ApprovalFlow metaToApprovalFlow(WorkFlow workFlowMeta) {
		ApprovalFlow approvalFlow = new ApprovalFlow();
		
		approvalFlow.setName(workFlowMeta.getName());
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
		approvalState.setAccessibleRoles(state.getAccessibleRoles());
		approvalState.setStateData(state.getStateData());
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
		if(activity.getTransitions().size() > 0) {
			approvalActivity.setToState(activity.getTransitions().get(0).getToState());
		}

		return approvalActivity;
	}
	
}
