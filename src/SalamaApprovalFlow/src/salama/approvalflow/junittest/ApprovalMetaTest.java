package salama.approvalflow.junittest;

import java.util.ArrayList;
import java.util.List;

import MetoXML.XmlDeserializer;
import MetoXML.XmlSerializer;

import com.salama.approvalflow.meta.designer.ApprovalActivity;
import com.salama.approvalflow.meta.designer.ApprovalActivityNames;
import com.salama.approvalflow.meta.designer.ApprovalFlow;
import com.salama.approvalflow.meta.designer.ApprovalState;
import com.salama.workflow.core.meta.RelevantData;
import com.salama.workflow.core.meta.Role;
import com.salama.workflow.core.meta.State;
import com.salama.workflow.core.meta.StateDataFieldUISetting;
import com.salama.workflow.core.meta.StateDataUISetting;
import com.salama.workflow.core.meta.UISetting;

public class ApprovalMetaTest {
	public static void main(String[] args) {
		try {
			outputApprovalFlow();
		} catch(Exception e) {
			error("main()", e);
		}
	}
	
	private static void outputApprovalFlow() throws Exception {
		ApprovalFlow flow = createTestApprovalMeta();
		
		XmlSerializer xmlSer = new XmlSerializer();
		xmlSer.Serialize("ApprovalFlow01.xml", flow, ApprovalFlow.class, XmlDeserializer.DefaultCharset, true);
	}

	private static ApprovalFlow createTestApprovalMeta() {
		List<Role> roleList = new ArrayList<Role>();
		Role role1 = new Role();
		role1.setName("role1");
		role1.setDescription("审批1");
		
		Role role2 = new Role();
		role2.setName("role2");
		role2.setDescription("审批2");

		Role role3 = new Role();
		role3.setName("role3");
		role3.setDescription("审批3");

		roleList.add(role1);
		roleList.add(role2);
		roleList.add(role3);
		
		ApprovalFlow flow = new ApprovalFlow();
		
		flow.setAllRoles(roleList);
		
		ApprovalActivity approveActivity = new ApprovalActivity();
		approveActivity.setName(ApprovalActivityNames.Approve);
		approveActivity.setToState("state2");
		
		ApprovalActivity disapproveActivity = new ApprovalActivity();
		disapproveActivity.setName(ApprovalActivityNames.Disapprove);
		disapproveActivity.setToState("state1");
		
		RelevantData stateData = new RelevantData();
		stateData.setDataClass("xxxx.xxx.Data");
		
		StateDataUISetting dataUISetting = new StateDataUISetting();
		StateDataFieldUISetting fieldUISetting = new StateDataFieldUISetting();
		fieldUISetting.setFieldNameExpression("feild1");
		UISetting uiSetting = new UISetting();
		uiSetting.setName("readOnly");
		uiSetting.setValue("true");
		
		fieldUISetting.getUiSettings().add(uiSetting);
		dataUISetting.getFieldUISettings().add(fieldUISetting);
		
		ApprovalState state1 = new ApprovalState();
		state1.setName("state1");
		state1.setStateData(stateData);
		state1.setStateDataUISetting(dataUISetting);
		state1.setStateType(State.StateType_Normal);

		state1.getActivityList().add(approveActivity);
		state1.getActivityList().add(disapproveActivity);
		
		state1.getAccessibleRoles().add(role1);
		
		
		ApprovalState state2 = new ApprovalState();
		state2.setName("state2");
		state2.setStateData(stateData);
		state2.setStateDataUISetting(dataUISetting);
		state2.setStateType(State.StateType_Completed);

		state2.getAccessibleRoles().add(role2);
		state2.getAccessibleRoles().add(role3);
		
		
		flow.getAllStates().add(state1);
		flow.getAllStates().add(state2);
		
		return flow;
	}
	
	protected static void error(String msg,  Throwable e) {
		System.out.println(msg);
		e.printStackTrace();
	} 
	
}
