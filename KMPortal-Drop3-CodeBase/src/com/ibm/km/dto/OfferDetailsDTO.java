package com.ibm.km.dto;

import java.util.Date;


public class OfferDetailsDTO {

	private int offerId=0;	
	private int bucketId=0;
	private String bucketDesc="";
	private String offerTitle="";
	private String offerDesc="";
	private Date startDate=null;
	private Date endDate=null;
	
	
	public String getBucketDesc() {
		return bucketDesc;
	}
	public void setBucketDesc(String bucketDesc) {
		this.bucketDesc = bucketDesc;
	}
	public int getOfferId() {
		return offerId;
	}
	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}
	public int getBucketId() {
		return bucketId;
	}
	public void setBucketId(int bucketId) {
		this.bucketId = bucketId;
	}
	public String getOfferTitle() {
		return offerTitle;
	}
	public void setOfferTitle(String offerTitle) {
		this.offerTitle = offerTitle;
	}
	public String getOfferDesc() {
		return offerDesc;
	}
	public void setOfferDesc(String offerDesc) {
		this.offerDesc = offerDesc;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}