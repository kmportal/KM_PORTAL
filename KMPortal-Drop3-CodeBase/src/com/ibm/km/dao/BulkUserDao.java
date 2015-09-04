/*
 * Created on Nov 26, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.dto.KmFileDto;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface BulkUserDao {
	
	public int bulkUserDeleteFile(KmFileDto dto) throws KmException;

	/**
	 * @param fileId
	 * @return
	 */
	//public KmFileDto getBulkUploadDetails() throws KmException;
	
	//modify by vishwas for bulk uploded
	public ArrayList<KmFileDto> getBulkUploadDetails() throws Exception;
	/**
	 * Method for bulk upload
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	public int bulkUserUploadFile(KmFileDto dto) throws KmException;

		/**
		 * @param fileId
		 * @return
		 */
		public ArrayList getBulkDeleteDetails(String fileId) throws KmException;

		/**
		 * @param fileId
		 */
		public void getBulkDeleteDetails(int fileId) throws KmException;

		/**
		 * @param fileId
		 */
		public void updateFileStatus(KmFileDto dto)throws KmException;
       
		/**
		 * Method to get bulk user
		 * @param date
		 * @return
		 * @throws KmException
		 */
		public ArrayList getBulkUserFiles(String circleId,String date)throws KmException;
	
	

}
