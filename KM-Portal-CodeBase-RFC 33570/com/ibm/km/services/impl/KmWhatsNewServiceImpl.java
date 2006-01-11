/*
 * Created on Apr 29, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import com.ibm.km.dao.KmWhatsNewDao;
import com.ibm.km.dao.impl.KmWhatsNewDaoImpl;
import com.ibm.km.dto.DocumentPath;
import com.ibm.km.dto.KmWhatsNew;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmWhatsNewService;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmWhatsNewServiceImpl implements KmWhatsNewService{
	
	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmWhatsNewServiceImpl.class);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmWhatsNewService#saveFile(com.ibm.km.dto.DocumentPath, org.apache.struts.upload.FormFile)
	 */
	public String saveFile(DocumentPath path, FormFile file) throws KmException {
		KmWhatsNewDao dao = new KmWhatsNewDaoImpl();
		
		try {

			String docName = dao.checkFile(path, file.getFileName());
			logger.info("Path: " +path.getNewStringPath());
			if (("").equals(docName)) {
				File f = new File(path.getNewStringPath());
				byte[] fileData = file.getFileData();
				RandomAccessFile raf = new RandomAccessFile(f, "rw");
				raf.write(fileData);
				raf.close();

				
				return docName;
			} else {
				return docName;
			}
		}
		catch(IOException io)
		{
			io.printStackTrace();
			throw new KmException("IOException Occours",io);
			
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmWhatsNewService#saveFileInfoInDB(com.ibm.km.dto.KmWhatsNew)
	 */
	public void saveFileInfoInDB(KmWhatsNew dto) throws KmException {
			
		KmWhatsNewDao dao = new KmWhatsNewDaoImpl();
		dao.saveFileInfoInDB(dto);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmWhatsNewService#updateFile(java.lang.String, java.lang.String, org.apache.struts.upload.FormFile)
	 */
	public void updateFile(String oldPath, String newPath, FormFile file) throws KmException {
		
		
		
			File f1=new File(oldPath);
			File f2=new File(newPath);
			f1.renameTo(f2);
		/*	File f = new File(oldPath);
			logger.info("File Name before delete " + f.getName());
			f.getAbsoluteFile().delete();
		
			f =new File(newPath);
			byte[] fileData = file.getFileData();
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			raf.write(fileData);
			raf.close(); */
	
		
		
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmWhatsNewService#updateFileInfoInDB(java.lang.String, java.lang.String, int)
	 */
	public void updateFileInfoInDB(String oldFile, String fileName, int userId, String docDesc) throws KmException{
				
		KmWhatsNewDao dao = new KmWhatsNewDaoImpl();
		dao.updateFileInfoInDB(oldFile,fileName,userId,docDesc);
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmWhatsNewService#viewFiles(java.lang.String)
	 */
	public ArrayList viewFiles(String[] circleIds,String actorId) throws KmException {
		KmWhatsNewDao dao = new KmWhatsNewDaoImpl();
		return dao.viewFiles(circleIds,actorId);
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmWhatsNewService#getCategories(java.lang.String)
	 */
	public String[] getCategories(String elementId) throws KmException {
		KmWhatsNewDao dao=new KmWhatsNewDaoImpl();
		return dao.getCategories(elementId);
	}

	

}
