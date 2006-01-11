/*
 * Created on Nov 26, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dto;

import java.io.File;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmFileDto {
	
	private String fileName="";
	private String errorMessage="";
	private String status="";
	private String loginIds="";
	private String fileId="";
	private String totalUsers="";
	private String usersCreated="";
	private String usersDeleted="";
	private String filePath="";
	private String elementId="";
	private String uploadedBy="";
	private String partnerName="";
	private String usersUpdated="";
	private String fileType="";
	private File errorFile=null;
	//added by vishwas
	private int elementId1;
	public int getElementId1() {
		return elementId1;
	}

	public void setElementId1(int elementId1) {
		this.elementId1 = elementId1;
	}

	public int getElement_level_id() {
		return element_level_id;
	}

	public void setElement_level_id(int element_level_id) {
		this.element_level_id = element_level_id;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	private int element_level_id;	
	private int parent_id;
	//end by vishwas

	

	public File getErrorFile() {
		return errorFile;
	}

	public void setErrorFile(File errorFile) {
		this.errorFile = errorFile;
	}

	/**
	 * @return
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return
	 */
	public String getLoginIds() {
		return loginIds;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param string
	 */
	public void setErrorMessage(String string) {
		errorMessage = string;
	}

	/**
	 * @param string
	 */
	public void setFileName(String string) {
		fileName = string;
	}

	/**
	 * @param string
	 */
	public void setLoginIds(String string) {
		loginIds = string;
	}

	/**
	 * @param string
	 */
	public void setStatus(String string) {
		status = string;
	}

	/**
	 * @return
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * @param string
	 */
	public void setFileId(String string) {
		fileId = string;
	}

	/**
	 * @return
	 */
	public String getUsersCreated() {
		return usersCreated;
	}

	/**
	 * @return
	 */
	public String getUsersDeleted() {
		return usersDeleted;
	}

	/**
	 * @param string
	 */
	public void setUsersCreated(String string) {
		usersCreated = string;
	}

	/**
	 * @param string
	 */
	public void setUsersDeleted(String string) {
		usersDeleted = string;
	}

	/**
	 * @return
	 */
	public String getTotalUsers() {
		return totalUsers;
	}

	/**
	 * @param string
	 */
	public void setTotalUsers(String string) {
		totalUsers = string;
	}

	/**
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param string
	 */
	public void setFilePath(String string) {
		filePath = string;
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
	public String getUploadedBy() {
		return uploadedBy;
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
	public void setUploadedBy(String string) {
		uploadedBy = string;
	}

	/**
	 * @return
	 */
	public String getPartnerName() {
		return partnerName;
	}

	/**
	 * @param string
	 */
	public void setPartnerName(String string) {
		partnerName = string;
	}

	/**
	 * @return
	 */
	public String getUsersUpdated() {
		return usersUpdated;
	}

	/**
	 * @param string
	 */
	public void setUsersUpdated(String string) {
		usersUpdated = string;
	}

	/**
	 * @return
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param string
	 */
	public void setFileType(String string) {
		fileType = string;
	}

}
