/*
 * Created on Feb 20, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmFileDao {


	/**
	 * @param fileId
	 */
	public void deleteDocument(String documentId, String updatedBy) throws KmException;


	/**
	 * @param circleId
	 * @param categoryId
	 * @param subCategoryId
	 * @param userId
	 * @return
	 */
	public ArrayList viewFiles(String circleId, String categoryId, String subCategoryId, String userId)throws KmException;

	/**
	 * @param keyword
	 * @param circleId
	 * @param uploadedBy
	 * @return file list
	 */
	public ArrayList keywordFileSearch(String keyword, String circleId, String uploadedBy)throws KmException;
	

}
