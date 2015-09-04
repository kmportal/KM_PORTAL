/*
 * Created on Oct 16, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;
import java.util.List;

import com.ibm.km.dto.KmLogin;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

/**
 * @author namangup
 * @version 2.0
 * 
 */
public interface LoginService {

	/**
	 * 
	 * @param loginId
	 * @return
	 * @throws KmException
	 * @throws DAOException
	 */
	
	public boolean isValidUser(String loginId) throws KmException ;
	
	/**
	 * 
	 * @param olmId
	 * @return
	 * @throws KmException
	 * @throws DAOException
	 */
	
	public boolean isValidOlmId(String olmId) throws KmException ;
	
	
	/**
	 * Method to populate User Details
	 * @param login
	 * @return
	 * @throws KmException
	 * @throws DAOException
	 */
	public KmUserMstr populateUserDetails(KmLogin login) throws KmException, DAOException;
	
	//Added by vishwas for UD intregation
	public KmUserMstr populateUserDetailsforUD(String login) throws KmException, DAOException;
	//end by vishwas
	/**
	 * Method to get date
	 * @param d
	 * @return
	 * @throws KmException
	 * @throws DAOException
	 */	
	public String getStringFromDate(java.util.Date d) throws KmException, DAOException;
	
	/**
	 * Method to get Warning
	 * @param ob
	 * @return
	 * @throws KmException
	 * @throws DAOException
	 */
	public String getWarning(Object ob) throws KmException, DAOException;
	
	/**
	 * Method to getConfigValue
	 * @param string
	 * @return
	 * @throws KmException
	 */
	public String getConfigValue(String string) throws KmException;
   
   /**
    * method to get Email id
    * @param userName
    * @return
    * @throws KmException
    */
	public ArrayList getEmailId(String userName)throws KmException;
	
   /**
	 *method to update password 
	 * @param userName
	 * @param encPassword
	 * @throws KmException
	 */
	public void updatePassword(String userName, String encPassword)throws KmException;
	
	 public int getExpiredDocumentCount(KmUserMstr userBean) throws KmException;
	 
	 public List getFavorites(int userId) throws KmException;

	public int getUserIdForUD(String userId)throws KmException;
	 
	
}
