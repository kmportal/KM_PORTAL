/*
 * Created on Feb 1, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;

import com.ibm.km.dto.KmActorMstr;
import com.ibm.km.dto.KmUserDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public interface KmUserMstrService  {
	

	/**
	 * Method to create User
	 * @param userMstrDto
	 * @throws KmException
	 */
	public int createUserService(KmUserMstr userMstrDto) throws KmException;

	/**
	 * Method to edit User
	 * @param userMstrDto
	 * @return
	 * @throws KmException
	 */	
	public int editUserService(KmUserMstr userMstrDto) throws KmException;

	/**
	 * Method for duplicate Check
	 * @param userLogingId
	 * @return
	 * @throws DAOException
	 * @throws KmException
	 */
	public boolean checkDuplicateUserLogin(String userLogingId) throws DAOException,KmException;

	/**
	 * Methode to change password
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	public int changePasswordService(KmUserMstr dto) throws KmException;

    /**
     * Method to view users
     * @param loginActorId
     * @param userId
     * @return
     * @throws KmException
     */
	public ArrayList viewUsers(String loginActorId, String userId) throws KmException;
	//added by vishwas
	public ArrayList<KmActorMstr> getActorList() throws KmException;
	public ArrayList viewUsers(String loginActorId, String kmactorId, String userId) throws KmException;
	//end by vishwas
	
	/**
	 * Method to view users
	 * @param userId
	 * @return
	 * @throws KmException
	 */
	public KmUserDto getUserService(String userId) throws KmException;

	/**
	 * Method to retrieve Elements
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public String[] getElementUsers(String elementId) throws KmException;

	/**
	 * Method to get favourite category
	 * @param string
	 * @return
	 * @throws KmException
	 */
	public String getFavCategory(String string) throws KmException;

	/**
	 * Method to get no of users
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public int noOfElementUsers(String elementId)throws KmException;

	/**
	 * Method for checking favourite
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public boolean checkForFavourite(String elementId)throws KmException;
	
	/**
	 * Method to get managers
	 * @param circleId
	 * @param escalationCount
	 * @return
	 * @throws KmException
	 */
	public KmUserMstr getManagers(String circleId,int escalationCount)throws KmException;

	/**
	 * Method to get Approver
	 * @param circleId
	 * @return
	 * @throws KmException
	 */
	public String getApprover(String circleId)throws KmException;
	
	/**
	 * Method to delete user
	 * @param userId
	 * @return
	 * @throws KmException
	 */
//modify by vishwas
	public int deleteUserService(String userId) throws Exception;

	/**
	 * Method to insert user
	 * @param user
	 * @return
	 * @throws com.ibm.km.exception.DAOException
	 * @throws KmException
	 */
	public int insertUserData(KmUserMstr user) throws com.ibm.km.exception.DAOException,Exception;

	/**
	 * Method to update user Status
	 * @param userBean
	 * @throws KmException
	 */
	
	public void updateUserStatus(KmUserMstr userBean) throws KmException;

	/**
	 * Method to get user Status
	 * @param userLoginId
	 * @return
	 * @throws KmException
	 */
	public String getUserStatus(String userLoginId) throws KmException;
	
	/* KM Phase II partner list retreival service by harpreet*/
	/**
	 * method to get partner Name
	 * @return
	 * @throws KmException
	 */
	public ArrayList getPartnerName() throws KmException;
		
	/**
	 * Method to check user login Id
	 * @param user_login_id
	 * @return
	 * @throws KmException
	 * @throws DAOException
	 */
	public KmUserMstr checkUserLoginId(String user_login_id)throws Exception,DAOException;

	public void updateSessionExpiry(KmUserMstr sessionUserBean) throws KmException;

	public KmUserMstr userIdFromMobile(String mobileNumber) throws KmException;
	
	public KmUserMstr userIdFromMobile(String mobileNumber,String actorIDs) throws KmException;
	
	/*
	 * Method added by Karan
	 * */
	public boolean validateLastThreePasswords(String userLoginId,String password) throws KmException;

	
}
