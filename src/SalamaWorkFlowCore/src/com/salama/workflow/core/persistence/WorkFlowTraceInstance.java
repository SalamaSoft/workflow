package com.salama.workflow.core.persistence;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import MetoXML.XmlDeserializer;
import MetoXML.XmlSerializer;
import MetoXML.Base.XmlParseException;

import com.salama.workflow.core.error.XmlSerializeException;
import com.salama.workflow.core.util.BeanUtil;
import com.salama.workflow.core.util.XmlSerializable;

public class WorkFlowTraceInstance implements XmlSerializable {
	private final static Logger logger = Logger.getLogger(WorkFlowTraceInstance.class);
	
	private long _workFlowId = 0;
	
	private List<StateTraceData> _stateTraceDatas = new ArrayList<StateTraceData>();

	@Override
	public void loadXml(String xml) throws XmlSerializeException {
    	try {
    		WorkFlowTraceInstance data = (WorkFlowTraceInstance) XmlDeserializer.stringToObject(xml, WorkFlowTraceInstance.class);
			BeanUtil.simpleDataValueCopy(data, this);
		} catch (IOException e) {
			logger.error("loadXml", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (XmlParseException e) {
			logger.error("loadXml", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (InvocationTargetException e) {
			logger.error("loadXml", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (IllegalAccessException e) {
			logger.error("loadXml", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (InstantiationException e) {
			logger.error("loadXml", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (NoSuchMethodException e) {
			logger.error("loadXml", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (IntrospectionException e) {
			logger.error("loadXml", e);
			throw new XmlSerializeException("loadXml()", e);
		}
	}
	
	@Override
	public String toXml() throws XmlSerializeException {
    	try {
			return XmlSerializer.objectToString(this, WorkFlowTraceInstance.class, true);
		} catch (IntrospectionException e) {
			logger.error("toXml()", e);
			throw new XmlSerializeException("toXml()", e);
		} catch (IllegalAccessException e) {
			logger.error("toXml()", e);
			throw new XmlSerializeException("toXml()", e);
		} catch (InvocationTargetException e) {
			logger.error("toXml()", e);
			throw new XmlSerializeException("toXml()", e);
		} catch (IOException e) {
			logger.error("toXml()", e);
			throw new XmlSerializeException("toXml()", e);
		}
	}
	
	
	
	public long getWorkFlowId() {
		return _workFlowId;
	}

	public void setWorkFlowId(long workFlowId) {
		_workFlowId = workFlowId;
	}

	public List<StateTraceData> getStateTraceDatas() {
		return _stateTraceDatas;
	}

	public void setStateTraceDatas(List<StateTraceData> stateTraceDatas) {
		_stateTraceDatas = stateTraceDatas;
	}
	
}
