package com.salama.workflow.core.meta;

import com.salama.workflow.core.util.BeanUtil;
import com.salama.workflow.core.util.Clonable;

/**
 * Set the value to the property of the name
 * @author liuxg
 *
 */
public class SetValue  implements Clonable<SetValue> {
	/**
	 * OGNL Expression of target which is set to
	 */
	private String _destExpression = "";
	
	/**
	 * OGNL Expression of parameter 
	 */
	private String _srcExpression = "";

	@Override
	public SetValue cloneData() {
		try {
			return (SetValue) BeanUtil.simpleDataCopy(this);
		} catch(Exception e) {
			return null;
		}
	}

	public String getDestExpression() {
		return _destExpression;
	}

	public void setDestExpression(String destExpression) {
		_destExpression = destExpression;
	}

	public String getSrcExpression() {
		return _srcExpression;
	}

	public void setSrcExpression(String srcExpression) {
		_srcExpression = srcExpression;
	}

	
}
