/*
 * Created on Jan 26, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */




import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.ibm.km.dao.KmCategoryMstrDao;
import com.ibm.km.dao.impl.KmCategoryMstrDaoImpl;
import com.ibm.km.dto.KmCategoryMstr;
import com.ibm.km.exception.KmCategoryMstrDaoException;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmCategoryMstrFormBean;
import com.ibm.km.services.KmCategoryMstrService;
import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import com.ibm.km.common.PropertyReader;



/**
 * @author Anil
 * @version 1.0
 * 
 */
public class KmCategoryMstrServiceImpl implements KmCategoryMstrService {

	/**
	* Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCategoryMstrServiceImpl.class);
	}
	ArrayList circleList = new ArrayList();
	KmCategoryMstrDao dao = new KmCategoryMstrDaoImpl();


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmCategoryMstrService#createCategoryService(com.ibm.km.dto.KmCategoryMstr)
	 */
	public void createCategoryService(KmCategoryMstr categoryMstrDto) throws KmException {
		try{	
			
			
			ResourceBundle bundle=ResourceBundle.getBundle("ApplicationResources");
			String newFolderPath =bundle.getString("folder.path");
			
			//Calling insert method in DAO
			int categoryId=dao.insert(categoryMstrDto);
			int circleId=categoryMstrDto.getCircleId();
			
			//Creating Folder for Category
			
			newFolderPath=newFolderPath+circleId+"/"+categoryId;
			int newFolderName = categoryId;
			String directory = newFolderPath;
			File f = new File(directory);
			f.mkdirs();  
			
			//Saving image for the Category
			String fileName = categoryMstrDto.getFile().getFileName();
			if(!fileName.equals("")){
			
			String extn = fileName.substring(fileName.indexOf(".")+1);
			fileName = fileName.substring(0,fileName.indexOf("."));
			String filePath=bundle.getString("image.path")+fileName+"_"+circleId+"_"+categoryMstrDto.getCategoryName()+".gif";
		//	String filePath=categoryMstrDto.getPath()+fileName+"_"+circleId+"_"+categoryMstrDto.getCategoryName()+".gif";
			logger.info("FILE PATH :  "+filePath);
			File imgFile = new File(filePath);
			byte[] fileData = categoryMstrDto.getFile().getFileData();
			RandomAccessFile raf = new RandomAccessFile(imgFile, "rw");
			raf.write(fileData);
			raf.close();
	//		fileName = fileName + "_" + date + "." + extn;  
			}
			logger.info("A folder for category is created :"+directory);
		}catch(Exception e){
			e.printStackTrace();
			if(e instanceof KmException)	
				logger.error("DaoException occured while creating category :"+e);		
			else
				logger.error("Exception occured while creating folder for category :"+e);
			throw new KmException(e.getMessage());
				
		}		
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmCategoryMstrService#checkOnCategoryNameService(java.lang.String)
	 */
	public boolean checkOnCategoryNameService(String categoryName, String circleId) throws KmException {
		KmCategoryMstrDao dao=new KmCategoryMstrDaoImpl();
		boolean isDuplicateCategory;
		isDuplicateCategory=dao.checkOnCategoryName(categoryName, circleId);
		return isDuplicateCategory;
		
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmSearchFileService#getCategoryService(java.lang.String)
	 */
	public ArrayList retrieveCategoryService(String[] circleId)throws KmException {
		ArrayList categoryList=new ArrayList();
		KmCategoryMstrDao dao=new KmCategoryMstrDaoImpl();
		categoryList=dao.getCategories(circleId);
		return categoryList;
	}
	
	public HashMap retrieveCategoryMap(String circleId, String favCategoryId)throws KmException{
		HashMap categoryMap=null;
		KmCategoryMstrDao dao=new KmCategoryMstrDaoImpl();
		categoryMap=dao.getCategoryMapElements(circleId,favCategoryId);
		
		return categoryMap;
		
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmCategoryMstrService#getNoOfCategories(java.lang.String)
	 */
	public int getNoOfCategories(String circleId) throws KmException {
		KmCategoryMstrDao dao=new KmCategoryMstrDaoImpl();
		return dao.getNoOfCategories(circleId);
	}


}

	