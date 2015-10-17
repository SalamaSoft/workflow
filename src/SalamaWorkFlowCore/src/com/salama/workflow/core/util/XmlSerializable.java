package com.salama.workflow.core.util;

import com.salama.workflow.core.error.XmlSerializeException;

public interface XmlSerializable {
	public String toXml() throws XmlSerializeException;
	
	public void loadXml(String xml) throws XmlSerializeException;
}
