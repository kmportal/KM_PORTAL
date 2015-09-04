/*
 * Created on Feb 13, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;

import com.ibm.km.exception.KmException;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface SubmitFileService {
	
	/**
	 * Method to interact with DAO and get the list of file to submit for approval.
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws KmException
	 */
	ArrayList getFileList(String userId,String fromDate,String toDate)throws KmException;
	
	/**
	 * Method to submit files.
	 * @param fileIds
	 * @throws KmException
	 */
	void submitFile(String[] fileIds)throws KmException;
}
