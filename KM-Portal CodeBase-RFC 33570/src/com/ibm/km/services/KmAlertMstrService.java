/*
 * Created on Apr 14, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;

import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmAlertMstrService {

	/**
	 * Method to create Alerts
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	public int createAlert(KmAlertMstr dto) throws KmException;
	
	/**
	 * Method to retrieve Alert Message
	 * @param circleId
	 * @return
	 * @throws KmException
	 */
	public String getAlertMessage(String circleId)throws KmException;

	/**
	 * method to retrieve Alert List
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	public ArrayList getAlertList(KmAlertMstr dto)throws KmException;

	/**
	 * Method to Delete Alert
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	public String deleteAlert(KmAlertMstr dto)throws KmException;
	
	/*km pase II view alert by harpreet*/
	/**
     * Method  to retrieve Alerts
     * @param circleId
     * @param time
     * @return
     * @throws KmException
     */
	public ArrayList getAlerts(String circleId,String time) throws KmException;
	
	/* km phase II edit alert by harpreet*/
	/**
	 * Method to Edit the Alerts
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	public int editAlert(KmAlertMstr dto) throws KmException;
		
	/*km pase II alert history by harpreet*/
	/**
	 * Method to retrieve Alerts History report 
	 * @param circleId,alert count
	 * @return
	 * @throws KmException
	 */
	public ArrayList getAlertHistoryReport(String circleId,String timeInterval) throws KmException;
	public String getAlertMessage(KmAlertMstr dto) throws KmException;

}
