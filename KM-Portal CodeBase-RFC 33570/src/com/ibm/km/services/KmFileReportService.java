
package com.ibm.km.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ibm.km.forms.KmSearchFileFormBean;
import com.ibm.km.dto.FileReportDto;
import com.ibm.km.dto.KmSearchFile;
import com.ibm.km.exception.KmException;

/**
 * @author Atishay
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public interface KmFileReportService  {
	
	/**
	 * Method  for approval
	 * @param circleId
	 * @param categoryId
	 * @param subCategoryId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getApproverService(String circleId, String categoryId, String subCategoryId,String fromDate,String toDate)throws KmException;
	
	/**
	 * Method to get Hit
	 * @param circleId
	 * @param categoryId
	 * @param subCategoryId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getHitService(String circleId, String categoryId, String subCategoryId,String fromDate,String toDate)throws KmException;
	
	/**
	 * Method to get Added File List
	 * @param circleList
	 * @param selectedDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getAddedFileList(ArrayList circleList,String selectedDate)throws KmException;
	
	/**
	 * Method to get Deleted File List
	 * @param elementId
	 * @param selectedDate
	 * @return
	 * @throws KmException
	 */
	public int getDeletedFileList(String elementId,String selectedDate)throws KmException;
	
	/**
	 * Method to get File Circle List
	 * @param elementId
	 * @param selectedDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getFileCircleList(String elementId,String selectedDate)throws KmException;
	
	/**
	 * Method to get File Approver List
	 * @param elementId
	 * @param selectedDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getFileApprovedList(String elementId,String selectedDate)throws KmException;
	
	/**
	 * Method to get Added File Count
	 * @param elementId
	 * @param selectedDate
	 * @return
	 * @throws KmException
	 */
	public int getAddedFileCount(String elementId,String selectedDate)throws KmException;
	
	/**
	 * Method to get Total File Count
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public int getTotalFileCount(String elementId)throws KmException;
	
	/**
	 * Method to get deleted File List
	 * @param circleList
	 * @param selectedDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getDeletedFileList(ArrayList circleList, String selectedDate)throws KmException;
	
	/**
	 * Method to get ApprovedRejected File COunt
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public FileReportDto getApprovedRejectedFileCount(String elementId) throws KmException;

}
