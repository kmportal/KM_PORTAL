/*
 * Created on Apr 14, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmAlertMstrDao {

	/**
	 * @param dto
	 * insert a record for a Alert in the database table KM_SCROLL_MSTR
	 */
	public int insert(KmAlertMstr dto) throws KmException;
	
	/**
	 * @param circleId
	 * @return
	 * @throws KmException
	 */
	public String getAlertMessage(String circleId)throws KmException;
	/**
	 * @param dto
	 * @return
	 */
	public ArrayList getAlertList(KmAlertMstr dto)throws KmException;
	/**
	 * @param dto
	 * @return
	 */
	public String deleteAlert(KmAlertMstr dto)throws KmException;
	
	/*KM phase II alerthistory dao*/
	/**
	* @param string parentId(elementId)
	* @returns an array list 
	**/
	public ArrayList getAlertHistoryReport(String parentId,String timeInterval) throws KmException;

	/*KM phase II alerthistory dao*/
	/**
	* @param string parentId(elementId)
	* @returns an array list 
	**/
	public ArrayList getAlerts(String parentId,String time) throws KmException;
	/* km phase II edit alert*/
	/**
	* @param dto
	* edit a record for a Alert in the database table KM_ALERT_MSTR
	*/
	public int editAlert(KmAlertMstr dto) throws KmException;


}
