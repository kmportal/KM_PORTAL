package com.ibm.km.dto;

import java.io.Serializable;
import java.util.List;

public class KmSchemesAndBenefitsDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String   id    			 =  null;
	private String   type     		 =  null;
	private String   description     =  null;
	private String   displayFlag     =  null;
	private String   insertedBy    	 =  null;
	private String   insertedDate    =  null;
	private List   	 recordList      =  null;
	private String   filePath 		 =  null;
	private String   fileName 		 =  null;
	private List     sheetList 		 =  null;
	private int   	 srNo      		 =  0;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisplayFlag() {
		return displayFlag;
	}
	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
	public String getInsertedBy() {
		return insertedBy;
	}
	public void setInsertedBy(String insertedBy) {
		this.insertedBy = insertedBy;
	}
	public String getInsertedDate() {
		return insertedDate;
	}
	public void setInsertedDate(String insertedDate) {
		this.insertedDate = insertedDate;
	}
	public List getRecordList() {
		return recordList;
	}
	public void setRecordList(List recordList) {
		this.recordList = recordList;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public List getSheetList() {
		return sheetList;
	}
	public void setSheetList(List sheetList) {
		this.sheetList = sheetList;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	
}