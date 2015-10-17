package com.salama.workflow.core.runtime;

public class ValueInjection {
	private String _targetExpression = "";
	
	private Object _value = null;


	public String getTargetExpression() {
		return _targetExpression;
	}

	public void setTargetExpression(String targetExpression) {
		_targetExpression = targetExpression;
	}

	public Object getValue() {
		return _value;
	}

	public void setValue(Object value) {
		_value = value;
	}
	
	
}
