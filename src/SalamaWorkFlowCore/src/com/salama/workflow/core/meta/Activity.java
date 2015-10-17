package com.salama.workflow.core.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.util.Clonable;

public class Activity implements Clonable<Activity>
{
    private String _name = "";

    private String _description = "";

    /**
     * Roles who can access this activity
     */
    private List<Role> _accessibleRoles = new ArrayList<Role>();

    /**
     * Url of the help document
     */
    private String _documentationUrl = "";

    /**
     * Sub work flow
     */
    private SubFlow _subFlow = new SubFlow();
    
    /**
     * Application which executes the logic
     */
    private Application _application = new Application();
    
    /**
     * Transitions
     */
    private List<Transition> _transitions = new ArrayList<Transition>();

	@Override
	public Activity cloneData() {
		Activity data = new Activity();
		
		data._name = this._name;
		data._description = this._description;
		data._documentationUrl = this._documentationUrl;
		data._application = this._application.cloneData();
		data._subFlow = this._subFlow.cloneData();
		
		int i;
		for(i = 0; i < this._accessibleRoles.size(); i++) {
			data._accessibleRoles.add(this._accessibleRoles.get(i).cloneData());
		}
		for(i = 0; i < this._transitions.size(); i++) {
			data._transitions.add(this._transitions.get(i).cloneData());
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


	public String getDocumentationUrl() {
		return _documentationUrl;
	}

	public void setDocumentationUrl(String documentationUrl) {
		_documentationUrl = documentationUrl;
	}

	public List<Role> getAccessibleRoles() {
		return _accessibleRoles;
	}

	public void setAccessibleRoles(List<Role> accessibleRoles) {
		_accessibleRoles = accessibleRoles;
	}

	public List<Transition> getTransitions() {
		return _transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		_transitions = transitions;
	}

	public Application getApplication() {
		return _application;
	}

	public void setApplication(Application application) {
		_application = application;
	}

	public SubFlow getSubFlow() {
		return _subFlow;
	}

	public void setSubFlow(SubFlow subFlow) {
		_subFlow = subFlow;
	}
    
}
