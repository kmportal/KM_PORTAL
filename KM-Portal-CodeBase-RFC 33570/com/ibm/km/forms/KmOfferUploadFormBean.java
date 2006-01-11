package com.ibm.km.forms;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ibm.km.dto.OfferDetailsDTO;

public class KmOfferUploadFormBean extends ActionForm {
	
	private String bucketId="";
	private ArrayList<OfferDetailsDTO> bucketList = null;
	private String offerTitle="";
	private String offerDesc="";
	private String documentId="";
	private String startDate="";
	private String endDate="";	
	private String methodName="";
	private String createdBy="";
	private String bucketDesc="";
	private String kmActorId="";
	private int offerId = 0;
	
	
	
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public String getKmActorId() {
		return kmActorId;
	}

	public void setKmActorId(String kmActorId) {
		this.kmActorId = kmActorId;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		 bucketId="";
		 bucketList = null;
		 offerTitle="";
	     offerDesc="";
		 startDate="";
		 endDate="";	
		 methodName="";
		 createdBy="";
		 bucketDesc="";
		 offerId=0;
	}
	
	public ArrayList<OfferDetailsDTO> getBucketList() {
		return bucketList;
	}
	public void setBucketList(ArrayList<OfferDetailsDTO> bucketList) {
		this.bucketList = bucketList;
	}
	public String getBucketId() {
		return bucketId;
	}
	public void setBucketId(String bucketId) {
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getBucketDesc() {
		return bucketDesc;
	}
	public void setBucketDesc(String bucketDesc) {
		this.bucketDesc = bucketDesc;
	}
}
