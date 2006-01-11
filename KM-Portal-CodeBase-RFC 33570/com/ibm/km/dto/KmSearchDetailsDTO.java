package com.ibm.km.dto;

import java.util.Map;

public class KmSearchDetailsDTO {
	// for ARC ------
	private String circle;
	private String zone;
	private String arcOr;
	private String city;
	private String pincode;
	private String arc;
	private String address;
	private String showRoomTiming;
	private String availabilityOfCcDcMachine;
	private String availabilityOfDoctorSim;
   // end for ARC ------
	// for Distributor
	private String stateName;
	private String pinCatagory;
	private String area;
	private String hub;
	private String sfAndSSDName;
	private String sfAndSSDEmail;
	private String sfAndSSDContNo;
	private String tsmName;
	private String tsmMailId;
	private String tsMContNo;
	private String srMngr;
	private String srMngrMailId;
	private String srMngrContNo;
	private String asmName;
	private String asmMailId;
	private String asmContNo;
	// end for distributor
	//for coordinator 
	private String compName;
	private String compSpocName;
	private String compSpocMail;
	private String compSpocCont;
	private String rmMgr;
	private String rmMailId;
	private String rmCont;
	private String requestor;
	private String requestorLocation;
	private String requestorCont;
	private String requestedOn;
	private int srNo;
	// end for coordinator
// for pack
	private String topic;
	private String header;
	private String contents;
	private String fromDt;
	private String toDt;
	// end for pack
	
	// for snb added by Saanya
	private String schemeType;
	private String schemeDesc;
	private String schemeDisplayFlag;
	// end for snb
	
		
	private String columnId;
	private String columnName;
	private String tableName;
		
	private String isConfigurable;
	
	private String vasName;
	private String activationCode;
	private String deactivationCode;
	private Integer charges;
	private String benefits;
	private String construct;
	Map<String, Object> sendSmsColumns;
	
	private String partner;
	private String location;
	private String olmId;
	
	public String getCircle() {
		return circle;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getFromDt() {
		return fromDt;
	}
	public void setFromDt(String fromDt) {
		this.fromDt = fromDt;
	}
	public String getToDt() {
		return toDt;
	}
	public void setToDt(String toDt) {
		this.toDt = toDt;
	}
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	public String getRmMgr() {
		return rmMgr;
	}
	public void setRmMgr(String rmMgr) {
		this.rmMgr = rmMgr;
	}
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	public String getCompSpocName() {
		return compSpocName;
	}
	public void setCompSpocName(String compSpocName) {
		this.compSpocName = compSpocName;
	}
	public String getCompSpocMail() {
		return compSpocMail;
	}
	public void setCompSpocMail(String compSpocMail) {
		this.compSpocMail = compSpocMail;
	}
	public String getCompSpocCont() {
		return compSpocCont;
	}
	public void setCompSpocCont(String compSpocCont) {
		this.compSpocCont = compSpocCont;
	}
	public String getRmMailId() {
		return rmMailId;
	}
	public void setRmMailId(String rmMailId) {
		this.rmMailId = rmMailId;
	}
	public String getRmCont() {
		return rmCont;
	}
	public void setRmCont(String rmCont) {
		this.rmCont = rmCont;
	}
	public String getRequestor() {
		return requestor;
	}
	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}
	public String getRequestorLocation() {
		return requestorLocation;
	}
	public void setRequestorLocation(String requestorLocation) {
		this.requestorLocation = requestorLocation;
	}
	public String getRequestorCont() {
		return requestorCont;
	}
	public void setRequestorCont(String requestorCont) {
		this.requestorCont = requestorCont;
	}
	public String getRequestedOn() {
		return requestedOn;
	}
	public void setRequestedOn(String requestedOn) {
		this.requestedOn = requestedOn;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getPinCatagory() {
		return pinCatagory;
	}
	public void setPinCatagory(String pinCatagory) {
		this.pinCatagory = pinCatagory;
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
	public String getSfAndSSDName() {
		return sfAndSSDName;
	}
	public void setSfAndSSDName(String sfAndSSDName) {
		this.sfAndSSDName = sfAndSSDName;
	}
	public String getSfAndSSDEmail() {
		return sfAndSSDEmail;
	}
	public void setSfAndSSDEmail(String sfAndSSDEmail) {
		this.sfAndSSDEmail = sfAndSSDEmail;
	}
	public String getSfAndSSDContNo() {
		return sfAndSSDContNo;
	}
	public void setSfAndSSDContNo(String sfAndSSDContNo) {
		this.sfAndSSDContNo = sfAndSSDContNo;
	}
	public String getTsmName() {
		return tsmName;
	}
	public void setTsmName(String tsmName) {
		this.tsmName = tsmName;
	}
	public String getTsmMailId() {
		return tsmMailId;
	}
	public void setTsmMailId(String tsmMailId) {
		this.tsmMailId = tsmMailId;
	}
	public String getTsMContNo() {
		return tsMContNo;
	}
	public void setTsMContNo(String tsMContNo) {
		this.tsMContNo = tsMContNo;
	}
	public String getSrMngr() {
		return srMngr;
	}
	public void setSrMngr(String srMngr) {
		this.srMngr = srMngr;
	}
	public String getSrMngrMailId() {
		return srMngrMailId;
	}
	public void setSrMngrMailId(String srMngrMailId) {
		this.srMngrMailId = srMngrMailId;
	}
	public String getSrMngrContNo() {
		return srMngrContNo;
	}
	public void setSrMngrContNo(String srMngrContNo) {
		this.srMngrContNo = srMngrContNo;
	}
	public String getAsmName() {
		return asmName;
	}
	public void setAsmName(String asmName) {
		this.asmName = asmName;
	}
	public String getAsmMailId() {
		return asmMailId;
	}
	public void setAsmMailId(String asmMailId) {
		this.asmMailId = asmMailId;
	}
	public String getAsmContNo() {
		return asmContNo;
	}
	public void setAsmContNo(String asmContNo) {
		this.asmContNo = asmContNo;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public void setCircle(String circle) {
		this.circle = circle;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
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
	public String getShowRoomTiming() {
		return showRoomTiming;
	}
	public void setShowRoomTiming(String showRoomTiming) {
		this.showRoomTiming = showRoomTiming;
	}
	public String getAvailabilityOfCcDcMachine() {
		return availabilityOfCcDcMachine;
	}
	public void setAvailabilityOfCcDcMachine(String availabilityOfCcDcMachine) {
		this.availabilityOfCcDcMachine = availabilityOfCcDcMachine;
	}
	public String getAvailabilityOfDoctorSim() {
		return availabilityOfDoctorSim;
	}
	public void setAvailabilityOfDoctorSim(String availabilityOfDoctorSim) {
		this.availabilityOfDoctorSim = availabilityOfDoctorSim;
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
	public String getIsConfigurable() {
		return isConfigurable;
	}
	public void setIsConfigurable(String isConfigurable) {
		this.isConfigurable = isConfigurable;
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
	public Integer getCharges() {
		return charges;
	}
	public void setCharges(Integer charges) {
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
	public Map<String, Object> getSendSmsColumns() {
		return sendSmsColumns;
	}
	public void setSendSmsColumns(Map<String, Object> sendSmsColumns) {
		this.sendSmsColumns = sendSmsColumns;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOlmId() {
		return olmId;
	}
	public void setOlmId(String olmId) {
		this.olmId = olmId;
	}
	public String getSchemeType() {
		return schemeType;
	}
	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}
	public String getSchemeDesc() {
		return schemeDesc;
	}
	public void setSchemeDesc(String schemeDesc) {
		this.schemeDesc = schemeDesc;
	}
	public String getSchemeDisplayFlag() {
		return schemeDisplayFlag;
	}
	public void setSchemeDisplayFlag(String schemeDisplayFlag) {
		this.schemeDisplayFlag = schemeDisplayFlag;
	}
	
	
}
