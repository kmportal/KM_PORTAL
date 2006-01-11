package com.ibm.km.forms;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ibm.km.dto.KmActorMstr;
import com.ibm.km.dto.KmUserDto;

import javax.servlet.http.*;


 /** 
	* @author Anil 
	* Created On Mon Jan 14 13:13:38 IST 2008 
	* Form Bean class for table KM_USER_MSTR 
 
 **/ 
public class KmUserMstrFormBean extends ActionForm {

	private String selectedCombo="";
	
	private String categoryId="0" ;
	
	private String levelCheck="same_level";
	
	private int iterator ;
	
	private int userLevelId;
	
	private String userLevelName;
	//added by vishwas
	private ArrayList<KmActorMstr> actorList = null;
	public ArrayList<KmActorMstr> getActorList() {
		return actorList;
	}


	public void setActorList(ArrayList<KmActorMstr> actorList) {
		this.actorList = actorList;
	}





	private int selectedActorId;
	private String actorId;
	public String getActorId() {
		return actorId;
	}


	public void setActorId(String actorId) {
		this.actorId = actorId;
	}





	private String actorName;
	//end by vishwas
	
	public String getActorName() {
		return actorName;
	}


	public void setActorName(String actorName) {
		this.actorName = actorName;
	}


	


	public int getSelectedActorId() {
		return selectedActorId;
	}


	public void setSelectedActorId(int selectedActorId) {
		this.selectedActorId = selectedActorId;
	}


	


	private String[] comboCaption= new String[100];
	
	private ArrayList lobList = null;
	
	private ArrayList circleList = null;
		
	private ArrayList categoryList = null;
	
	private HashMap elementMap=null;
    
    private String elementId="";
    
    private String elementName="";
    
    private String parentId="";
   
    private String userId=null;

    private String userLoginId=null;

    private String userFname=null;

    private String userMname=null;

    private String userLname=null;

    private String userMobileNumber=null;

    private String userEmailid=null;

    private String userPassword=null;

    private java.sql.Date userPsswrdExpryDt=null;

    private java.sql.Date createdDt=null;

    private String createdBy=null;

    private java.sql.Date updatedDt=null;

    private String updatedBy=null;

    private String status=null;

    private String circleId=null;

    private String groupId=null;

    private String kmActorId=null;

    private String kmOwnerId=null;

    private String loginAttempted=null;

    private java.sql.Date lastLoginTime=null;
    
	private String userConfirmPassword=null;
	
	private ArrayList userType;
	
	private ArrayList circleDetails;
	
	private String kmLoginActorId;
	
	private String userStatus=null;
	
	private KmUserDto userDetails = null;
	
	private String methodName;
	
	private ArrayList userList;
	
	private String message=null;
	
	private String selectedUserId=null;
	
	private String categoryName = "";
	
	private ArrayList elementList = null;
	
	private String elementType="";
	
	private String favCategoryId;
	
	// Added By Parnika
	
	private String initialLevel;
	private String roleId;
	private String circleElementId;
	
	private String partner="";
	private String pbxId="";
	private String businessSegment="";
	private String activity="";
	private String role="";
	



	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getPartner() {
		return partner;
	}


	public void setPartner(String partner) {
		this.partner = partner;
	}


	public String getPbxId() {
		return pbxId;
	}


	public void setPbxId(String pbxId) {
		this.pbxId = pbxId;
	}


	public String getBusinessSegment() {
		return businessSegment;
	}


	public void setBusinessSegment(String businessSegment) {
		this.businessSegment = businessSegment;
	}


	public String getActivity() {
		return activity;
	}


	public void setActivity(String activity) {
		this.activity = activity;
	}


	/** Creates a dto for the KM_USER_MSTR table */
    public KmUserMstrFormBean() {
    }


    public ActionErrors validate(
        ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        //TODO Replace with Actual code.This method is called if validate for this action mapping is set to true. and iff errors is not null and not emoty it forwards to the page defined in input attritbute of the ActionMapping
        return errors;
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

    public java.sql.Date getUserPsswrdExpryDt() {
        return userPsswrdExpryDt;
    }

 /** 
	* Set userPsswrdExpryDt associated with this object.
	* @param userPsswrdExpryDt The userPsswrdExpryDt value to set
 **/ 

    public void setUserPsswrdExpryDt(java.sql.Date userPsswrdExpryDt) {
        this.userPsswrdExpryDt = userPsswrdExpryDt;
    }

 /** 
	* Get createdDt associated with this object.
	* @return createdDt
 **/ 

    public java.sql.Date getCreatedDt() {
        return createdDt;
    }

 /** 
	* Set createdDt associated with this object.
	* @param createdDt The createdDt value to set
 **/ 

    public void setCreatedDt(java.sql.Date createdDt) {
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

    public java.sql.Date getUpdatedDt() {
        return updatedDt;
    }

 /** 
	* Set updatedDt associated with this object.
	* @param updatedDt The updatedDt value to set
 **/ 

    public void setUpdatedDt(java.sql.Date updatedDt) {
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

    public java.sql.Date getLastLoginTime() {
        return lastLoginTime;
    }

 /** 
	* Set lastLoginTime associated with this object.
	* @param lastLoginTime The lastLoginTime value to set
 **/ 

    public void setLastLoginTime(java.sql.Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

	/**
	 * @return confirmPassword value associated with the object
	 */
	public String getUserConfirmPassword() {
		return userConfirmPassword;
	}

	/**
	 * @param string. The confirmPassword value is set to string.
	 */
	public void setUserConfirmPassword(String string) {
		userConfirmPassword = string;
	}

	/**
	 * @return the circleDetails and set to an ArrayList.
	 */
	public ArrayList getCircleDetails() {
		return circleDetails;
	}

	/**
	 * @param list.circleDetails is set to list
	 */
	public void setCircleDetails(ArrayList list) {
		circleDetails = list;
	}

	/**
	 * @return the userType asscoiated with the object.
	 */
	public ArrayList getUserType() {
		return userType;
	}

	/**
	 * @param list. The value userType is set to list.
	 */
	public void setUserType(ArrayList list) {
		userType = list;
	}

	/**
	 * @return kmLoginActorId
	 */
	public String getKmLoginActorId() {
		return kmLoginActorId;
	}

	/**
	 * @param string. The value kmLoginActorId is set to string.
	 */
	public void setKmLoginActorId(String string) {
		kmLoginActorId = string;
	}

	/**
	 * @return the status asscociated with the object.
	 */
	public String getUserStatus() {
		return userStatus;
	}

	/**
	 * @param string. The userStatus is set to string.
	 */
	public void setUserStatus(String string) {
		userStatus = string;
	}

	/**
	 * @return
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param string
	 */
	public void setMethodName(String string) {
		methodName = string;
	}

	/**
	 * @return
	 */
	public ArrayList getUserList() {
		return userList;
	}

	/**
	 * @param list
	 */
	public void setUserList(ArrayList list) {
		userList = list;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	
	

	/**
	 * @return
	 */
	public KmUserDto getUserDetails() {
		return userDetails;
	}

	/**
	 * @param string
	 */
	public void setMessage(String string) {
		message = string;
	}

	/**
	 * @param map
	 */
	public void setUserDetails(KmUserDto map) {
		userDetails = map;
	}

	/**
	 * @return
	 */
	public String getSelectedUserId() {
		return selectedUserId;
	}

	/**
	 * @param string
	 */
	public void setSelectedUserId(String string) {
		selectedUserId = string;
	}

	/**
	 * @return
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @return
	 */
	public ArrayList getCategoryList() {
		return categoryList;
	}

	/**
	 * @param string
	 */
	public void setCategoryId(String string) {
		categoryId = string;
	}

	/**
	 * @param string
	 */
	public void setCategoryList(ArrayList string) {
		categoryList = string;
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
	 * @return
	 */
	public String getParentId() {
		return parentId;
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
	public void setParentId(String string) {
		parentId = string;
	}

	/**
	 * @return
	 */
	public ArrayList getCircleList() {
		return circleList;
	}

	/**
	 * @return
	 */
	public ArrayList getLobList() {
		return lobList;
	}

	/**
	 * @param list
	 */
	public void setCircleList(ArrayList list) {
		circleList = list;
	}

	/**
	 * @param list
	 */
	public void setLobList(ArrayList list) {
		lobList = list;
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
	public HashMap getElementMap() {
		return elementMap;
	}

	/**
	 * @param map
	 */
	public void setElementMap(HashMap map) {
		elementMap = map;
	}

	/**
	 * @return
	 */
	public String getComboCaption(int index) {
		return comboCaption[index];
	}

	/**
	 * @param strings
	 */
	public void setComboCaption(int index, String value) {
		comboCaption[index] = value;
	}

	/**
	 * @return
	 */
	public int getUserLevelId() {
		return userLevelId;
	}

	/**
	 * @return
	 */
	public String getUserLevelName() {
		return userLevelName;
	}

	/**
	 * @param strings
	 */
	public void setComboCaption(String[] strings) {
		comboCaption = strings;
	}

	/**
	 * @param i
	 */
	public void setUserLevelId(int i) {
		userLevelId = i;
	}

	/**
	 * @param string
	 */
	public void setUserLevelName(String string) {
		userLevelName = string;
	}

	/**
	 * @return
	 */
	public int getIterator() {
		return iterator;
	}

	/**
	 * @param i
	 */
	public void setIterator(int i) {
		iterator = i;
	}

	/**
	 * @return
	 */
	public String getLevelCheck() {
		return levelCheck;
	}

	/**
	 * @param string
	 */
	public void setLevelCheck(String string) {
		levelCheck = string;
	}

	/**
	 * @return
	 */
	public String getSelectedCombo() {
		return selectedCombo;
	}

	/**
	 * @param string
	 */
	public void setSelectedCombo(String string) {
		selectedCombo = string;
	}

	/**
	 * @return
	 */
	public String[] getComboCaption() {
		return comboCaption;
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
	public void setElementList(ArrayList list) {
		elementList = list;
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

	/**
	 * @return
	 */
	public String getFavCategoryId() {
		return favCategoryId;
	}

	/**
	 * @param string
	 */
	public void setFavCategoryId(String string) {
		favCategoryId = string;
	}


	public String getInitialLevel() {
		return initialLevel;
	}


	public void setInitialLevel(String initialLevel) {
		this.initialLevel = initialLevel;
	}


	public String getRoleId() {
		return roleId;
	}


	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


	public String getCircleElementId() {
		return circleElementId;
	}


	public void setCircleElementId(String circleElementId) {
		this.circleElementId = circleElementId;
	}

}
