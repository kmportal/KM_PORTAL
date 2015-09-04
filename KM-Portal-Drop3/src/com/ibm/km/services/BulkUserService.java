/*
 * Created on Nov 26, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.io.IOException;
import java.util.ArrayList;

import com.ibm.km.dto.KmFileDto;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface BulkUserService {
	
	/**
	 * Methods for Bulk Uploading
	 * @param filePath
	 * @param userId
	 * @param elementId
	 * @throws IOException
	 * @throws KmException
	 */
   public void bulkUpload()throws IOException, Exception;
			
	/**
	 * Method for bulk Deletion
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws KmException
	 */		
	public KmFileDto bulkDelete(String filePath)throws IOException, KmException;

	/**
	 * Method to retrieve Bulk upload Details
	 * @param string
	 * @return
	 */
//modify by vishwas
	public ArrayList<KmFileDto> getBulkUploadDetails() throws Exception;
	
	/**
	 * Method to retrieve Bulk delete Details 
	 * @param string
	 * @return
	 * @throws KmException
	 */
	 
	public ArrayList getBulkDeleteDetails(String string) throws KmException;


	/**
	 * Method to Upload a File
	 * @param fileDto
	 * @return
	 * @throws KmException
	 */
	 
	public int uploadFile(KmFileDto fileDto)throws KmException;


	/**
	 * Method  to Update File Status
	 * @param dto
	 * @throws KmException
	 */
	 
	public void updateFileStatus(KmFileDto dto)throws KmException;

    /**
     * Method to retrieve Bulk User Files
     * @param date
     * @return
     * @throws KmException
     */
	public ArrayList getBulkUserFiles(String circleId,String date) throws KmException;

}
