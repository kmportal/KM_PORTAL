/*
 * Created on Feb 11, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import org.apache.struts.upload.FormFile;

import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.exception.KmException;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface AddFileService {
	

	/**
	 * This method is used to save the file in the workspace.
	 * @param path
	 * @param file
	 * @return Document name if already exists else empty string. 
	 * @throws KmException
	 */
	public void saveFile(String path,FormFile file, String parentId)throws KmException;
	
	/**
	 * This method is used to save the file in the workspace.
	 * @param path
	 * @param file
	 * @return Document name if already exists else empty string. 
	 * @throws KmException
	 */
	public KmDocumentMstr checkFile(FormFile file, String parentId)throws KmException;
	
	/**
	 * Save file info in DB.
	 * @param dto
	 * @throws KmException
	 */
	public String saveFileInfoInDB(KmDocumentMstr dto)throws KmException;
	
	/**
	 * Update file info in DB.
	 * @param oldFileName
	 * @param newFileName
	 * @param userId
	 * @throws KmException
	 */
	public void updateDocumentName(KmDocumentMstr dto)throws KmException;
	
	/**
	 * Update file in workspace.
	 * @param oldPath
	 * @param newPath
	 * @param file
	 * @throws KmException
	 */
	public void updateFile(String oldPath,String newPath,FormFile oldFile)throws KmException;

}
