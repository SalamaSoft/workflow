package workflowegine.junittest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import MetoXML.XmlDeserializer;
import MetoXML.XmlSerializer;
import MetoXML.Base.XmlParseException;

import com.salama.workflow.core.config.WorkFlowEngineContext;
import com.salama.workflow.core.meta.Activity;
import com.salama.workflow.core.meta.Application;
import com.salama.workflow.core.meta.Event;
import com.salama.workflow.core.meta.Param;
import com.salama.workflow.core.meta.RelevantData;
import com.salama.workflow.core.meta.Role;
import com.salama.workflow.core.meta.SetValue;
import com.salama.workflow.core.meta.State;
import com.salama.workflow.core.meta.Transition;
import com.salama.workflow.core.meta.WorkFlow;
import com.salama.workflow.core.persistence.RoleUser;
import com.salama.workflow.core.persistence.StateInstanceInfo;
import com.salama.workflow.engine.persistence.disk.DiskFilePersistenceContext;

import ognl.Ognl;
import ognl.OgnlException;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			test4();
		} catch(Exception e) {
			error("main()", e);
		}
	}
	
	private static void test4() {
		String xml = "<StateInstanceInfo>  <stateDataClass>com.toshiba.data.Sales.Sales901DetailData</stateDataClass>  <stateDescription>新建提货单</stateDescription>  <stateId>1375092551696</stateId>  <stateName>state13600617853241</stateName>  <updateTime>2013-07-29T18:09:11.69600+08:00</updateTime>  <updateUser>    <role>admin</role>    <userId>admin</userId>  </updateUser>  <workFlowId>1375092551680</workFlowId>  <workFlowName>Sales901_0000000381783619218</workFlowName></StateInstanceInfo>";
		try {
			StateInstanceInfo data = (StateInstanceInfo)XmlDeserializer.stringToObject(xml, StateInstanceInfo.class);
			Date d = data.getUpdateTime();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void test1() throws OgnlException {
		boolean b1, b2, b3;
		TestData data1 = new TestData();
		
		data1.setA("1");
		data1.setB("002");
		b1 = (Boolean) Ognl.getValue("a == b", data1);

		TestData data2 = new TestData();
		data2.setA(" 1");
		data2.setB("1");
		
		b2 = (Boolean) Ognl.getValue("a == b", data2);
		
		debug("b1:" + b1 + "; b2:" + b2);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("data1", data1);
		map.put("data2", data2);
		
		data1.setC(10);
		data2.setC(25);
		
		//b3 = (Boolean) Ognl.getValue("(data1.c + 15) == data2.c", map);
		b3 = (Boolean) Ognl.getValue("data1.a.trim() == data2.a.trim()", map);
		debug("b3:" + b3);
		
		Ognl.setValue("data1", map, data2);
		debug("data1.C:" + Ognl.getValue("data1.c", map));
		
		boolean b4 = (Boolean) Ognl.getValue("true", map);
		debug("test:" + b4);
		
		Object c1 = Ognl.getValue("data1.c", map);
		Object c2 = Ognl.getValue("data2.c", map);
		int b5 = ((Comparable)c1).compareTo(c2);
		debug("b5:" + b5);
		
		TestData data3 = new TestData();
		data3.setA("test1");
		data3.setC(33);
		Ognl.setValue("#this", data1, data3);
		
		debug("a=='test1':" + Ognl.getValue("a == 'test1'", data3));
		
	}
	
	private static void test2() throws Exception {
		WorkFlow workFlowMeta = new WorkFlow();

		Role role1 = new Role();
		role1.setName("role1");
		role1.setDescription("瀹℃壒1");
		
		Role role2 = new Role();
		role2.setName("role2");
		role2.setDescription("瀹℃壒2");

		Role role3 = new Role();
		role3.setName("role3");
		role3.setDescription("瀹℃壒3");
		
		workFlowMeta.getAllRoles().add(role1);
		workFlowMeta.getAllRoles().add(role2);
		workFlowMeta.getAllRoles().add(role3);

		Activity activity1 = new Activity();
		activity1.getAccessibleRoles().add(role1);
		activity1.setName("agree");
		activity1.setDescription("鍚屾剰");
		
		Application application = new Application();
		application.setApplicationClass("xxx.xxxx.xxxService");
		application.setMethodName("agree");
		
		Param param1 = new Param();
		param1.setParamType(RoleUser.class.getName());
		param1.setValueExpression("stateInstanceInfo.updateUser");
		application.getParams().add(param1);
		
		SetValue setValue = new SetValue();
		setValue.setSrcExpression("#this");
		setValue.setDestExpression("stateData.agreeFlg");
		application.getSetReturnValueToState().add(setValue);
		
		activity1.setApplication(application);
		
		Transition transition = new Transition();
		transition.setToState("state2");
		SetValue setValue2 = new SetValue();
		setValue2.setSrcExpression("stateData");
		setValue2.setDestExpression("stateData");
		transition.getSetValueToToState().add(setValue2);
		
		State state1 = new State();
		state1.getAccessibleRoles().add(role1);
		state1.getActivities().add(activity1);

		Event event = new Event();
		event.setActivityName("agree");
		event.setName("agree");
		
		state1.getActivityEvents().add(event);
		
		state1.setInitiateActivity(activity1);
		state1.setName("state1");
		RelevantData data = new RelevantData();
		data.setDataClass("com.salama.appserver.workflow.ProjectStateData");
		data.setDescription("椤圭洰");
		
		state1.setStateData(data);
		state1.setStateType(State.StateType_Normal);
		
		workFlowMeta.getAllStates().add(state1);
		workFlowMeta.setName("project");
		workFlowMeta.setStartState("state1");
		
		XmlSerializer xmlSer = new XmlSerializer();
		xmlSer.Serialize("workFlow.xml", workFlowMeta, WorkFlow.class, XmlDeserializer.DefaultCharset, true);
	}
	
	private static void test3() throws Exception {
		WorkFlowEngineContext context = new WorkFlowEngineContext();
		
		XmlSerializer xmlSer = new XmlSerializer();
		xmlSer.Serialize("WorkFlowEngineContext.xml", context, WorkFlowEngineContext.class, XmlDeserializer.DefaultCharset, true);
		
		DiskFilePersistenceContext diskPersistenceContext = new DiskFilePersistenceContext();
		xmlSer.Serialize("DiskFilePersistenceContext.xml", diskPersistenceContext, DiskFilePersistenceContext.class, XmlDeserializer.DefaultCharset, true);
	}
	
	protected static void debug(String msg) {
		System.out.println(msg);
	}
	
	protected static void error(String msg,  Throwable e) {
		System.out.println(msg);
		e.printStackTrace();
	} 
}
