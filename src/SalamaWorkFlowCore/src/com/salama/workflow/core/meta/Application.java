package com.salama.workflow.core.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.util.Clonable;

public class Application implements Clonable<Application>
{
    private String _name = "";

    private String _description = "";

    private String _applicationClass = "";

    private String _methodName = "";

    private List<Param> _params = new ArrayList<Param>();
    
    /**
     * set return value to current state
     */
    private List<SetValue> _setReturnValueToState = new ArrayList<SetValue>();
    
    @Override
    public Application cloneData() {
    	Application data = new Application();
    	data._name = this._name;
    	data._description = this._description;
    	data._applicationClass = this._applicationClass;
    	data._methodName = this._methodName;

    	int i;
    	for(i = 0; i < this._params.size(); i++) {
    		data._params.add(this._params.get(i).cloneData());
    	}
    	
    	for(i = 0; i < this._setReturnValueToState.size(); i++) {
    		data._setReturnValueToState.add(this._setReturnValueToState.get(i).cloneData());
    	}

    	return data;
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

	public String getApplicationClass() {
		return _applicationClass;
	}

	public void setApplicationClass(String applicationClass) {
		_applicationClass = applicationClass;
	}

	public String getMethodName() {
		return _methodName;
	}

	public void setMethodName(String methodName) {
		_methodName = methodName;
	}

	public List<Param> getParams() {
		return _params;
	}

	public void setParams(List<Param> params) {
		_params = params;
	}

	public List<SetValue> getSetReturnValueToState() {
		return _setReturnValueToState;
	}

	public void setSetReturnValueToState(List<SetValue> setReturnValueToState) {
		_setReturnValueToState = setReturnValueToState;
	}

	
}
