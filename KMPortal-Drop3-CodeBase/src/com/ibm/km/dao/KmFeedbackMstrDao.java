/*
 * Created on Apr 28, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.dto.KmFeedbackMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmFeedbackMstrDao {

	/**
	 * @param dto
	 */
	public void insert(KmFeedbackMstr dto)throws KmException;

	/**
	 * @param circleId
	 */
	public ArrayList viewFeedbacks(String[] elementIds,String elementId)throws KmException;
	
	/**
	 * @param sendDate 
	 * @param circleId
	 */
	public ArrayList feedbackReport(String[] elementIds,String elementId, String startDate, String endDate)throws KmException;


	/**
	 * @param readFeedbacks
	 */
	public void readFeedbacks(String[] readFeedbacks,String[] feedbackResp, String[] feedBackId) throws DAOException ;
    
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	public ArrayList feedbackResponse(int userId) throws DAOException ;
	

	public ArrayList feedbackResponseAll(int elementId) throws DAOException ;
}
