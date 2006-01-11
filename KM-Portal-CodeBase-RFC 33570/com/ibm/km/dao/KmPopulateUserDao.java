/*
 * Created on Oct 18, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.HashMap;
import java.util.List;

import com.ibm.km.dto.KmLogin;
import com.ibm.km.exception.DAOException;

/**
 * @author avanagar
 * @version 2.0
 * 
 */
public interface KmPopulateUserDao {
	/** 
	
		* Select user details from ARC_MODULE_MSTR,ARC_USER_MSTR,ARC_ACTOR_MSTR,ARC_CIRCLE_MSTR table.
		* @param dto  DTO Object holding the data.
		* @return  List 
		* @throws DAOException In case of an error
	 **/

	public List populateValues(KmLogin dto) throws DAOException;

	/** 
	
		* Select category details from ARC_CATEGORY_MSTR table.
		* @return  HashMap 
		* @throws DAOException In case of an error
	**/
//Added by vishwas for UD intregation
	public List populateValuesforUD(String loginid) throws DAOException;
	//end by vishwas for UD intregation
	//public HashMap category() throws DAOException;
}
