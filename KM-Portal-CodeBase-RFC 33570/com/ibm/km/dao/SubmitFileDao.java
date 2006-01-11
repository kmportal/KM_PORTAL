/*
 * Created on Feb 13, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.exception.KmException;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface SubmitFileDao {
	
	/**
	 * Method to the list fo files to submit for approval in the selected date range of the logged in user.
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws KmException
	 */
	ArrayList getFileList(String userId, String fromDate,String toDate)throws KmException;
	
	/**
	 * Method to submit files.
	 * @param fileIds
	 * @throws KmException
	 */
	void submitFile(String[] fileIds)throws KmException;
	
}
