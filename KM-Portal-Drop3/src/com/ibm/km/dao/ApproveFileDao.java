/*
 * Created on Feb 15, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.ibm.km.dto.ApproveFileDto;
import com.ibm.km.exception.KmException;

/**
 * @author Pramod
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ApproveFileDao {
	
	/**
	 * This method is used to interact with DB and get the list of files to approve
	 * in the date range selected by user.
	 * @param userId
	 * @param circleId
	 * @param onDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getFileList(String userId)throws KmException;
	
	/**
	 * Method to approve files 
	 * @param fileIds
	 * @param fileList
	 * @param userId
	 * @throws KmException
	 */
	public void approveFile(String[] fileIds,String[] fileList,String userId) throws KmException;

	/**
	 * Method to deactivate Old Files 
	 * @param fileIds
	 * @throws KmException
	 */
	public void deactivateOldFile(String[] fileIds) throws KmException;
	
	/**
	 * Method to reject files
	 * @param fileIds
	 * @param fileList
	 * @param userId
	 * @throws KmException
	 */
	public void rejectFile(String[] fileIds,String[] fileList,String userId) throws KmException;

    /**
     * Method for Escalation
     * @param fileIds
     * @param escalationTime
     * @param escalationCount
     * @param approverId
     * @throws KmException
     */	
	public void updateEscalationTime(String fileIds,String escalationTime,int escalationCount,String approverId) throws KmException;
	
	/**
	 * Method to check approved files
	 * @return
	 * @throws KmException
	 */
	public List checkApprovedFiles() throws KmException;
	
	/**
	 * Method to get upload time
	 * @param documentId
	 * @return
	 * @throws KmException
	 */
	public ApproveFileDto getUploadedTime(String documentId) throws KmException;
	
}
