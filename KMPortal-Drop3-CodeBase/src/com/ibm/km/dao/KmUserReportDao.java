/*
 * Created on Dec 5, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;
import java.util.ArrayList;

import com.ibm.km.exception.KmException;
/**
 * @author harpreet
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmUserReportDao {

	
	/**
	 * @param elementId
	 * @param toDate
	 * @param fromDate
	 * @param elementLevel
	 * @return
	 * @throws KmException
	 */
	public ArrayList getUserLoginList( String elementId,String elementLevel)throws KmException;
	
	/**
	 * @param elementId
	 * @param kmActorId
	 * @return
	 * @throws KmException
	 */
	
	public ArrayList getcircleWisereport(String elementId, String kmActorId, String date) throws KmException;
/**
	* @param elementId
	* @param 
	* @return
	*/
	public ArrayList getAgentList( String elementId,String eleLevelId,String partner)throws KmException;

public ArrayList getLockedUserList(String elementId)throws KmException;
}