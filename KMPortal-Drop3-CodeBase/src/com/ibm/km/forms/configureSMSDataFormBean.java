package com.ibm.km.forms;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

import com.ibm.km.dto.KmConfigureDataForSMSDto;

public class configureSMSDataFormBean extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String methodName;
	private String docType="";
	private String selectValue;
	//private String tableName;
	private ArrayList<KmConfigureDataForSMSDto> columnList;
	private String isConfigurable;
	
	private String columnId;
	private String columnName;
	private String tableName;
	
	private ArrayList<String> columnIdList;
	private ArrayList<String> isConfigurableList;
	private String idFlag;
	private String message;
	private ArrayList<KmConfigureDataForSMSDto>docTypeList;
	private String docTypeName;

	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getSelectValue() {
		return selectValue;
	}
	public void setSelectValue(String selectValue) {
		this.selectValue = selectValue;
	}
	/*public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}*/
	public ArrayList<KmConfigureDataForSMSDto> getColumnList() {
		return columnList;
	}
	public void setColumnList(ArrayList<KmConfigureDataForSMSDto> columnList) {
		this.columnList = columnList;
	}
	public String getIsConfigurable() {
		return isConfigurable;
	}
	public void setIsConfigurable(String isConfigurable) {
		this.isConfigurable = isConfigurable;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public ArrayList<String> getColumnIdList() {
		return columnIdList;
	}
	public void setColumnIdList(ArrayList<String> columnIdList) {
		this.columnIdList = columnIdList;
	}
	public ArrayList<String> getIsConfigurableList() {
		return isConfigurableList;
	}
	public void setIsConfigurableList(ArrayList<String> isConfigurableList) {
		this.isConfigurableList = isConfigurableList;
	}
	public String getIdFlag() {
		return idFlag;
	}
	public void setIdFlag(String idFlag) {
		this.idFlag = idFlag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<KmConfigureDataForSMSDto> getDocTypeList() {
		return docTypeList;
	}
	public void setDocTypeList(ArrayList<KmConfigureDataForSMSDto> docTypeList) {
		this.docTypeList = docTypeList;
	}
	public String getDocTypeName() {
		return docTypeName;
	}
	public void setDocTypeName(String docTypeName) {
		this.docTypeName = docTypeName;
	}

}
