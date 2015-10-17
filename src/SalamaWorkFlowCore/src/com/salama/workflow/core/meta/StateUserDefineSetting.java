package com.salama.workflow.core.meta;

import java.util.ArrayList;
import java.util.List;

import com.salama.workflow.core.util.Clonable;

public class StateUserDefineSetting implements Clonable<StateUserDefineSetting> {
	private List<UserDefineSetting> _userDefineSettings = new ArrayList<UserDefineSetting>();

	@Override
	public StateUserDefineSetting cloneData() {
		StateUserDefineSetting setting = new StateUserDefineSetting();
		
		for(int i = 0; i < this._userDefineSettings.size(); i++) {
			setting._userDefineSettings.add(this._userDefineSettings.get(i).cloneData());
		}
		
		return setting;
	}

	public List<UserDefineSetting> getUserDefineSettings() {
		return _userDefineSettings;
	}

	public void setUserDefineSettings(List<UserDefineSetting> userDefineSettings) {
		_userDefineSettings = userDefineSettings;
	}

	
}
