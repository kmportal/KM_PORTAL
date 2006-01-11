/*
 * Created on May 15, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dto;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmElementMstr {


	private String elementId="";
	private String elementName="";
	private String documentName="";
	private String elementDesc="";
	private String parentId="";
	private String elementLevel="";
	private String panStatus="N";
	private String status="A";
	private String createdBy="";
	private Timestamp createdDt=null;
	private Timestamp updatedDt=null;
	private String updatedBy="";
	private String path="";
	private String documentId="";
	private String documentCount="";
	private String totalCount="0";
	private String elementStringPath="";
	private ArrayList elementList=null;
	private ArrayList documentList=null;
	private String parentName="";
	private String eleName="";
	private int sequenceNo;
	//added by vishwas for lob wise upload
	
	private String lobStatus="";
	
	// added by Vaishali for adding LOB Name with Circle in Manage Element
	private String lobName="";
	
		
		public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

		public String getLobStatus() {
		return lobStatus;
	}

	public void setLobStatus(String lobStatus) {
		this.lobStatus = lobStatus;
	}


	//end by vishwas for lob wise upload
	private String documentPath=null;
	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	/**
	 * @return
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return
	 */
	public Timestamp getCreatedDt() {
		return createdDt;
	}

	/**
	 * @return
	 */
	public String getElementDesc() {
		return elementDesc;
	}

	/**
	 * @return
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * @return
	 */
	public String getElementLevel() {
		return elementLevel;
	}

	/**
	 * @return
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * @return
	 */
	public String getPanStatus() {
		return panStatus;
	}

	/**
	 * @return
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @return
	 */
	public Timestamp getUpdatedDt() {
		return updatedDt;
	}

	/**
	 * @param string
	 */
	public void setCreatedBy(String string) {
		createdBy = string;
	}

	/**
	 * @param timestamp
	 */
	public void setCreatedDt(Timestamp timestamp) {
		createdDt = timestamp;
	}

	/**
	 * @param string
	 */
	public void setElementDesc(String string) {
		elementDesc = string;
	}

	/**
	 * @param string
	 */
	public void setElementId(String string) {
		elementId = string;
	}

	/**
	 * @param string
	 */
	public void setElementLevel(String string) {
		elementLevel = string;
	}

	/**
	 * @param string
	 */
	public void setElementName(String string) {
		elementName = string;
	}

	/**
	 * @param string
	 */
	public void setPanStatus(String string) {
		panStatus = string;
	}

	/**
	 * @param string
	 */
	public void setParentId(String string) {
		parentId = string;
	}

	/**
	 * @param string
	 */
	public void setStatus(String string) {
		status = string;
	}

	/**
	 * @param string
	 */
	public void setUpdatedBy(String string) {
		updatedBy = string;
	}

	/**
	 * @param timestamp
	 */
	public void setUpdatedDt(Timestamp timestamp) {
		updatedDt = timestamp;
	}
	
	public JSONObject toJSONObject() {
		JSONObject json=new JSONObject();
		try {
			json.put("elementId", elementId);
			json.put("elementName", elementName);
			json.put("parentId", parentId);
			json.put("elementLevel", elementLevel);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return json; 
	}

	public JSONObject toJSONDocPathObject() {
		JSONObject json=new JSONObject();
		try {
			json.put("documentId", documentId);
			json.put("documentName", documentName);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return json; 
	}

	/**
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param string
	 */
	public void setPath(String string) {
		path = string;
	}


	/**
	 * @return
	 */
	public String getDocumentName() {
		return documentName;
	}

	/**
	 * @param string
	 */
	public void setDocumentName(String string) {
		documentName = string;
	}

	/**
	 * @return
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * @param string
	 */
	public void setDocumentId(String string) {
		documentId = string;
	}

	/**
	 * @return
	 */
	public String getDocumentCount() {
		return documentCount;
	}

	/**
	 * @param string
	 */
	public void setDocumentCount(String string) {
		documentCount = string;
	}

	/**
	 * @return
	 */
	public String getTotalCount() {
		return totalCount;
	}

	/**
	 * @param string
	 */
	public void setTotalCount(String string) {
		totalCount = string;
	}

	/**
	 * @return
	 */
	public String getElementStringPath() {
		return elementStringPath;
	}

	/**
	 * @param string
	 */
	public void setElementStringPath(String string) {
		elementStringPath = string;
	}

	/**
	 * @return
	 */
	public ArrayList getDocumentList() {
		return documentList;
	}

	/**
	 * @return
	 */
	public ArrayList getElementList() {
		return elementList;
	}

	/**
	 * @param list
	 */
	public void setDocumentList(ArrayList list) {
		documentList = list;
	}

	/**
	 * @param list
	 */
	public void setElementList(ArrayList list) {
		elementList = list;
	}

	/**
	 * @return
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * @param string
	 */
	public void setParentName(String string) {
		parentName = string;
	}

	/**
	 * @return
	 */
	public String getEleName() {
		return eleName;
	}

	/**
	 * @param string
	 */
	public void setEleName(String string) {
		eleName = string;
	}

	
	public void print() {
		
	/*	//System.out.println("elementId:" + elementId + " ; elementName" + elementName  + " ; documentName" 
				+ documentName  + " ; elementDesc" + elementDesc  + " ; parentId" 
				+ parentId  + " ; elementLevel" + elementLevel  + 
				" ; panStatus" + panStatus  + " ; createdBy" + createdBy );*/
	}
	public String getLobName() {
		return lobName;
	}

	public void setLobName(String lobName) {
		this.lobName = lobName;
	}

}
