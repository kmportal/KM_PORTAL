/*
 * Created on Feb 11, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import com.ibm.km.dto.AddFileDTO;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.exception.KmException;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface AddFileDAO {
	

	
	/**
	 * Method to save file information in DB
	 * @param dto
	 * @throws KmException
	 */
	public String saveFileInfoInDB(KmDocumentMstr dto)throws KmException;
	
	/**
	 * Method to check the duplicacy of a file in submitted state.It returns file complete name 
	 * if the same name file exists else returns empty an string.
	 * @param fileName
	 * @return
	 * @throws KmException
	 */
	public KmDocumentMstr checkFile(String parentId, String fileName)throws KmException;
	
	/**
	 * Method to update file details for a duplicate file existing in the DB.
	 * @param oldFileName
	 * @param newFileName
	 * @param userId
	 * @throws KmException
	 */
	public void updateDocumentName(KmDocumentMstr dto)throws KmException;
	/**
	 * Method to save Document
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	public int saveDocumentAsElement(AddFileDTO dto) throws KmException;

}
