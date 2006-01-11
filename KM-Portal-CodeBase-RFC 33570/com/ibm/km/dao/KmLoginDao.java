/*
 * Created on Jun 11, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;

/**
 * @author Atul
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmLoginDao {
	
	/**
	 * 
	 * @param userLoginId
	 * @return
	 * @throws KmException
	 */
	public boolean isValidUser(String userLoginId) throws KmException ;
	
	/**
	 * 
	 * @param userLoginId
	 * @return
	 * @throws KmException
	 */
	public boolean isValidOlmId(String userLoginId) throws KmException ;
	
	/**
	 * @param userLoginId
	 * @return
	 * @throws KmException
	 */
	public ArrayList getEmailId(String userLoginId) throws KmException;
	
	/**
	 * @param userLoginId
	 * @param password
	 * @throws KmException
	 */
	public void  updatePassword(String userLoginId, String password) throws KmException;
  /**
   * @param string
   * @return
   */
  public String getConfigValue(String string) throws KmException;
  
  public int getExpiredDocumentCount(KmUserMstr userBean) throws KmException;
  // get favorites of logged in user 
  
  public List getFavorites(int userId) throws KmException ;
  
  public Timestamp getLastLogin(String userLoginId) throws KmException;

  public int getUserIdForUD(String userId)throws KmException;
}
