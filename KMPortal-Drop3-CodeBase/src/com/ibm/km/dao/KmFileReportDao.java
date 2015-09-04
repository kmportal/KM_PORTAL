/*
 * Created on Apr 4, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.dto.FileReportDto;
import com.ibm.km.exception.KmException;

/**
 * @author Atishay
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmFileReportDao{
	
	/**
	 * @param circleId
	 * @param categoryId
	 * @param subCategoryId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getApproverList(String circleId, String categoryId, String subCategoryId,String fromDate,String toDate)throws KmException;
	
	/**
	 * @param circleId
	 * @param categoryId
	 * @param subCategoryId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getHitList(String circleId, String categoryId, String subCategoryId,String fromDate,String toDate)throws KmException;
	
	/**
	 * @param elementId
	 * @param selectedDate
	 * @return
	 * @throws KmException
	 */
	public String getAddedFileCount(String elementId,String selectedDate) throws KmException;
	
	/**
	 * @param elementId
	 * @param selectedDate
	 * @return
	 * @throws KmException
	 */
	public int getDeletedFileList(String elementId,String selectedDate) throws KmException;
	
	/**
	 * @param elementId
	 * @param selectedDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getFileCircleList(String elementId,String selectedDate) throws KmException;
	
	/**
	 * @param elementId
	 * @param selectedDate
	 * @return
	 * @throws KmException
	 */
	public ArrayList getFileApprovedList(String elementId,String selectedDate) throws KmException;
	
//	public int getAddedFileCount(String elementId,String selectedDate) throws KmException;
	public int getTotalFileCount(String elementId) throws KmException;
	/**
	 * @param string
	 * @param selectedDate
	 * @return
	 */
	public String getDeletedFileCount(String string, String selectedDate) throws KmException;
	
	/**
	 * @param elementId
	 * @return
	 */
	public FileReportDto getFileCount(String elementId)throws KmException;
	
}
