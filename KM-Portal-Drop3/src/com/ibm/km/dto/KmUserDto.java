/*
 * Created on Mar 24, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dto;

import java.sql.Timestamp;
import java.util.*;
import java.io.Serializable;


public class KmUserDto implements Serializable {


	
	
	private String userId;

	private String userLoginId;

	private String userFname;

	private String userMname=null;

	private String userLname;

	private String userMobileNumber;

	private String userEmailid;

	private String userPassword;

	private java.sql.Timestamp userPsswrdExpryDt=null;

	private java.sql.Timestamp createdDt;

	private String createdBy;

	private java.sql.Timestamp updatedDt=null;

	private String updatedBy=null;

	private String status;

	private String circleId;
    
	private String circleName;

	private String groupId;

	private String kmActorId;
    
	private String kmActorName;

	private String kmOwnerId;

	private String loginAttempted="0";

	private Timestamp lastLoginTime;
    
	private String newPassword;
   
	private String oldPassword;
    
    private String userType;
    
    private String categoryId = "0";
    
    private String categoryName="";
    
    private String elementId ="";
    
    private String elementName="";
    
    private String elementType="";
    
    
    
	private ArrayList moduleList;
	private HashMap categoryList;
	private HashMap ModuleData;


	/** Creates a dto for the KM_USER_MSTR table */
	public KmUserDto() {
	}


 /** 
	* Get userId associated with this object.
	* @return userId
 **/ 

	public String getUserId() {
		return userId;
	}

 /** 
	* Set userId associated with this object.
	* @param userId The userId value to set
 **/ 

	public void setUserId(String userId) {
		this.userId = userId;
	}

 /** 
	* Get userLoginId associated with this object.
	* @return userLoginId
 **/ 

	public String getUserLoginId() {
		return userLoginId;
	}

 /** 
	* Set userLoginId associated with this object.
	* @param userLoginId The userLoginId value to set
 **/ 

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

 /** 
	* Get userFname associated with this object.
	* @return userFname
 **/ 

	public String getUserFname() {
		return userFname;
	}

 /** 
	* Set userFname associated with this object.
	* @param userFname The userFname value to set
 **/ 

	public void setUserFname(String userFname) {
		this.userFname = userFname;
	}

 /** 
	* Get userMname associated with this object.
	* @return userMname
 **/ 

	public String getUserMname() {
		return userMname;
	}

 /** 
	* Set userMname associated with this object.
	* @param userMname The userMname value to set
 **/ 

	public void setUserMname(String userMname) {
		this.userMname = userMname;
	}

 /** 
	* Get userLname associated with this object.
	* @return userLname
 **/ 

	public String getUserLname() {
		return userLname;
	}

 /** 
	* Set userLname associated with this object.
	* @param userLname The userLname value to set
 **/ 

	public void setUserLname(String userLname) {
		this.userLname = userLname;
	}

 /** 
	* Get userMobileNumber associated with this object.
	* @return userMobileNumber
 **/ 

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

 /** 
	* Set userMobileNumber associated with this object.
	* @param userMobileNumber The userMobileNumber value to set
 **/ 

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

 /** 
	* Get userEmailid associated with this object.
	* @return userEmailid
 **/ 

	public String getUserEmailid() {
		return userEmailid;
	}

 /** 
	* Set userEmailid associated with this object.
	* @param userEmailid The userEmailid value to set
 **/ 

	public void setUserEmailid(String userEmailid) {
		this.userEmailid = userEmailid;
	}

 /** 
	* Get userPassword associated with this object.
	* @return userPassword
 **/ 

	public String getUserPassword() {
		return userPassword;
	}

 /** 
	* Set userPassword associated with this object.
	* @param userPassword The userPassword value to set
 **/ 

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

 /** 
	* Get userPsswrdExpryDt associated with this object.
	* @return userPsswrdExpryDt
 **/ 

	public java.sql.Timestamp getUserPsswrdExpryDt() {
		return userPsswrdExpryDt;
	}

 /** 
	* Set userPsswrdExpryDt associated with this object.
	* @param userPsswrdExpryDt The userPsswrdExpryDt value to set
 **/ 

	public void setUserPsswrdExpryDt(java.sql.Timestamp userPsswrdExpryDt) {
		this.userPsswrdExpryDt = userPsswrdExpryDt;
	}

 /** 
	* Get createdDt associated with this object.
	* @return createdDt
 **/ 

	public java.sql.Timestamp getCreatedDt() {
		return createdDt;
	}

 /** 
	* Set createdDt associated with this object.
	* @param createdDt The createdDt value to set
 **/ 

	public void setCreatedDt(java.sql.Timestamp createdDt) {
		this.createdDt = createdDt;
	}

 /** 
	* Get createdBy associated with this object.
	* @return createdBy
 **/ 

	public String getCreatedBy() {
		return createdBy;
	}

 /** 
	* Set createdBy associated with this object.
	* @param createdBy The createdBy value to set
 **/ 

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

 /** 
	* Get updatedDt associated with this object.
	* @return updatedDt
 **/ 

	public java.sql.Timestamp getUpdatedDt() {
		return updatedDt;
	}

 /** 
	* Set updatedDt associated with this object.
	* @param updatedDt The updatedDt value to set
 **/ 

	public void setUpdatedDt(java.sql.Timestamp updatedDt) {
		this.updatedDt = updatedDt;
	}

 /** 
	* Get updatedBy associated with this object.
	* @return updatedBy
 **/ 

	public String getUpdatedBy() {
		return updatedBy;
	}

 /** 
	* Set updatedBy associated with this object.
	* @param updatedBy The updatedBy value to set
 **/ 

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

 /** 
	* Get status associated with this object.
	* @return status
 **/ 

	public String getStatus() {
		return status;
	}

 /** 
	* Set status associated with this object.
	* @param status The status value to set
 **/ 

	public void setStatus(String status) {
		this.status = status;
	}

 /** 
	* Get circleId associated with this object.
	* @return circleId
 **/ 

	public String getCircleId() {
		return circleId;
	}

 /** 
	* Set circleId associated with this object.
	* @param circleId The circleId value to set
 **/ 

	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}

 /** 
	* Get groupId associated with this object.
	* @return groupId
 **/ 

	public String getGroupId() {
		return groupId;
	}

 /** 
	* Set groupId associated with this object.
	* @param groupId The groupId value to set
 **/ 

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

 /** 
	* Get kmActorId associated with this object.
	* @return kmActorId
 **/ 

	public String getKmActorId() {
		return kmActorId;
	}

 /** 
	* Set kmActorId associated with this object.
	* @param kmActorId The kmActorId value to set
 **/ 

	public void setKmActorId(String kmActorId) {
		this.kmActorId = kmActorId;
	}

 /** 
	* Get kmOwnerId associated with this object.
	* @return kmOwnerId
 **/ 

	public String getKmOwnerId() {
		return kmOwnerId;
	}

 /** 
	* Set kmOwnerId associated with this object.
	* @param kmOwnerId The kmOwnerId value to set
 **/ 

	public void setKmOwnerId(String kmOwnerId) {
		this.kmOwnerId = kmOwnerId;
	}

 /** 
	* Get loginAttempted associated with this object.
	* @return loginAttempted
 **/ 

	public String getLoginAttempted() {
		return loginAttempted;
	}

 /** 
	* Set loginAttempted associated with this object.
	* @param loginAttempted The loginAttempted value to set
 **/ 

	public void setLoginAttempted(String loginAttempted) {
		this.loginAttempted = loginAttempted;
	}

 /** 
	* Get lastLoginTime associated with this object.
	* @return lastLoginTime
 **/ 

	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

 /** 
	* Set lastLoginTime associated with this object.
	* @param lastLoginTime The lastLoginTime value to set
 **/ 

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return
	 */
	public HashMap getCategoryList() {
		return categoryList;
	}

	/**
	 * @return
	 */
	public HashMap getModuleData() {
		return ModuleData;
	}

	/**
	 * @return
	 */
	public ArrayList getModuleList() {
		return moduleList;
	}

	/**
	 * @param map
	 */
	public void setCategoryList(HashMap map) {
		categoryList = map;
	}

	/**
	 * @param map
	 */
	public void setModuleData(HashMap map) {
		ModuleData = map;
	}

	/**
	 * @param list
	 */
	public void setModuleList(ArrayList list) {
		moduleList = list;
	}

	/**
	 * @return
	 */
	

	/**
	 * @return
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @return
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param string
	 */
	public void setNewPassword(String string) {
		newPassword = string;
	}

	/**
	 * @param string
	 */
	public void setOldPassword(String string) {
		oldPassword = string;
	}

	/**
	 * @return
	 */
	public String getCircleName() {
		return circleName;
	}

	/**
	 * @return
	 */
	public String getKmActorName() {
		return kmActorName;
	}

	/**
	 * @param string
	 */
	public void setCircleName(String string) {
		circleName = string;
	}

	/**
	 * @param string
	 */
	public void setKmActorName(String string) {
		kmActorName = string;
	}

	/**
	 * @return
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param string
	 */
	public void setUserType(String string) {
		userType = string;
	}

	/**
	 * @return
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @param string
	 */
	public void setCategoryId(String string) {
		categoryId = string;
	}

	/**
	 * @return
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param string
	 */
	public void setCategoryName(String string) {
		categoryName = string;
	}

	/**
	 * @return
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * @param string
	 */
	public void setElementId(String string) {
		elementId = string;
	}

	/**
	 * @return
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * @param string
	 */
	public void setElementName(String string) {
		elementName = string;
	}

	/**
	 * @return
	 */
	public String getElementType() {
		return elementType;
	}

	/**
	 * @param string
	 */
	public void setElementType(String string) {
		elementType = string;
	}

}
