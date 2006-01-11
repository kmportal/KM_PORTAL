package com.ibm.km.dto;

import java.util.ArrayList;
import java.util.Date;

public class SMSReportDTO {
	private String smsSender;    	
	private String mobileNo;	
	private String smsContent;     
    private String smsCategory;     
    private String smsCreatedDate;  
    private String circleName;
    private String patnerName;    
    private String location;        
    private String smsStatus;       
	private ArrayList<SMSReportDTO> smsReportList = null;
	private Integer totalSMSCount;
	private String udId;
	private String olmId;
	
	
	public String getSmsSender() {
		return smsSender;
	}
	public void setSmsSender(String smsSender) {
		this.smsSender = smsSender;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	public String getSmsCategory() {
		return smsCategory;
	}
	public void setSmsCategory(String smsCategory) {
		this.smsCategory = smsCategory;
	}
	public String getSmsCreatedDate() {
		return smsCreatedDate;
	}
	public void setSmsCreatedDate(String smsCreatedDate) {
		this.smsCreatedDate = smsCreatedDate;
	}
	public String getCircleName() {
		return circleName;
	}
	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}
	public String getPatnerName() {
		return patnerName;
	}
	public void setPatnerName(String patnerName) {
		this.patnerName = patnerName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSmsStatus() {
		return smsStatus;
	}
	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}
	public ArrayList<SMSReportDTO> getSmsReportList() {
		return smsReportList;
	}
	public void setSmsReportList(ArrayList<SMSReportDTO> smsReportList) {
		this.smsReportList = smsReportList;
	}
	public Integer getTotalSMSCount() {
		return totalSMSCount;
	}
	public void setTotalSMSCount(Integer totalSMSCount) {
		this.totalSMSCount = totalSMSCount;
	}
	public String getUdId() {
		return udId;
	}
	public void setUdId(String udId) {
		this.udId = udId;
	}
	public String getOlmId() {
		return olmId;
	}
	public void setOlmId(String olmId) {
		this.olmId = olmId;
	}
	
	
	
	
	
}
