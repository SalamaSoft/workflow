package com.salama.workflow.core.meta;

import com.salama.workflow.core.util.BeanUtil;
import com.salama.workflow.core.util.Clonable;

/**
 * Setting of role
 * @author Lenovo
 *
 */
public class Role implements Clonable<Role>
{
    private String _name = "";

    private String _description = "";

    @Override
    public Role cloneData() {
		try {
			return (Role) BeanUtil.simpleDataCopy(this);
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


}
