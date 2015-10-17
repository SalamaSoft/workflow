package com.salama.workflow.core.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.util.Clonable;

public class Transition  implements Clonable<Transition>
{
	private String _name = "";
	
	private String _description = "";
	
    private Condition _conditionExpression = new Condition();
    
    private String _toState = "";

    private List<SetValue> _setValueToToState = new ArrayList<SetValue>();

    @Override
    public Transition cloneData() {
    	Transition data = new Transition();
    	data._name = this._name;
    	data._description = this._description;
    	data._conditionExpression = this._conditionExpression.cloneData();
    	data._toState = this._toState;
    	
    	for(int i = 0; i < this._setValueToToState.size(); i++) {
    		data._setValueToToState.add(this._setValueToToState.get(i).cloneData());
    	}
    	
    	return data;
    }
    
	public String getToState() {
		return _toState;
	}

	public void setToState(String toState) {
		_toState = toState;
	}

	public Condition getConditionExpression() {
		return _conditionExpression;
	}

	public void setConditionExpression(Condition conditionExpression) {
		_conditionExpression = conditionExpression;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public List<SetValue> getSetValueToToState() {
		return _setValueToToState;
	}

	public void setSetValueToToState(List<SetValue> setValueToToState) {
		_setValueToToState = setValueToToState;
	}
	    
}
