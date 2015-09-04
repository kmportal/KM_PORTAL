package com.ibm.km.dto;

public class KmContentStatusReportDto {

	private String documentId=null;
	private String documentName=null;
	private String uploadedDate=null;
	private String uploadedTime=null;
	private String documentPath=null;
	private String uploadedBy=null;
	private String circle=null;
	private String lob=null;
	private String publishingEndDate=null;
	
	
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(String uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	public String getUploadedTime() {
		return uploadedTime;
	}
	public void setUploadedTime(String uploadedTime) {
		this.uploadedTime = uploadedTime;
	}
	public String getDocumentPath() {
		return documentPath;
	}
	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}
	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	public String getPublishingEndDate() {
		return publishingEndDate;
	}
	public void setPublishingEndDate(String publishingEndDate) {
		this.publishingEndDate = publishingEndDate;
	}
	
	
}
