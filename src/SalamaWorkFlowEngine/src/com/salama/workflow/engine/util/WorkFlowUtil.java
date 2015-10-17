package com.salama.workflow.engine.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;
import MetoXML.Util.ClassFinder;

import com.salama.reflect.MethodInvokeUtil;
import com.salama.workflow.core.IWorkFlowEngine;
import com.salama.workflow.core.meta.Activity;
import com.salama.workflow.core.meta.Application;
import com.salama.workflow.core.meta.Condition;
import com.salama.workflow.core.meta.SetValue;
import com.salama.workflow.core.meta.State;
import com.salama.workflow.core.meta.Transition;
import com.salama.workflow.core.meta.WorkFlow;
import com.salama.workflow.core.persistence.StateInstance;
import com.salama.workflow.core.runtime.IConditionHandler;
import com.salama.workflow.core.runtime.ValueInjection;
import com.salama.workflow.core.runtime.ValueInjectionSet;

public class WorkFlowUtil {
	private WorkFlowUtil() {
		
	}
	
	public static State getMetaDataState(WorkFlow workFlowMeta, String stateName) {
		State data = null;
		
		for(int i = 0; i < workFlowMeta.getAllStates().size(); i++) {
			data = workFlowMeta.getAllStates().get(i);
			if(data.getName().equals(stateName)) {
				return data;
			}
		}
		
		return data;
	}
	
	public static void setMetaDataState(WorkFlow workFlowMeta, State stateMeta) {
		State stateTmp = null;
		
		for(int i = 0; i < workFlowMeta.getAllStates().size(); i++) {
			stateTmp = workFlowMeta.getAllStates().get(i);
			if(stateTmp.getName().equals(stateMeta.getName())) {
				workFlowMeta.getAllStates().set(i, stateMeta);
			}
		}
	}

	public static void setValueToState(StateInstance state, String expression, Object value) throws OgnlException {
		Ognl.setValue(expression, state, value);
	}
	
	public static Object getValueFromState(StateInstance state, String expression) throws OgnlException {
		return Ognl.getValue(expression, state);
	}
	
	public static void injectValueFromStateToState(StateInstance srcState, StateInstance destState, List<SetValue> setValues) throws OgnlException {
		SetValue setValue = null;
		Object srcValue = null;
		
		for(int i = 0; i < setValues.size(); i++) {
			setValue = setValues.get(i);
			
			srcValue = Ognl.getValue(setValue.getSrcExpression(), srcState);
			Ognl.setValue(setValue.getDestExpression(), destState, srcValue);
		}
	}
	
	public static void injectValueToState(StateInstance stateInst, ValueInjectionSet injectSet) throws OgnlException {
		if(injectSet == null) {
			return;
		}
		
		ValueInjection injection = null;
		
		for(int i = 0; i < injectSet.getValueInjectionSet().size(); i++) {
			injection = injectSet.getValueInjectionSet().get(i);
			
			Ognl.setValue(injection.getTargetExpression(), stateInst, injection.getValue());
		}
	}
	
	public static Transition doActivityOfApplicationType(
			IWorkFlowEngine workFlowEngine,
			Activity activity, StateInstance state, 
			ClassFinder applicationClassFinder, ClassFinder dataClassFinder) 
					throws OgnlException, IllegalAccessException, InstantiationException, InvocationTargetException, 
					ClassNotFoundException, OgnlException, NoSuchMethodException {
		executeApplication(activity, state, applicationClassFinder, dataClassFinder);
		
		Transition transition = null;
		Condition condition = null;
		Boolean boolVal = false;
		
		for(int i = 0; i < activity.getTransitions().size(); i++) {
			transition = activity.getTransitions().get(i);
			condition = transition.getConditionExpression();
			
			if((condition.getConditionHandler() != null) 
					&& (condition.getConditionHandler().getHandlerClass() != null)
					&& (condition.getConditionHandler().getHandlerClass().trim().length() > 0)
					) {
				Class<?> handlerClass = applicationClassFinder.findClass(
						condition.getConditionHandler().getHandlerClass().trim());
				IConditionHandler conditionHandler = (IConditionHandler) handlerClass.newInstance();
				boolVal = conditionHandler.handleCondition(workFlowEngine, state, activity, transition);
			} else {
				boolVal = (Boolean) WorkFlowUtil.getValueFromState(state, condition.getBoolExpression());
			}
			
			if(boolVal) {
				return transition;
			}
		}
		
		return null;
	}
	
	/**
	 * Return value of the application is ignored.
	 * @param activity
	 * @param application
	 * @param stateData
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 */
	protected static void executeApplication(Activity activity, StateInstance state, 
			ClassFinder applicationClassFinder, ClassFinder dataClassFinder) 
			throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException, OgnlException, NoSuchMethodException
	{
		int i;
		
		Application application = activity.getApplication();
		Class<?> appClass = applicationClassFinder.findClass(application.getApplicationClass());
		
		String[] paramTypes = new String[application.getParams().size()];
		for(i = 0; i < application.getParams().size(); i++) {
			paramTypes[i] = application.getParams().get(i).getParamType();
		}
		
		Object[] paramValues = new Object[application.getParams().size()];
		for(i = 0; i < application.getParams().size(); i++) {
			paramValues[i] = getValueFromState(state, application.getParams().get(i).getValueExpression());
		}
		
		Method method = MethodInvokeUtil.GetMethod(
				appClass, application.getMethodName(), paramTypes, dataClassFinder);
		Object app = appClass.newInstance();
		Object returnValue = MethodInvokeUtil.InvokeMethod(app, method, paramValues);
		
		//return value to state
		if((application.getSetReturnValueToState().size() > 0) && (returnValue != null)) {
			SetValue setValue = null;
			Object valueTmp = null;
			
			for(i = 0; i < application.getSetReturnValueToState().size(); i++) {
				setValue = application.getSetReturnValueToState().get(i);
				valueTmp = Ognl.getValue(setValue.getSrcExpression(), returnValue);
				setValueToState(state, setValue.getDestExpression(), valueTmp);
			}
		}
	}
	
	public static Activity getActivityByEvent(State stateMeta, String eventName) {
		String activityName = null;
		int i;
		for(i = 0; i < stateMeta.getActivityEvents().size(); i++) {
			if(stateMeta.getActivityEvents().get(i).getName().equals(eventName)) {
				activityName = stateMeta.getActivityEvents().get(i).getActivityName();
				break;
			}
		}
		
		return getActivityByActivityName(stateMeta, activityName);
	}
	
	public static Activity getActivityByActivityName(State stateMeta, String activityName) {
		if(activityName == null) {
			return null;
		} else {
			for(int i = 0; i < stateMeta.getActivities().size(); i++) {
				if(stateMeta.getActivities().get(i).getName().equals(activityName)) {
					return stateMeta.getActivities().get(i);
				}
			}
			return null;
		}
	}
	
}
