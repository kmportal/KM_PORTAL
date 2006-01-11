package com.ibm.km.dto;

import java.util.Date;
import java.util.List;

import org.apache.struts.upload.FormFile;

public class BulkUploadDetailsDTO {
	
	private String docType="";
	private int rowNumber;
	private FormFile newFile;
	private String methodName="";
	private String uploadType="";
	private String action="";
	public boolean isErrFlag;
	private String message="";

	//dist upload
	private String pinCode="";
	private String state="";
	private String city="";
	private String pinCategory="";
	private String area="";
	private String hub="";
	private String circle="";
	private String sfssdName="";
	private String sfssdMail="";
	private String sfssdContact="";
	private String tsm="";
	private String tsmMail="";
	private String tsmContact="";
	private String srManager="";
	private String srManagerMail="";
	private String srManagerContact="";
	private String asm="";
	private String asmMail="";
	private String asmContact="";
	
	
	private String zone="";
	private String arcOr="";
	private String arc="";
	private String address="";
	private String timings="";
	private String availabilityMc="";
	private String availabilitySim="";
	
	private String company="";
	private String spoc="";
	private String spocMail="";
	private String spocPhone="";
	private String rm="";
	private String rmMail="";
	private String rmPh="";
	private String requestor="";
	private String requestorLoc="";
	private String requestorPh="";
	private String reqOn;
	private String bulkUploadPath;
	private String bulkUploadflag;
	
	private String vasName;
	private String activationCode;
	private String deactivationCode;
	private String charges;
	private String benefits;
	private String construct;
	
	
		
	
	
	public String getBulkUploadflag() {
		return bulkUploadflag;
	}
	public void setBulkUploadflag(String bulkUploadflag) {
		this.bulkUploadflag = bulkUploadflag;
	}
	public String getBulkUploadPath() {
		return bulkUploadPath;
	}
	public void setBulkUploadPath(String bulkUploadPath) {
		this.bulkUploadPath = bulkUploadPath;
	}
	public String getReqOn() {
		return reqOn;
	}
	public void setReqOn(String reqOn) {
		this.reqOn = reqOn;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isErrFlag() {
		return isErrFlag;
	}
	public void setErrFlag(boolean isErrFlag) {
		this.isErrFlag = isErrFlag;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSpoc() {
		return spoc;
	}
	public void setSpoc(String spoc) {
		this.spoc = spoc;
	}
	public String getSpocMail() {
		return spocMail;
	}
	public void setSpocMail(String spocMail) {
		this.spocMail = spocMail;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public FormFile getNewFile() {
		return newFile;
	}
	public void setNewFile(FormFile newFile) {
		this.newFile = newFile;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getUploadType() {
		return uploadType;
	}
	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPinCategory() {
		return pinCategory;
	}
	public void setPinCategory(String pinCategory) {
		this.pinCategory = pinCategory;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getHub() {
		return hub;
	}
	public void setHub(String hub) {
		this.hub = hub;
	}
	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	public String getSfssdName() {
		return sfssdName;
	}
	public void setSfssdName(String sfssdName) {
		this.sfssdName = sfssdName;
	}
	public String getSfssdMail() {
		return sfssdMail;
	}
	public void setSfssdMail(String sfssdMail) {
		this.sfssdMail = sfssdMail;
	}
	public String getSfssdContact() {
		return sfssdContact;
	}
	public void setSfssdContact(String sfssdContact) {
		this.sfssdContact = sfssdContact;
	}
	public String getTsm() {
		return tsm;
	}
	public void setTsm(String tsm) {
		this.tsm = tsm;
	}
	public String getTsmMail() {
		return tsmMail;
	}
	public void setTsmMail(String tsmMail) {
		this.tsmMail = tsmMail;
	}
	public String getTsmContact() {
		return tsmContact;
	}
	public void setTsmContact(String tsmContact) {
		this.tsmContact = tsmContact;
	}
	public String getSrManager() {
		return srManager;
	}
	public void setSrManager(String srManager) {
		this.srManager = srManager;
	}
	public String getSrManagerMail() {
		return srManagerMail;
	}
	public void setSrManagerMail(String srManagerMail) {
		this.srManagerMail = srManagerMail;
	}
	public String getSrManagerContact() {
		return srManagerContact;
	}
	public void setSrManagerContact(String srManagerContact) {
		this.srManagerContact = srManagerContact;
	}
	public String getAsm() {
		return asm;
	}
	public void setAsm(String asm) {
		this.asm = asm;
	}
	public String getAsmMail() {
		return asmMail;
	}
	public void setAsmMail(String asmMail) {
		this.asmMail = asmMail;
	}
	public String getAsmContact() {
		return asmContact;
	}
	public void setAsmContact(String asmContact) {
		this.asmContact = asmContact;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getArcOr() {
		return arcOr;
	}
	public void setArcOr(String arcOr) {
		this.arcOr = arcOr;
	}
	public String getArc() {
		return arc;
	}
	public void setArc(String arc) {
		this.arc = arc;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTimings() {
		return timings;
	}
	public void setTimings(String timings) {
		this.timings = timings;
	}
	public String getAvailabilityMc() {
		return availabilityMc;
	}
	public void setAvailabilityMc(String availabilityMc) {
		this.availabilityMc = availabilityMc;
	}
	public String getAvailabilitySim() {
		return availabilitySim;
	}
	public void setAvailabilitySim(String availabilitySim) {
		this.availabilitySim = availabilitySim;
	}
	public String getSpocPhone() {
		return spocPhone;
	}
	public void setSpocPhone(String spocPhone) {
		this.spocPhone = spocPhone;
	}
	public String getRm() {
		return rm;
	}
	public void setRm(String rm) {
		this.rm = rm;
	}
	public String getRmMail() {
		return rmMail;
	}
	public void setRmMail(String rmMail) {
		this.rmMail = rmMail;
	}
	public String getRmPh() {
		return rmPh;
	}
	public void setRmPh(String rmPh) {
		this.rmPh = rmPh;
	}
	public String getRequestor() {
		return requestor;
	}
	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}
	public String getRequestorLoc() {
		return requestorLoc;
	}
	public void setRequestorLoc(String requestorLoc) {
		this.requestorLoc = requestorLoc;
	}
	public String getRequestorPh() {
		return requestorPh;
	}
	public void setRequestorPh(String requestorPh) {
		this.requestorPh = requestorPh;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;	
	}
	public String getVasName() {
		return vasName;
	}
	public void setVasName(String vasName) {
		this.vasName = vasName;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public String getDeactivationCode() {
		return deactivationCode;
	}
	public void setDeactivationCode(String deactivationCode) {
		this.deactivationCode = deactivationCode;
	}
	public String getCharges() {
		return charges;
	}
	public void setCharges(String charges) {
		this.charges = charges;
	}
	public String getBenefits() {
		return benefits;
	}
	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}
	public String getConstruct() {
		return construct;
	}
	public void setConstruct(String construct) {
		this.construct = construct;
	}
	
	
	
	
	
	
	
	
}
