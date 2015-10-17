package com.salama.workflow.core.meta;

import com.salama.workflow.core.util.BeanUtil;
import com.salama.workflow.core.util.Clonable;

/**
 * Setting of data type
 * @author Lenovo
 *
 */
public class RelevantData implements Clonable<RelevantData>
{
    /**
     * Data class(Full name)
     */
    private String _dataClass = "";

    private String _description = "";

    @Override
    public RelevantData cloneData() {
		try {
			return (RelevantData) BeanUtil.simpleDataCopy(this);
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

	public String getDataClass() {
		return _dataClass;
	}

	public void setDataClass(String dataClass) {
		_dataClass = dataClass;
	}
}
