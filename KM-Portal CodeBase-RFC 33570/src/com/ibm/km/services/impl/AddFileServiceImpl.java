/*
 * Created on Feb 11, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import com.ibm.km.dao.AddFileDAO;
import com.ibm.km.dao.impl.AddFileDaoImpl;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.AddFileService;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AddFileServiceImpl implements AddFileService{
	
	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(AddFileServiceImpl.class);
	}

	public KmDocumentMstr checkFile(FormFile file, String parentId)throws KmException{
		AddFileDAO dao = new AddFileDaoImpl();
		return dao.checkFile(parentId, file.getFileName());
	}
	
	

	
	/*
	 *  (non-Javadoc)
	 * @see com.ibm.km.services.AddFileService#saveFile(java.lang.String, org.apache.struts.upload.FormFile)
	 */
	public void saveFile(String path,FormFile file, String parentId)throws KmException
	{
		AddFileDAO dao = new AddFileDaoImpl();
		try {
				logger.info("FILE PATH NAME IS"+path);
				File f = new File(path);
				byte[] fileData = file.getFileData();
				logger.info("datalength"+fileData.length);
				RandomAccessFile raf = new RandomAccessFile(f, "rw");
				logger.info("datalengthPPPPPPP"+fileData.length);
				raf.write(fileData);
				logger.info("datalengthssssss"+fileData.length);
				raf.close();
		}
	
		catch(IOException io)
		{
			io.printStackTrace();
			throw new KmException("IOException Occours",io);
			
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ibm.km.services.AddFileService#updateFile(java.lang.String, java.lang.String, org.apache.struts.upload.FormFile)
	 */
	public void updateFile(String oldPath, String newPath,FormFile fileContent)throws KmException
	{

		File f = new File(oldPath);
		f.delete();
		saveFile(newPath,fileContent,"1");
	}		
			
	
	/* (non-Javadoc)
	 * @see com.ibm.km.services.AddFileService#saveFileInfoInDB(com.ibm.km.dto.AddFileDTO)
	 */
	public String saveFileInfoInDB(KmDocumentMstr dto) throws KmException {
		
		AddFileDAO dao = new AddFileDaoImpl();
		return dao.saveFileInfoInDB(dto);
	
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.AddFileService#updateFileInfoInDB(java.lang.String)
	 */
	public void updateDocumentName(KmDocumentMstr dto) throws KmException {
		
		AddFileDAO dao = new AddFileDaoImpl();
		dao.updateDocumentName(dto);

	}

}
