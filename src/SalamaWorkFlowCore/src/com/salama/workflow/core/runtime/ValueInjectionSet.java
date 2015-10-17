package com.salama.workflow.core.runtime;

import java.util.ArrayList;
import java.util.List;

public class ValueInjectionSet {
	List<ValueInjection> _valueInjectionSet = new ArrayList<ValueInjection>();

	public List<ValueInjection> getValueInjectionSet() {
		return _valueInjectionSet;
	}

	public void setValueInjectionSet(List<ValueInjection> valueInjectionSet) {
		_valueInjectionSet = valueInjectionSet;
	}
	
}
