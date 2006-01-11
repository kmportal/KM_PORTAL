package com.ibm.km.dto;

import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import com.ibm.km.common.Constants;
import java.io.Serializable;

import com.ibm.km.forms.KmRCContentUploadFormBean;


 /** 
	* @author Naman 
	* Created On Mon Jan 14 13:13:38 IST 2008 
	* Data Trasnfer Object class for table KM_USER_MSTR 
 
 **/ 
public class KmUserMstr implements Serializable {


    private String categoryId ="0";
   
  
//added by vishwas

	private int FavCatId=0;    

	//added by viswas for UD intregation
	private String udId="";
	private String ipaddress="";
	//end by vishwas for UD intregation
	
	//Added by Bhaskar
	
	private String msisdn;


	

	public String getMsisdn() {
		return msisdn;
	}


	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}


	public String getIpaddress() {
		return ipaddress;
	}


	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}


	public String getUdId() {
		return udId;
	}


	public void setUdId(String udId) {
		this.udId = udId;
	}


	public int getFavCatId() {
		return FavCatId;
	}


	public void setFavCatId(int favCatId) {
		FavCatId = favCatId;
	}

//end by vishwas
	private String elementName="";    
    
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

    private String lastLoginTime= "";
    
    private String userLoginStatus=null;
    
	private String favCategoryId;
	
	private String elementId;    
    
	private String newPassword;
   
	private String oldPassword;
	
	
	private int alertForSessionExpiry=0;
	
	//private Calendar sessionExpiry=null;
    
	private long alertUpdateTime=0;
	
	private boolean csr = false;
	
	/*km Phase II */
	
	private String partnerName="";
	private String categoryName="";
	private ArrayList moduleList;
	private HashMap categoryList;
	private HashMap ModuleData;
	private String favCategoryName="";
	
	/* edited by harpreet for phase II report */
	
	private String loginTime;
	private int noOfLoggedInUser;
	private String lobName;
	private String firstName;
	private String lastName;
	private int total;
	private String lockType;
	
	private boolean restricted = false;
	
	private String initialElementPath;
	
	private String process = "";
	private String businessSegment = "";
	private String activity = "";
	private String location = "";
	private String role = "";
	private String circle = "";
	private String lob = "";
	
	//added by ashutosh for login user status report
	private Double totalLoginTime;
	
	//added by Karan for keeping track of session id of the user
	private String sessionID ="";
	
	private List favoriteList;
	// added by parnika for new fields in create user
	
	public List getFavoriteList() {
		return favoriteList;
	}


	public void setFavoriteList(List favoriteList) {
		this.favoriteList = favoriteList;
	}


	private String partner="";
	private String pbxId="";

	
	
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


	public Double getTotalLoginTime() {
		return totalLoginTime;
	}


	public void setTotalLoginTime(Double totalLoginTime) {
		this.totalLoginTime = totalLoginTime;
	}


	public String getProcess() {
		return process;
	}


	public void setProcess(String process) {
		this.process = process;
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


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getCircle() {
		return circle;
	}


	public void setCircle(String circle) {
		this.circle = circle;
	}


	public String getLob() {
		return lob;
	}


	public void setLob(String lob) {
		this.lob = lob;
	}


	public String getInitialElementPath() {
		return initialElementPath;
	}


	public void setInitialElementPath(String initialElementPath) {
		this.initialElementPath = initialElementPath;
	}


	public String getInitialParentId() {
		return initialParentId;
	}


	public void setInitialParentId(String initialParentId) {
		this.initialParentId = initialParentId;
	}


	private String initialParentId;

	

    public boolean isRestricted() {
		return restricted;
	}


	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}


	/** Creates a dto for the KM_USER_MSTR table */
    public KmUserMstr() {
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
        this.userLoginId = userLoginId.toUpperCase();
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

   

	/**
	 * @return
	 */
	public HashMap getCategoryList() {
		return categoryList;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}


	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
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
	public String getUserLoginStatus() {
		return userLoginStatus;
	}

	/**
	 * @param string
	 */
	public void setUserLoginStatus(String string) {
		userLoginStatus = string;
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
	 * @return
	 */
	public String getFavCategoryId() {
		return favCategoryId;
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
	public void setFavCategoryId(String string) {
		favCategoryId = string;
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
	public String getFavCategoryName() {
		return favCategoryName;
	}

	/**
	 * @param string
	 */
	public void setFavCategoryName(String string) {
		favCategoryName = string;
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
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return
	 */
	public String getLobName() {
		return lobName;
	}

	/**
	 * @return
	 */
	public String getLoginTime() {
		return loginTime;
	}

	/**
	 * @return
	 */
	public int getNoOfLoggedInUser() {
		return noOfLoggedInUser;
	}

	/**
	 * @return
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param string
	 */
	public void setFirstName(String string) {
		firstName = string;
	}

	/**
	 * @param string
	 */
	public void setLastName(String string) {
		lastName = string;
	}

	/**
	 * @param string
	 */
	public void setLobName(String string) {
		lobName = string;
	}

	/**
	 * @param string
	 */
	public void setLoginTime(String string) {
		loginTime = string;
	}

	/**
	 * @param i
	 */
	public void setNoOfLoggedInUser(int i) {
		noOfLoggedInUser = i;
	}

	/**
	 * @param i
	 */
	public void setTotal(int i) {
		total = i;
	}


	public int getAlertForSessionExpiry() {
		return alertForSessionExpiry;
	}


	public void setAlertForSessionExpiry(int alertForSessionExpiry) {
		this.alertForSessionExpiry = alertForSessionExpiry;
	}


	public String getLockType() {
		return lockType;
	}


	public void setLockType(String lockType) {
		this.lockType = lockType;
	}

	public long getAlertUpdateTime() {
		return alertUpdateTime;
	}


	public void setAlertUpdateTime(long alertUpdateTime) {
		this.alertUpdateTime = alertUpdateTime;
	}


	public void setCsr(boolean csr) {
		this.csr = csr;
	}


	public boolean isCsr() {
		return ( kmActorId.equals(Constants.CIRCLE_CSR) ||  kmActorId.equals(Constants.CATEGORY_CSR) );
	}


	public String getSessionID() {
		return sessionID;
	}


	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	

	

}
