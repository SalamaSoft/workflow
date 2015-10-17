package com.salama.workflow.engine;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import MetoXML.XmlDeserializer;

import com.salama.reflect.PreScanClassFinder;
import com.salama.service.core.context.CommonContext;
import com.salama.util.ClassLoaderUtil;
import com.salama.workflow.core.IWorkFlowEngine;
import com.salama.workflow.core.IWorkFlowEngineContext;
import com.salama.workflow.core.config.BasePackage;
import com.salama.workflow.core.persistence.PersistenceService;

public class WorkFlowEngineContext implements IWorkFlowEngineContext, CommonContext, Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2262355139965381425L;

	private final static Logger logger = Logger.getLogger(WorkFlowEngineContext.class);
	
	private com.salama.workflow.core.config.WorkFlowEngineContext _contextConfig = null;
	
	private PreScanClassFinder _dataClassFinder = new PreScanClassFinder();

    private PreScanClassFinder _applicationClassFinder = new PreScanClassFinder();

    private Class<?> _persistenceServiceClass = null;
    
    private PersistenceService _persistenceService = null;

	private WorkFlowEngine _workFlowEngine = null;
	
	@Override
	public void reload(com.salama.workflow.core.config.WorkFlowEngineContext contextConfig) {
    	_contextConfig = contextConfig;
    	
		try {
			reloadPreScanClass(_applicationClassFinder, _contextConfig.getApplicationScan());
		} catch (Exception e) {
			logger.error(e);
			//throw new WorkFlowEngineException(e);
		}
		
		try {
			reloadPreScanClass(_dataClassFinder, _contextConfig.getDataScan());
		} catch (Exception e) {
			logger.error(e);
			//throw new WorkFlowEngineException(e);
		}
		
		try {
			ClassLoader defaultClassLoader = ClassLoaderUtil.getDefaultClassLoader();
			_persistenceServiceClass = defaultClassLoader.loadClass(_contextConfig.getPersistenceSetting().getPersistenceService().getClassName());
			_persistenceService = (PersistenceService) _persistenceServiceClass.newInstance();
			_persistenceService.init(_contextConfig.getPersistenceSetting().getConfigLocation());
		} catch (Exception e) {
			logger.error(e);
			//throw new WorkFlowEngineException(e);
		}

		try {
			_workFlowEngine = new WorkFlowEngine(this);
			_workFlowEngine.init();
		} catch(Exception e) {
			logger.error(e);
		}
		
    }
    
	@Override
    public void destroy() {
		try {
	    	_persistenceService.destroy();
		} catch (Exception e) {
			logger.error(e);
			//throw new WorkFlowEngineException(e);
		}
		
		try {
			_workFlowEngine.destroy();
		} catch(Exception e) {
			logger.error(e);
		}
    }

	@Override
	public IWorkFlowEngine getWorkFlowEngine() {
		return _workFlowEngine;
	}

	@Override
	public void reload(ServletContext servletContext, String configLocation) {
		logger.debug("reload() configLocation:" + configLocation);
		
		String configFilePath = servletContext.getRealPath(configLocation);
		
		XmlDeserializer xmlDes = new XmlDeserializer();
		
		try {
			_contextConfig = (com.salama.workflow.core.config.WorkFlowEngineContext)
			xmlDes.Deserialize(configFilePath, 
					com.salama.workflow.core.config.WorkFlowEngineContext.class, 
					XmlDeserializer.DefaultCharset);
		} catch (Exception e) {
			logger.error(e);
			return;
		}

		try {
			//In servlet runtime envirement the configure location is a virtual path, need to be converted to real path 
			_contextConfig.getPersistenceSetting().setConfigLocation(
					servletContext.getRealPath(_contextConfig.getPersistenceSetting().getConfigLocation()));
			
			reload(_contextConfig);
		} catch(Exception e) {
			logger.error(e);
			return;
		}
	}	
	
	public com.salama.workflow.core.config.WorkFlowEngineContext getContextConfig() {
		return _contextConfig;
	}

	protected static void reloadPreScanClass(
			PreScanClassFinder preScanClassFinder, 
			List<BasePackage> basePackageList) {
		preScanClassFinder.clearPreScannedClass();
		
		for(int i = 0; i < basePackageList.size(); i++) {
			preScanClassFinder.loadClassOfPackage(basePackageList.get(i).getBasePackage());
		}
	}
    

	public void setContextConfig(
			com.salama.workflow.core.config.WorkFlowEngineContext contextConfig) {
		_contextConfig = contextConfig;
	}
    
	public PreScanClassFinder getDataClassFinder() {
		return _dataClassFinder;
	}

	public void setDataClassFinder(PreScanClassFinder dataClassFinder) {
		_dataClassFinder = dataClassFinder;
	}

	public PreScanClassFinder getApplicationClassFinder() {
		return _applicationClassFinder;
	}

	public void setApplicationClassFinder(PreScanClassFinder applicationClassFinder) {
		_applicationClassFinder = applicationClassFinder;
	}

	public Class<?> getPersistenceServiceClass() {
		return _persistenceServiceClass;
	}

	public void setPersistenceServiceClass(Class<?> persistenceServiceClass) {
		_persistenceServiceClass = persistenceServiceClass;
	}

	public PersistenceService getPersistenceService() {
		return _persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		_persistenceService = persistenceService;
	}


}
