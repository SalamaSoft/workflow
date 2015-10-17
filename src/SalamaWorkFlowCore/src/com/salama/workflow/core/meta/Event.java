package com.salama.workflow.core.meta;

import com.salama.workflow.core.util.BeanUtil;
import com.salama.workflow.core.util.Clonable;

public class Event implements Clonable<Event> {
	private String _name = "";
	
	private String _description = "";
	
	private String _activityName = "";

	@Override
	public Event cloneData() {
		try {
			return (Event) BeanUtil.simpleDataCopy(this);
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

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getActivityName() {
		return _activityName;
	}

	public void setActivityName(String activityName) {
		_activityName = activityName;
	}
	
	
}
