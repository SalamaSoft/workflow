package com.salama.workflow.core.meta;

import com.salama.workflow.core.util.BeanUtil;
import com.salama.workflow.core.util.Clonable;

public class Param implements Clonable<Param> {
	private String _valueExpression = "";
	
	private String _description = "";
	
	private String _paramType = "";
	
	@Override
	public Param cloneData() {
		try {
			return (Param) BeanUtil.simpleDataCopy(this);
		} catch(Exception e) {
			return null;
		}
	}
	
	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getParamType() {
		return _paramType;
	}

	public void setParamType(String paramType) {
		_paramType = paramType;
	}

	public String getValueExpression() {
		return _valueExpression;
	}

	public void setValueExpression(String valueExpression) {
		_valueExpression = valueExpression;
	}
	
	
}
