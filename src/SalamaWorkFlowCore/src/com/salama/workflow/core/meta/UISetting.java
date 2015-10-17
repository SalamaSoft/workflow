package com.salama.workflow.core.meta;

import com.salama.workflow.core.util.BeanUtil;
import com.salama.workflow.core.util.Clonable;

public class UISetting implements Clonable<UISetting> {
	private String _name = "";
	
	private String _value = "";

	@Override
	public UISetting cloneData() {
		try {
			return (UISetting) BeanUtil.simpleDataCopy(this);
		} catch(Exception e) {
			return null;
		}
	}
	
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getValue() {
		return _value;
	}

	public void setValue(String value) {
		_value = value;
	}
	
}
