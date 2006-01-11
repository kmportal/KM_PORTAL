package com.ibm.km.dto;

public class KmRcHeaderDTO {
	
	private int rCId ;
	private String rCHeader;
	//added by ABU for configurable SMS data
	private String isConfigurable;
		
	public void setIsConfigurable(String isConfigurable) {
		this.isConfigurable = isConfigurable;
	}
	public String getIsConfigurable() {
		return isConfigurable;
	}	
	//added till here
	public int getRCId() {
		return rCId;
	}
	public void setRCId(int id) {
		rCId = id;
	}
	public String getRCHeader() {
		return rCHeader;
	}
	public void setRCHeader(String header) {
		rCHeader = header;
	}
	

		
	
	
}
