/*
 * Created on Feb 20, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.dao.KmFileDao;
import com.ibm.km.dao.impl.KmFileDaoImpl;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmFileService;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmFileServiceImpl implements KmFileService{


	/**
	* Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCategoryMstrServiceImpl.class);
	}
		
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmSearchFileService#viewFileService(java.lang.String)
	 */
	public ArrayList viewFileService(String circleId, String categoryId, String subCategoryId, String userId) throws KmException{
		ArrayList fileList=null;
		KmFileDao dao=new KmFileDaoImpl();
			
			fileList=dao.viewFiles(circleId,categoryId,subCategoryId,userId);
			return fileList;
	}






	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmSearchFileService#deleteFileService(java.lang.String)
	 */
	public void deleteFileService(String documentId, String updatedBy) throws KmException {
		KmFileDao dao=new KmFileDaoImpl();
		dao.deleteDocument(documentId,updatedBy);
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFileService#keywordFileSearch(java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList keywordFileSearch(String keyword, String circleId, String uploadedBy) throws KmException{
		ArrayList fileList=null;
		KmFileDao dao=new KmFileDaoImpl();
		
			fileList=dao.keywordFileSearch(keyword, circleId, uploadedBy);
			return fileList;
	}


}