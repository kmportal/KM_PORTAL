package com.ibm.km.dto;

import java.util.ArrayList;

public class KmConfigureDataForSMSDto {
	private String methodName;
	private String docType="";
	private String selectValue;
	//private String tableName;
	private ArrayList<KmConfigureDataForSMSDto> columnList;
	
	private String columnId;
	private String columnName;
	private String tableName;
		
	private String isConfigurable;

	private Integer docId;
	private String docTypeName;
	private String status;
	
	
	
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
	public ArrayList<KmConfigureDataForSMSDto> getColumnList() {
		return columnList;
	}
	public void setColumnList(ArrayList<KmConfigureDataForSMSDto>columnList) {
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
	public Integer getDocId() {
		return docId;
	}
	public void setDocId(Integer docId) {
		this.docId = docId;
	}
	public String getDocTypeName() {
		return docTypeName;
	}
	public void setDocTypeName(String docTypeName) {
		this.docTypeName = docTypeName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
