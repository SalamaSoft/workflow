package com.salama.workflow.core.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

public class BeanUtil {
	private final static Logger logger = Logger.getLogger(BeanUtil.class);
	
	public static void simpleDataValueCopy(Object srcData, Object destData) 
			throws IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException{
		if(srcData == null || destData == null) {
			return;
		}
		
		PropertyDescriptor[] props = Introspector.getBeanInfo(srcData.getClass()).getPropertyDescriptors();
		PropertyDescriptor prop = null;
		
		if(props == null || props.length == 0) {
			return;
		} else {
			for(int i = 0; i < props.length; i++) {
				prop = props[i];
				
				if((prop.getWriteMethod() != null) && (prop.getReadMethod() != null)) {
					prop.getWriteMethod().invoke(destData, prop.getReadMethod().invoke(srcData));
				}
			}
		}
	}
	
	public static Object simpleDataCopy(Object data) 
			throws IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {
		if(data == null) {
			return null;
		}

		
		try {
			Object newData = data.getClass().newInstance();
			
			PropertyDescriptor[] props = Introspector.getBeanInfo(data.getClass()).getPropertyDescriptors();
			PropertyDescriptor prop = null;
			
			if(props == null || props.length == 0) {
				return newData;
			} else {
				for(int i = 0; i < props.length; i++) {
					prop = props[i];
					
					if((prop.getWriteMethod() != null) && (prop.getReadMethod() != null)) {
						prop.getWriteMethod().invoke(newData, prop.getReadMethod().invoke(data));
					}
				}
				
				return newData;
			}
		} catch (IllegalArgumentException e) {
			logger.error("simpleDataCopy()", e);
			throw e;
		} catch (InstantiationException e) {
			logger.error("simpleDataCopy()", e);
			throw e;
		} catch (IllegalAccessException e) {
			logger.error("simpleDataCopy()", e);
			throw e;
		} catch (IntrospectionException e) {
			logger.error("simpleDataCopy()", e);
			throw e;
		} catch (InvocationTargetException e) {
			logger.error("simpleDataCopy()", e);
			throw e;
		}
	}
}
