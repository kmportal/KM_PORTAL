/*
 * Created on Feb 14, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;
import java.util.List;

import com.ibm.km.exception.KmException;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ApproveFileService {
	
	/**
	 * Method to interact with DAO and get the list of file to approve.
	 * @param circleId
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws KmException
	 */
	ArrayList getFileList(String userId)throws KmException;
	
	/**
	 * Method to approve files.
	 * @param fileIds
	 * @param commentList
	 * @param userId
	 * @throws KmException
	 */
	void approveFiles(String[] fileIds,String[] commentList,String userId)throws KmException;
	
	/**
	 * Method to reject files.
	 * @param fileIds
	 * @param commentList
	 * @param userId
	 * @throws KmException
	 */
	void rejectFiles(String[] fileIds,String[] commentList,String userId)throws KmException;

	
	/**
	 * Method to approve files.
	 * @param documentId
	 * @throws KmException
	 */
	void updateEscalationTime(String documentId,String userId)throws KmException;
	
	/**
	 * Metod to check approve files.
	 * @return
	 * @throws KmException
	 */
	public List checkApprovedFiles() throws KmException;
	
	
	
}
