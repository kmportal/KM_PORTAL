/* 
 * KmUserMstrDao.java
 * Created: January 14, 2008
 * 
 * 
 */ 

package com.ibm.km.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.ibm.km.dto.*;
import com.ibm.km.exception.*;


/** 
  * DAO for the <b>KM_USER_MSTR</b> database table.
  * Provides methods to perform insert, update, delete and select queries.
  * <br><pre>
  * Table: KM_USER_MSTR
  * ----------------------------------------------
  *     column: USER_ID        NUMBER null
  *     column: USER_LOGIN_ID        VARCHAR2 null
  *     column: USER_FNAME        VARCHAR2 null
  *     column: USER_MNAME        VARCHAR2 null
  *     column: USER_LNAME        VARCHAR2 null
  *     column: USER_MOBILE_NUMBER        VARCHAR2 null
  *     column: USER_EMAILID        VARCHAR2 null
  *     column: USER_PASSWORD        VARCHAR2 null
  *     column: USER_PSSWRD_EXPRY_DT        DATE null
  *     column: CREATED_DT        DATE null
  *     column: CREATED_BY        NUMBER null
  *     column: UPDATED_DT        DATE null
  *     column: UPDATED_BY        NUMBER null
  *     column: STATUS        CHAR null
  *     column: CIRCLE_ID        NUMBER null
  *     column: GROUP_ID        NUMBER null
  *     column: KM_ACTOR_ID        NUMBER null
  *     column: KM_OWNER_ID        NUMBER null
  *     column: LOGIN_ATTEMPTED        NUMBER null
  *     column: LAST_LOGIN_TIME        DATE null
  * 
  * Primary Key(s):  USER_ID
  * </pre>
  * 
  */ 
public interface KmUserMstrDao {

 /** 

	* Inserts a row in the KM_USER_MSTR table.
	* @param dto  DTO Object holding the data to be inserted.
	* @return  The no. of rows inserted. 
	* @throws KmUserMstrDaoException In case of an error
 **/ 

    public  int insert(KmUserMstr dto) throws KmException;

 /** 

	* Updates a row in the KM_USER_MSTR table.
	* The Primary Key Object determines wich row gets updated.
	* @param dto  DTO Object holding the data to be updated. The primary key vales determine which row will be updated
	* @return  The no. of rows updated. 
	* @throws KmUserMstrDaoException In case of an error
 **/ 

    public  int update(KmUserMstr dto) throws KmException;



	
	/**
	 * Checking for duplicate user Id 
	 * @param userLoginId
	 * @return
	 * @throws DAOException
	 */
	
	public boolean CheckUserId(String userLoginId) throws DAOException,KmException;
	
	/**
	 * To update the password
	 * @param dto
	 * @return
	 * @throws DAOException
	 */
	public int updatePassword(KmUserMstr dto) throws KmException;
	

	/**
	 * @return list of circle approvers
	 * @throws KmUserMstrDaoException
	 */
	public ArrayList viewCircleApprovers()throws KmException;

	/**
	 * @param circleId
	 * @return list of circle users and CSRs
	 * @throws KmUserMstrDaoException
	 */
	public ArrayList viewCircleUsers(String circleId)throws KmException;

	/**
	 * @param userId
	 * @return a HashMap object of the user 
	 */
	public KmUserDto selectUser(String userId) throws KmException;

	/**
	 * @param circleId
	 * @return circleList
	 */
	public ArrayList viewCsrs(String circleId)throws KmException;

	/**
	 * @param loginActorId
	 * @param userId
	 * @return
	 */
	public ArrayList viewUsers(String loginActorId, String userId)throws KmException;
//added by vishwas
	public ArrayList viewUsers(String loginActorId,String kmactorid, String userId)throws KmException;
	public ArrayList<KmActorMstr> getActorList()throws KmException;

	//end by vishwas
	
	/**
	 * @param elementId(circleId)
	 * @return Element Users(circle users)
	 */
	public String[] getElementsUsers(String elementId)throws KmException;

	/**
	 * @param userId
	 * @return
	 */
	public String getFavCategory(String userId) throws KmException;

	/**
	 * @param elementId
	 * @return
	 */
	public int noOfElementUsers(String elementId)throws KmException;

	/**
	 * @param elementId
	 * @return
	 */
	public boolean checkForFavourite(String elementId)throws KmException;

	/**
	 * @param circleId
	 * @return
	 */
	public KmUserMstr getManagers(String circleId,int escalationCount)throws KmException;

	/**
	 * @param circleId
	 * @return
	 */
	public String getApprover(String circleId)throws KmException;

	/**
	 * @param userLoginId
	 */
	public int deleteUser(String userLoginId) throws Exception;

	/**
	 * @param user
	 * @return
	 */
	public int insertUserData(KmUserMstr user) throws DAOException,Exception;

	/**
	 * @param userMstrDto
	 */
	public void updateUserStatus(KmUserMstr userMstrDto)throws KmException;
	
	public void updateSessionExpiry(KmUserMstr userMstrDto)throws KmException;

	/**
	 * @param user
	 * @return
	 */
	public int bulkUpdate(KmUserMstr user) throws Exception;

	/**
	 * @param userLoginId
	 * @return
	 */
	public String getUserStatus(String userLoginId)throws KmException;

	/*KM phase II partner dao*/
		/**
		 * @param 
		 * @return dto
		 */
	public ArrayList getPartner() throws KmException;
		
	/**
	 * @param user_login_id
	 * @return
	 * @throws KmException
	 * @throws DAOException
	 */
//modify by vishwas
	public KmUserMstr CheckUserLoginId(String user_login_id)throws Exception,DAOException;

	public KmUserMstr userIdFromMobile(String mobileNumber) throws DAOException;
	
	public KmUserMstr userIdFromMobile(String mobileNumber,String actorIDs) throws DAOException;
	
	public boolean validateLastThreePasswords(String userLoginId,String password) throws KmException,DAOException;

	


}
