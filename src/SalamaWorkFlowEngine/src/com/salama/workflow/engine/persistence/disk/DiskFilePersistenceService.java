package com.salama.workflow.engine.persistence.disk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import MetoXML.XmlDeserializer;
import MetoXML.XmlSerializer;

import com.salama.workflow.core.error.PersistenceException;
import com.salama.workflow.core.meta.WorkFlow;
import com.salama.workflow.core.meta.WorkFlowSetting;
import com.salama.workflow.core.persistence.IWorkFlowInstanceFilter;
import com.salama.workflow.core.persistence.PersistenceService;
import com.salama.workflow.core.persistence.StateInstance;
import com.salama.workflow.core.persistence.WorkFlowInstance;
import com.salama.workflow.core.persistence.WorkFlowTraceInstance;

/**
 *  ${baseDir}/meta/${workFlowName}.xml
 *  ${baseDir}/data/state/${stateId}.xml : stateInstance xml
 *  ${baseDir}/data/workFlow/${workFlowId}.xml
 *  ${baseDir}/data/trace/${workFlowId}.xml
 *  ${baseDir}/map/role/${role}/workFlow/${workFlowName}.wfrm
 *  ${baseDir}/map/role/${role}/currentState/${stateId}.wfrm
 * @author Liuxg
 *
 */
public class DiskFilePersistenceService implements PersistenceService {
	private final static Logger logger = Logger.getLogger(DiskFilePersistenceService.class);

	public final static String DIR_META = "meta";
	public final static String DIR_DATA = "data";
	public final static String DIR_DATA_STATE = "state";
	public final static String DIR_WORK_FLOW = "workFlow";
	public final static String DIR_TRACE = "trace";

	public final static String FILE_EXT = ".xml";

	private String _metaDir = null;
	private String _dataDir = null;
	
	private File _fileMetaDir = null;
	private File _fileDataDir = null;

	/*********** map index ***************/
	protected final static String DIR_NAME_MAP = "map";
	protected final static String DIR_NAME_ROLE = "role";
	protected final static String DIR_NAME_WORK_FLOW = "workFlow";
	protected final static String DIR_NAME_CURRENT_STATE = "currentState";
	
	protected final static String FILE_EXT_ROLE_WORK_FLOW_MAP = ".wfrm";
	
	private String _mapDir = "";
	private File _fileMapDir = null;
	
	private String _mapRoleDir = "";
	private File _fileMapRoleDir = null;
	
	private DiskFilePersistenceContext _contextConfig = null;
	
	public void init(String configFilePath) throws PersistenceException {
		XmlDeserializer xmlDes = new XmlDeserializer();
		
		try {
			_contextConfig = (DiskFilePersistenceContext) xmlDes.Deserialize(configFilePath, 
					DiskFilePersistenceContext.class, XmlDeserializer.DefaultCharset);
		} catch(Exception e) {
			logger.error("init()", e);
		}

		initDirs();
		
		initMapDirs(_contextConfig.getBaseDir());
	}
	
	@Override
	public void destroy() throws PersistenceException {
	}
	
	@Override
	public WorkFlow getMetaDataWorkFlow(String workFlowName) throws PersistenceException {
		try {
			logger.debug("getMetaDataWorkFlow() begin");

			String dataFilePath = getFilePathOfWorkFlowMeta(workFlowName);
			File file = new File(dataFilePath);
			if(!file.exists()) {
				return null;
			}
			
			XmlDeserializer xmlDes = new XmlDeserializer();

			logger.debug("getMetaDataWorkFlow() end");
			return (WorkFlow) xmlDes.Deserialize(dataFilePath, WorkFlow.class, XmlDeserializer.DefaultCharset);
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public void setMetaDataWorkFlow(WorkFlow data) throws PersistenceException {
		try {
			logger.debug("setMetaDataWorkFlow() begin");

			String dataFilePath = getFilePathOfWorkFlowMeta(data.getName());

			XmlSerializer xmlSer = new XmlSerializer();
			xmlSer.Serialize(dataFilePath, data, WorkFlow.class, XmlDeserializer.DefaultCharset, true);

			logger.debug("setMetaDataWorkFlow() end");
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public WorkFlowSetting getMetaDataWorkFlowSetting() throws PersistenceException {
		WorkFlowSetting workFlowSetting = new WorkFlowSetting();
		
		File[] files = _fileMetaDir.listFiles(new XmlFileFilter());
		String name = null;
		
		if(files != null) {
			for(int i = 0; i < files.length; i++) {
				name = files[i].getName().substring(0, files[i].getName().length() - 4);
				workFlowSetting.getAllWorkFlowNames().add(name);
			}
		}
		
		return workFlowSetting;
	}
	
	@Override
	public void setMetaDataWorkFlowSetting(WorkFlowSetting data) throws PersistenceException {
		//Do nothing
	}

	@Override
	public WorkFlowInstance getWorkFlowInstance(long workFlowId) throws PersistenceException {
		try {
			logger.debug("getWorkFlowInstance() begin");

			String dataFilePath = getFilePathOfWorkFlowInstance(workFlowId);

			File file = new File(dataFilePath);
			if(!file.exists()) {
				return null;
			}

			WorkFlowInstance data = new WorkFlowInstance();
			data.loadXml(readXmlFile(dataFilePath));

			logger.debug("getWorkFlowInstance() end");
			return data;
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public void setWorkFlowInstance(WorkFlowInstance data) throws PersistenceException {
		try {
			logger.debug("setWorkFlowInstance() begin");

			String dataFilePath = getFilePathOfWorkFlowInstance(data.getId());
			writeXmlFile(dataFilePath, data.toXml());

			logger.debug("setWorkFlowInstance() end");
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public WorkFlowTraceInstance getWorkFlowTraceInstance(long workFlowId) throws PersistenceException {
		try {
			logger.debug("getWorkFlowTraceInstance() begin");

			String dataFilePath = getFilePathOfWorkFlowTraceInstance(workFlowId);

			File file = new File(dataFilePath);
			if(!file.exists()) {
				return null;
			}

			WorkFlowTraceInstance data = new WorkFlowTraceInstance();
			data.loadXml(readXmlFile(dataFilePath));

			logger.debug("getWorkFlowTraceInstance() end");
			return data;
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public void setWorkFlowTraceInstance(WorkFlowTraceInstance data) throws PersistenceException {
		try {
			logger.debug("setWorkFlowTraceInstance() begin");

			String dataFilePath = getFilePathOfWorkFlowTraceInstance(data.getWorkFlowId());
			writeXmlFile(dataFilePath, data.toXml());

			logger.debug("setWorkFlowTraceInstance() end");
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public StateInstance getStateInstance(long stateId) throws PersistenceException {
		try {
			logger.debug("getStateInstance() begin");

			String dataFilePath = getFilePathOfStateInstance(stateId);

			File file = new File(dataFilePath);
			if(!file.exists()) {
				return null;
			}

			StateInstance data = new StateInstance();
			data.loadXml(readXmlFile(dataFilePath));

			logger.debug("getStateInstance() end");

			return data;
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public void setStateInstance(StateInstance data) throws PersistenceException {
		try {
			logger.debug("getStateInstance() begin");

			String dataFilePath = getFilePathOfStateInstance(data.getStateInstanceInfo().getStateId());
			writeXmlFile(dataFilePath, data.toXml());

			logger.debug("getStateInstance() end");
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public void addMapIndexOfRoleWorkFlow(String role, String workFlowName, long workFlowId) throws PersistenceException {
		logger.debug("addMapIndexOfRoleWorkFlow() begin");

		FileOutputStream fs = null;
		
		try {
			File workFlowFile = getRoleWorkFlowFile(role, workFlowName);
			if(!workFlowFile.exists()) {
				workFlowFile.createNewFile();
			}
			
			fs = new FileOutputStream(workFlowFile, true);
			fs.write(toRoleWorkFlowLine(workFlowId, new Date()).getBytes());
			fs.flush();

		} catch(Exception e) {
			throw new PersistenceException(e);
		} finally {
			try {
				fs.close();
				Thread.sleep(10);
			} catch(Exception e) {
			}

			logger.debug("addMapIndexOfRoleWorkFlow() end");
		}
	}

	@Override
	public void addMapIndexOfRoleCurrentState(String role, long stateId) throws PersistenceException {
		logger.debug("addMapIndexOfRoleCurrentState() begin");

		try {
			File file = getRoleCurrentStateFile(role, stateId);
			
			if(!file.exists()) {
				file.createNewFile();
			}

			logger.debug("addMapIndexOfRoleCurrentState() end");
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public void deleteMapIndexOfRoleCurrentState(String role, long stateId) throws PersistenceException {
		logger.debug("addMapIndexOfRoleCurrentState() begin");

		File file = getRoleCurrentStateFile(role, stateId);
		if(file.exists()) {
			file.delete();
		}

		logger.debug("addMapIndexOfRoleCurrentState() end");
	}
	
	@Override
	public List<Long> findWorkFlowIdByRole(String role, Date dateFrom, Date dateTo) throws PersistenceException {
		logger.debug("findWorkFlowIdByRole() begin");

		List<Long> workFlowIdList = new ArrayList<Long>();
		
		File roleWorkFlowDir = getRoleWorkFlowDir(role);
		String[] fileNameList = roleWorkFlowDir.list(new RoleWorkFlowMapFileFilter());
		String workFlowName = null;
		
		if(fileNameList != null) {
			for(int i = 0; i < fileNameList.length; i++) {
				workFlowName = fileNameList[i].substring(0, fileNameList[i].length() - FILE_EXT_ROLE_WORK_FLOW_MAP.length());
				workFlowIdList.addAll(findWorkFlowIdByRole(role, workFlowName, dateFrom, dateTo));
			}
		}
		
		logger.debug("findWorkFlowIdByRole() end");
		return workFlowIdList;
	}

	@Override
	public List<Long> findWorkFlowIdByRole(String role, String workFlowName, Date dateFrom, Date dateTo) throws PersistenceException {
		logger.debug("findWorkFlowIdByRole()#2 begin");

		List<Long> workFlowIdList = new ArrayList<Long>();
		
		File workFlowFile = getRoleWorkFlowFile(role, workFlowName);
		
		if(!workFlowFile.exists()) {
			return workFlowIdList;
		}
		
		FileReader fr = null;
		BufferedReader reader = null;
		
		try {
			fr = new FileReader(workFlowFile);
			reader = new BufferedReader(fr);
			
			String line = null;
			int index = 0;
			long workFlowId = 0;
			long time = 0;
			long timeFrom = 0;
			long timeTo = Long.MAX_VALUE;
			
			if(dateFrom != null) {
				timeFrom = dateFrom.getTime();
			}
			
			if(dateTo != null) {
				timeTo = dateTo.getTime();
			}
			
			while(true) {
				line = reader.readLine();
				
				if(line == null) {
					break;
				}
				
				if(line.length() == 0) {
					continue;
				}
				
				index = line.indexOf(',');
				workFlowId = Long.parseLong(line.substring(0, index));
				time = Long.parseLong(line.substring(index+1));
				
				if((time >= timeFrom) && (time <= timeTo)) {
					workFlowIdList.add(Long.valueOf(workFlowId));
				}
			}
			
			return workFlowIdList;
		} catch(Exception e) {
			throw new PersistenceException(e);
		} finally {
			try {
				fr.close();
			} catch(Exception e) {}
			try {
				reader.close();
			} catch(Exception e) {}

			logger.debug("findWorkFlowIdByRole()#2 end");
		}
	}
	
	@Override
	public List<Long> findCurrentStateIdByRole(String role) throws PersistenceException {
		logger.debug("findCurrentStateIdByRole() begin");

		List<Long> stateIdList = new ArrayList<Long>();
		
		File currentStateDir = getRoleCurrentStateDir(role);
		
		String[] fileNameList = currentStateDir.list(new RoleWorkFlowMapFileFilter());
		String stateId = null;
		
		if(fileNameList != null) {
			for(int i = 0; i < fileNameList.length; i++) {
				stateId = fileNameList[i].substring(0, fileNameList[i].length() - FILE_EXT_ROLE_WORK_FLOW_MAP.length());
				stateIdList.add(Long.parseLong(stateId));
			}
		}
		
		logger.debug("findCurrentStateIdByRole() end");
		return stateIdList;
	}

	@Override
	public List<WorkFlowInstance> findWorkFlow(String workFlowName, Date dateFrom,
			Date dateTo) throws PersistenceException {
		logger.debug("findWorkFlow() begin");

		try {
			String dirPath = getDirPathOfWorkFlowInstance();
			File fileDir = new File(dirPath);
			
			File[] files = fileDir.listFiles(new XmlFileFilter());

			List<WorkFlowInstance> dataList = new ArrayList<WorkFlowInstance>();
			
			if(files == null) {
				return dataList;
			}
			
			long time = 0;
			long timeFrom = 0;
			long timeTo = Long.MAX_VALUE;
			
			if(dateFrom != null) {
				timeFrom = dateFrom.getTime();
			}
			
			if(dateTo != null) {
				timeTo = dateTo.getTime();
			}
			
			long id = 0;
			
			WorkFlowInstance data = null;
			
			for(File file: files) {
				id = Long.parseLong(file.getName().substring(0, file.getName().length() - FILE_EXT.length()));
				time = id;

				if((time >= timeFrom) && (time <= timeTo)) {
					data = new WorkFlowInstance();
					data.loadXml(readXmlFile(file));

					if(workFlowName.equals(data.getWorkFlowName())) {
						dataList.add(data);
					}
				}
			}
			
			logger.debug("findWorkFlow() end");
			return dataList;
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<WorkFlowInstance> findWorkFlow(String workFlowName,
			Date dateFrom, Date dateTo,
			IWorkFlowInstanceFilter workFlowInstanceFilter)
			throws PersistenceException {
		logger.debug("findWorkFlow()#2 begin");

		try {
			String dirPath = getDirPathOfWorkFlowInstance();
			File fileDir = new File(dirPath);
			
			File[] files = fileDir.listFiles(new XmlFileFilter());

			List<WorkFlowInstance> dataList = new ArrayList<WorkFlowInstance>();
			
			if(files == null) {
				return dataList;
			}
			
			long time = 0;
			long timeFrom = 0;
			long timeTo = Long.MAX_VALUE;
			
			if(dateFrom != null) {
				timeFrom = dateFrom.getTime();
			}
			
			if(dateTo != null) {
				timeTo = dateTo.getTime();
			}
			
			long id = 0;
			
			WorkFlowInstance data = null;
			
			for(File file: files) {
				id = Long.parseLong(file.getName().substring(0, file.getName().length() - FILE_EXT.length()));
				time = id;

				if((time >= timeFrom) && (time <= timeTo)) {
					data = new WorkFlowInstance();
					data.loadXml(readXmlFile(file));

					if(workFlowName.equals(data.getWorkFlowName())) {
						if(workFlowInstanceFilter.accept(data)) {
							dataList.add(data);
						}
					}
				}
			}
			
			logger.debug("findWorkFlow()#2 end");
			return dataList;
		} catch(Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public List<Long> findWorkFlowId(Date dateFrom, Date dateTo)
			throws PersistenceException {
		logger.debug("findWorkFlowId() begin");

		String dirPath = getDirPathOfWorkFlowInstance();
		File fileDir = new File(dirPath);
		
		String[] fileNames = fileDir.list(new XmlFileNameFilter());

		List<Long> idList = new ArrayList<Long>();

		if(fileNames == null) {
			return idList;
		}
		
		long time = 0;
		long timeFrom = 0;
		long timeTo = Long.MAX_VALUE;
		
		if(dateFrom != null) {
			timeFrom = dateFrom.getTime();
		}
		
		if(dateTo != null) {
			timeTo = dateTo.getTime();
		}
		
		long id = 0;
		
		for(String fileName: fileNames) {
			id = Long.parseLong(fileName.substring(0, fileName.length() - FILE_EXT.length()));
			time = id;

			if((time >= timeFrom) && (time <= timeTo)) {
				idList.add(id);
			}
		}
		
		logger.debug("findWorkFlowId() end");

		return idList;
	}
	
	private void initDirs() {
		_fileMetaDir = new File(_contextConfig.getBaseDir(), DIR_META);
		if(!_fileMetaDir.exists()) {
			_fileMetaDir.mkdirs();
		}

		_metaDir = _fileMetaDir.getAbsolutePath();
		if(!_metaDir.endsWith(File.separator)) {
			_metaDir += File.separator;
		}
		
		_fileDataDir = new File(_contextConfig.getBaseDir(), DIR_DATA);
		
		if(!_fileDataDir.exists()) {
			_fileDataDir.mkdirs();
		}
		
		_dataDir = _fileDataDir.getAbsolutePath();
		if(!_dataDir.endsWith(File.separator)) {
			_dataDir += File.separator;
		}
		
		 //  ${baseDir}/data/state/${stateId}.xml : stateInstance xml
		File fileDataStateDir = new File(_fileDataDir, DIR_DATA_STATE);
		if(!fileDataStateDir.exists()) {
			fileDataStateDir.mkdirs();
		}
		 //  ${baseDir}/data/workFlow/${workFlowId}.xml
		File fileDataWorkFlowDir = new File(_fileDataDir, DIR_WORK_FLOW);
		if(!fileDataWorkFlowDir.exists()) {
			fileDataWorkFlowDir.mkdirs();
		}
		 //  ${baseDir}/data/trace/${workFlowId}.xml
		File fileDataTraceDir = new File(_fileDataDir, DIR_TRACE);
		if(!fileDataTraceDir.exists()) {
			fileDataTraceDir.mkdirs();
		}
	}

	/**
	 * ${baseDir}/meta/${workFlowName}.xml
	 * @param workFlowName
	 * @return
	 */
	private String getFilePathOfWorkFlowMeta(String workFlowName) {
		return _metaDir + workFlowName + FILE_EXT;
	}

	 /**
	  * ${baseDir}/data/state/${stateId}.xml : stateInstance xml
	  * @param workFlowId
	  * @param stateId
	  * @return
	  */
	private String getFilePathOfStateInstance(long stateId) {
		return _dataDir 
				+ DIR_DATA_STATE + File.separator
				+ stateId + FILE_EXT;
	}

	/**
	 * ${baseDir}/data/workFlow/${workFlowId}.xml
	 * @param workFlowId
	 * @return
	 */
	private String getFilePathOfWorkFlowInstance(long workFlowId) {
		return _dataDir 
				+ DIR_WORK_FLOW + File.separator
				+ workFlowId + FILE_EXT;
	}

	/**
	 *  ${baseDir}/data/trace/${workFlowId}.xml
	 * @param workFlowId
	 * @return
	 */
	private String getFilePathOfWorkFlowTraceInstance(long workFlowId) {
		return _dataDir 
				+ DIR_TRACE + File.separator
				+ workFlowId + FILE_EXT;
	}

	private String getDirPathOfWorkFlowInstance() {
		return _dataDir 
				+ DIR_WORK_FLOW ;
	}
	
	/*
	private String getDirPathOfStateInstance() {
		return _dataDir 
				+ DIR_DATA_STATE ;
	}
	*/

	private String readXmlFile(String filePath) throws IOException {
		return readXmlFile(new File(filePath));
	}
	
	private String readXmlFile(File file) throws IOException {
		FileInputStream fs = null;
		InputStreamReader reader = null;
		int readCnt = 0;

		try {
			StringBuilder sb = new StringBuilder();
			
			fs = new FileInputStream(file);
			reader = new InputStreamReader(fs, XmlDeserializer.DefaultCharset);
			char[] tempChrBuff = new char[512];
			
			while(true) {
				readCnt = reader.read(tempChrBuff, 0, 512);
				if(readCnt < 0) {
					break;
				}
				
				sb.append(tempChrBuff, 0, readCnt);
			}
			
			return sb.toString();
		} finally {
			try {
				fs.close();
			} catch(Exception e) {
			}
			try {
				reader.close();
			} catch(Exception e) {
			}
		}
	}
	
	private void writeXmlFile(String filePath, String xml) throws IOException {
		FileOutputStream fs = null;
		OutputStreamWriter writer = null;
		
		try {
			fs = new FileOutputStream(filePath);
			writer = new OutputStreamWriter(fs, XmlDeserializer.DefaultCharset);
			writer.write(xml);
			writer.flush();
		} finally {
			try {
				fs.close();
			} catch(Exception e) {
			}
			try {
				writer.close();
			} catch(Exception e) {
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	} 

	private void initMapDirs(String baseDir) {
		File fileBaseDir = new File(baseDir);
		
		_fileMapDir = new File(fileBaseDir, DIR_NAME_MAP);
		_fileMapRoleDir = new File(_fileMapDir, DIR_NAME_ROLE);
		
		if(!_fileMapRoleDir.exists()) {
			_fileMapRoleDir.mkdirs();
		}

		_mapDir = _fileMapDir.getAbsolutePath();
		if(!_mapDir.endsWith(File.separator)) {
			_mapDir += File.separator;
		}
		
		_mapRoleDir = _fileMapRoleDir.getAbsolutePath();
		if(!_mapRoleDir.endsWith(File.separator)) {
			_mapRoleDir += File.separator;
		}
		
	}

	protected File getRoleWorkFlowDir(String role) {
		File dir = new File(_mapRoleDir + role + File.separator + DIR_NAME_WORK_FLOW);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		return dir;
	}

	protected File getRoleCurrentStateDir(String role) {
		File dir = new File(_mapRoleDir + role + File.separator + DIR_NAME_CURRENT_STATE);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		return dir;
	}
	
	protected File getRoleCurrentStateFile(String role, long stateId) {
		return new File(getRoleCurrentStateDir(role), stateId + FILE_EXT_ROLE_WORK_FLOW_MAP);
	}
	
	protected File getRoleWorkFlowFile(String role, String workFlowName) {
		return new File(getRoleWorkFlowDir(role), workFlowName + FILE_EXT_ROLE_WORK_FLOW_MAP);
	}
	
	protected String toRoleWorkFlowLine(long workFlowId, Date time) {
		return workFlowId + "," + time.getTime() + "\r\n";
	}
	
	protected class RoleWorkFlowMapFileFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(FILE_EXT_ROLE_WORK_FLOW_MAP);
		}
	}
	
	protected class XmlFileNameFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			if(name.endsWith(FILE_EXT)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	protected class XmlFileFilter implements FileFilter {
		@Override
		public boolean accept(File file) {
			if(file.isFile() && file.getName().endsWith(FILE_EXT)) {
				return true;
			} else {
				return false;
			}
		}
	}
}
