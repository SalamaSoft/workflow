package com.salama.workflow.core.persistence;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

import MetoXML.XmlDeserializer;
import MetoXML.XmlReader;
import MetoXML.XmlSerializer;
import MetoXML.XmlWriter;
import MetoXML.Base.XmlNode;
import MetoXML.Base.XmlParseException;

import com.salama.util.ClassLoaderUtil;
import com.salama.workflow.core.error.XmlSerializeException;
import com.salama.workflow.core.util.XmlSerializable;

public class StateInstance implements XmlSerializable {
	private static final Logger logger = Logger.getLogger(StateInstance.class);
	

	private final static String NODE_NAME_ROOT =  StateInstance.class.getName();
	private final static String NODE_NAME_STATE_INFO = "StateInstanceInfo";
	private final static String NODE_NAME_STATE_DATA = "StateData";
	private final static String NODE_NAME_STATE_OPERATION = "StateOperation";
	
	private StateInstanceInfo _stateInstanceInfo = new StateInstanceInfo();
	
	private StateOperation _stateOperation = null;
	
	private Object _stateData = null;

	public StateInstance() {
		
	}
	
//	public StateInstance(long workFlowId, long stateId, String workFlowName, String stateName, Class<?> stateDataType) {
//		this._workFlowId = workFlowId;
//		this._stateId = stateId;
//		this._workFlowName = workFlowName;
//		this._stateName = stateName;
//		this._stateDataType = stateDataType;
//	}

	@Override
	public void loadXml(String xml) throws XmlSerializeException {
		try {
			XmlReader reader = new XmlReader();
			XmlNode rootNode = reader.StringToXmlNode(xml, XmlDeserializer.DefaultCharset);
			XmlNode idNode = null;
			XmlNode dataNode = null;
			XmlNode stateOperationNode = null;
			XmlNode nodeTmp = null;
			
			nodeTmp = rootNode.getFirstChildNode();
			while(nodeTmp != null) {
				if(nodeTmp.getName().equals(NODE_NAME_STATE_INFO)) {
					idNode = nodeTmp;
				} else if(nodeTmp.getName().equals(NODE_NAME_STATE_DATA)) {
					dataNode = nodeTmp;
				} else if (nodeTmp.getName().equals(NODE_NAME_STATE_OPERATION)) {
					stateOperationNode = nodeTmp;
				}
				
				nodeTmp = nodeTmp.getNextNode();
			}

			XmlDeserializer xmlDes = new XmlDeserializer();

			if(idNode != null) {
				this._stateInstanceInfo = (StateInstanceInfo) xmlDes.ConvertXmlNodeToObject(
						idNode, StateInstanceInfo.class);
				
				if(dataNode != null) {
					ClassLoader classLoader = ClassLoaderUtil.getDefaultClassLoader();
					Class<?> stateDataTypeClass = classLoader.loadClass(_stateInstanceInfo.getStateDataClass());

					this._stateData = xmlDes.ConvertXmlNodeToObject(dataNode, stateDataTypeClass);
				}
				
				if(stateOperationNode != null) {
					this._stateOperation = (StateOperation) xmlDes.ConvertXmlNodeToObject(
							stateOperationNode, StateOperation.class);
				}
			}
		} catch (IOException e) {
			logger.error("loadXml()", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (XmlParseException e) {
			logger.error("loadXml()", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (InvocationTargetException e) {
			logger.error("loadXml()", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (IllegalAccessException e) {
			logger.error("loadXml()", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (InstantiationException e) {
			logger.error("loadXml()", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (NoSuchMethodException e) {
			logger.error("loadXml()", e);
			throw new XmlSerializeException("loadXml()", e);
		} catch (ClassNotFoundException e) {
			logger.error("loadXml()", e);
			throw new XmlSerializeException("loadXml()", e);
		}
		
	}
	
	@Override
	public String toXml() throws XmlSerializeException {
		try {
			ClassLoader classLoader = ClassLoaderUtil.getDefaultClassLoader();
			Class<?> stateDataTypeClass = classLoader.loadClass(_stateInstanceInfo.getStateDataClass());

			String xml = "<" + NODE_NAME_ROOT + ">\r\n";
			
			XmlSerializer xmlSer = new XmlSerializer();
			XmlWriter xmlWriter = new XmlWriter();
			XmlNode xmlNode = null;

			xmlNode = xmlSer.ConvertObjectToXmlNode(
					NODE_NAME_STATE_INFO, _stateInstanceInfo, StateInstanceInfo.class, true);
			xml += xmlWriter.XmlNodeToString(xmlNode, XmlDeserializer.DefaultCharset);

			if(_stateOperation != null) {
				xmlNode = xmlSer.ConvertObjectToXmlNode(
						NODE_NAME_STATE_OPERATION, _stateOperation, StateOperation.class, true);
				xml += xmlWriter.XmlNodeToString(xmlNode, XmlDeserializer.DefaultCharset);
			}
			
			if(_stateData != null) {
				xmlNode = xmlSer.ConvertObjectToXmlNode(
						NODE_NAME_STATE_DATA, _stateData, stateDataTypeClass, true);
				xml += xmlWriter.XmlNodeToString(xmlNode, XmlDeserializer.DefaultCharset);
			}
			
			xml += "</" + NODE_NAME_ROOT + ">\r\n";
			
			return xml;
		} catch (ClassNotFoundException e) {
			logger.error("toXml()", e);
			throw new XmlSerializeException("toXml()", e);
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
	
	public StateInstanceInfo getStateInstanceInfo() {
		return _stateInstanceInfo;
	}

	public void setStateInstanceInfo(StateInstanceInfo stateInstanceInfo) {
		_stateInstanceInfo = stateInstanceInfo;
	}

	public Object getStateData() {
		return _stateData;
	}

	public void setStateData(Object stateData) {
		_stateData = stateData;
	}

	/**
	 * Operation which had been done 
	 * @return
	 */
	public StateOperation getStateOperation() {
		return _stateOperation;
	}

	/**
	 * Operation which had been done
	 * @param stateOperation
	 */
	public void setStateOperation(StateOperation stateOperation) {
		_stateOperation = stateOperation;
	}
	

	
}
