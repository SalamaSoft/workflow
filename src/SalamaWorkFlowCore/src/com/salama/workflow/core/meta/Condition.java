package com.salama.workflow.core.meta;

import com.salama.workflow.core.util.Clonable;

public class Condition implements Clonable<Condition>{
	private String _boolExpression = "true";

	private ConditionHandler _conditionHandler = new ConditionHandler();
	
	private String _descrption = "";
	
	@Override
	public Condition cloneData() {
		try {
			Condition newData = new Condition();
			
			newData._boolExpression = this._boolExpression;
			newData._descrption = this._descrption;
			
			if(this._conditionHandler == null) {
				newData._conditionHandler = null;
			} else {
				newData._conditionHandler.setHandlerClass(this._conditionHandler.getHandlerClass());
				newData._conditionHandler.setDescrption(this._conditionHandler.getDescrption());
			}
			
			return newData;
		} catch(Exception e) {
			return null;
		}
	}

	public String getBoolExpression() {
		return _boolExpression;
	}

	public void setBoolExpression(String boolExpression) {
		_boolExpression = boolExpression;
	}

	public String getDescrption() {
		return _descrption;
	}

	public void setDescrption(String descrption) {
		_descrption = descrption;
	}

	public ConditionHandler getConditionHandler() {
		return _conditionHandler;
	}

	public void setConditionHandler(ConditionHandler conditionHandler) {
		_conditionHandler = conditionHandler;
	}
	
	
}
