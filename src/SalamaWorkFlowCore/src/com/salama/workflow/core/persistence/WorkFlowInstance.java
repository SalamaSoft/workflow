package com.salama.workflow.core.persistence;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.log4j.Logger;

import MetoXML.XmlDeserializer;
import MetoXML.XmlSerializer;
import MetoXML.Base.XmlParseException;

import com.salama.workflow.core.error.XmlSerializeException;
import com.salama.workflow.core.util.BeanUtil;
import com.salama.workflow.core.util.XmlSerializable;


public class WorkFlowInstance implements XmlSerializable {
	private final static Logger logger = Logger.getLogger(WorkFlowInstance.class);
	/**
	 * Initiated: Just initiated
	 * Running: Running on normal status and active
	 * Suspended: Suspended by admin
	 * Terminated: Terminated by flow running normally 
	 * Completed:Flow normally completed 
	 * @author liuxg
	 *
	 */
	//public final static int WORK_FLOW_STATUS_INITIATED = 0; 
	public final static int WORK_FLOW_STATUS_RUNNING = 1; 
	//public final static int WORK_FLOW_STATUS_SUSPENDED = 2; 
	public final static int WORK_FLOW_STATUS_TERMINATED = 3; 
	public final static int WORK_FLOW_STATUS_COMPLETED = 4; 
	
	private long _id = 0;

	private String _workFlowName = "";
	
	/**
	 * This workflow is a subflow if _parentFlowId is not 0;
	 */
	private long _parentFlowId = 0;
	
	/**
	 * If this work flow is a sub flow, then _parentActivityName is the activity name where sub flow was created from. 
	 */
	private String _parentActivityName = "";
	
	private int _currentWorkFlowStatus = WORK_FLOW_STATUS_RUNNING;
	
	private long _currentStateId = 0;
	
    private Date _creationTime = new Date();
	
    public WorkFlowInstance() {
    }
    
    @Override
    public void loadXml(String xml) throws XmlSerializeException {
    	try {
			WorkFlowInstance data = (WorkFlowInstance) XmlDeserializer.stringToObject(xml, WorkFlowInstance.class);
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
			return XmlSerializer.objectToString(this, WorkFlowInstance.class, true);
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
    
	public long getId() {
		return _id;
	}

	public void setId(long id) {
		_id = id;
	}

	public long getParentFlowId() {
		return _parentFlowId;
	}

	public void setParentFlowId(long parentFlowId) {
		_parentFlowId = parentFlowId;
	}

	public int getCurrentWorkFlowStatus() {
		return _currentWorkFlowStatus;
	}

	public void setCurrentWorkFlowStatus(int currentWorkFlowStatus) {
		_currentWorkFlowStatus = currentWorkFlowStatus;
	}

	public Date getCreationTime() {
		return _creationTime;
	}

	public void setCreationTime(Date creationTime) {
		_creationTime = creationTime;
	}

	public long getCurrentStateId() {
		return _currentStateId;
	}

	public void setCurrentStateId(long currentStateId) {
		_currentStateId = currentStateId;
	}

	public String getWorkFlowName() {
		return _workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		_workFlowName = workFlowName;
	}

	public String getParentActivityName() {
		return _parentActivityName;
	}

	public void setParentActivityName(String parentActivityName) {
		_parentActivityName = parentActivityName;
	}

	
}
