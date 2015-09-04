/*
 * Created on Oct 23, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;
import java.util.List;

import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;

/**
 * @author avanagar
 * @version 2.0
 * 
 */
public interface UserService {
	/** 
	
		* Returns different userType
		* @return List
	 **/

	public List getUserTypeService(String actorId) throws KmException;

	/**
	 * To insert user details into Database
	 * @param userMstrDto
	 * @param sessionUserBean
	 */

	public void createUserService(KmUserMstr userMstrDto) throws KmException;

	/**
		 * To edit user details into Database
		 * @param userMstrDto
		 * @param sessionUserBean
		 */

	public int editUserService(KmUserMstr userMstrDto) throws KmException;

	/**
	 * To view the user details
	 * @param actorId
	 * @return List
	 * @throws KmException
	 */

	public List viewUserService(String actorId,String userId) throws KmException;

	/**
	 * 
	 * @param userId
	 * @param userList
	 * @return
	 * @throws KmException
	 */

	public KmUserMstr getEditUserDetails(String userId, ArrayList userList) throws KmException;

	/**
	 * 
	 * @param userLogingId
	 * @return boolean
	 * @throws KmException
	 */

	public boolean checkDuplicateUserLogin(String userLogingId) throws KmException;

	/**
	 * Change password service
	 * @param dto
	 * @throws KmException
	 */
	public int changePasswordService(KmUserMstr dto) throws KmException;

	/**
			 * Returns email id for userLogin Id
			 * @param userLogingId
			 * @return boolean
			 * @throws KmException
			 */

	public KmUserMstr userLoginEmail(String userLogingId) throws KmException;

	/**
				 * Returns email id for userLogin Id
				 * @param userLogingId
				 * @return boolean
				 * @throws KmException
				 */

	public String pwdMailingService(String userLogingId, String userEmail) throws KmException;
}
