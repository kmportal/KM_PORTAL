/*
 * Created on Feb 16, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package com.ibm.km.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ibm.km.forms.KmSearchFileFormBean;
import com.ibm.km.dto.KmSearchFile;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public interface KmFileService  {
	

	
	/**
	 * Method to view File
	 * @param circleId
	 * @param categoryId
	 * @param subCategoryId
	 * @param userId
	 * @return
	 * @throws KmException
	 */
	public ArrayList viewFileService(String circleId, String categoryId, String subCategoryId, String userId)throws KmException;

    /**
     * Method to delete File 
     * @param documentId
     * @param updatedBy
     * @throws KmException
     */
	public void deleteFileService(String documentId, String updatedBy)throws KmException;


	/**
	 * Method for keyword file search
	 * @param keyword
	 * @param circleId
	 * @param uploadedBy
	 * @return
	 * @throws KmException
	 */
	public ArrayList keywordFileSearch(String keyword, String circleId, String uploadedBy)throws KmException;


}