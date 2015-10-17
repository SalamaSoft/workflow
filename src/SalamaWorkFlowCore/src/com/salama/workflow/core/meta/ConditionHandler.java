package com.salama.workflow.core.meta;

import com.salama.workflow.core.util.BeanUtil;
import com.salama.workflow.core.util.Clonable;

public class ConditionHandler implements Clonable<ConditionHandler> {
	
	private String _handlerClass = "";
	
	private String _descrption = "HandlerClass must implement com.salama.workflow.core.runtime.IConditionHandler. And the method which will be invoked is the method: boolean handleCondition(StateInstance state)";

	@Override
	public ConditionHandler cloneData() {
		try {
			return (ConditionHandler) BeanUtil.simpleDataCopy(this);
		} catch(Exception e) {
			return null;
		}
	}

	public String getHandlerClass() {
		return _handlerClass;
	}

	public void setHandlerClass(String handlerClass) {
		_handlerClass = handlerClass;
	}

	public String getDescrption() {
		return _descrption;
	}

	public void setDescrption(String descrption) {
		_descrption = descrption;
	}
	
	
}
