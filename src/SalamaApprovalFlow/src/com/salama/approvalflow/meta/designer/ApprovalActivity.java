package com.salama.approvalflow.meta.designer;

public class ApprovalActivity {
	private String _name = "";
	
	private String _description = "";
	
	private String _toState = "";

	private String _boolExpressionForMoveToNext = "";
	
	private String _condtionHandlerClass = "";
	
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
	
	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getToState() {
		return _toState;
	}

	public void setToState(String toState) {
		_toState = toState;
	}

	public String getBoolExpressionForMoveToNext() {
		return _boolExpressionForMoveToNext;
	}

	public void setBoolExpressionForMoveToNext(String boolExpressionForMoveToNext) {
		_boolExpressionForMoveToNext = boolExpressionForMoveToNext;
	}

	public String getCondtionHandlerClass() {
		return _condtionHandlerClass;
	}

	public void setCondtionHandlerClass(String condtionHandlerClass) {
		_condtionHandlerClass = condtionHandlerClass;
	}
	
}
