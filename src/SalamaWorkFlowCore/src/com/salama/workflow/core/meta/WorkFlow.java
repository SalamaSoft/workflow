package com.salama.workflow.core.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.util.Clonable;

public class WorkFlow implements Clonable<WorkFlow>
{
    private String _name = "";

    private String _description = "";

    private String _author = "";

    private String _version = "0";

//    private String _characterSet = "UTF-8";

    private String _validBeginDate = "19770101";

    private String _validEndDate = "99990101";

    private int _priority = 0;

    private String _documentationUrl = "";

    private String _startState = "";

    private List<State> _allStates = new ArrayList<State>();

    private List<Role> _allRoles = new ArrayList<Role>();
    
    public WorkFlow()
    { 
    }

    @Override
    public WorkFlow cloneData() {
    	WorkFlow data = new WorkFlow();
    	
    	data._name = this._name;
    	data._description = this._description;
    	data._author = this._author;
    	data._version = this._version;
//    	data._characterSet = this._characterSet;
    	data._validBeginDate = this._validBeginDate;
    	data._validEndDate = this._validEndDate;
    	data._priority = this._priority;
    	data._documentationUrl = this._documentationUrl;
    	data._startState = this._startState;
    	
    	int i;
    	for(i = 0; i < this._allStates.size(); i++) {
    		data._allStates.add(this._allStates.get(i).cloneData());
    	}
    	
    	for(i = 0; i < this._allRoles.size(); i++) {
    		data._allRoles.add(this._allRoles.get(i).cloneData());
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

	public String getAuthor() {
		return _author;
	}

	public void setAuthor(String author) {
		_author = author;
	}

	public String getVersion() {
		return _version;
	}

	public void setVersion(String version) {
		_version = version;
	}

	/*
	public String getCharacterSet() {
		return _characterSet;
	}

	public void setCharacterSet(String characterSet) {
		_characterSet = characterSet;
	}
	*/

	public int getPriority() {
		return _priority;
	}

	public void setPriority(int priority) {
		_priority = priority;
	}

	public String getDocumentationUrl() {
		return _documentationUrl;
	}

	public void setDocumentationUrl(String documentationUrl) {
		_documentationUrl = documentationUrl;
	}

	public List<State> getAllStates() {
		return _allStates;
	}

	public void setAllStates(List<State> allStates) {
		_allStates = allStates;
	}

	public List<Role> getAllRoles() {
		return _allRoles;
	}

	public void setAllRoles(List<Role> allRoles) {
		_allRoles = allRoles;
	}

	public String getStartState() {
		return _startState;
	}

	public void setStartState(String startState) {
		_startState = startState;
	}

	public String getValidBeginDate() {
		return _validBeginDate;
	}

	public void setValidBeginDate(String validBeginDate) {
		_validBeginDate = validBeginDate;
	}

	public String getValidEndDate() {
		return _validEndDate;
	}

	public void setValidEndDate(String validEndDate) {
		_validEndDate = validEndDate;
	}
    
}
