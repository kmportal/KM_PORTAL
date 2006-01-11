/*
 * Created on Jan 30, 2008
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ibm.km.dao.KmSubCategoryMstrDao;
import com.ibm.km.dao.impl.KmSubCategoryMstrDaoImpl;
import com.ibm.km.dto.KmSubCategoryMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.KmSubCategoryMstrDaoException;
import com.ibm.km.exception.DAOException;
import com.ibm.km.forms.KmSubCategoryMstrFormBean;
import com.ibm.km.services.KmSubCategoryMstrService;
import org.apache.log4j.Logger;
import java.util.ResourceBundle;

/**
 * @author Anil
 * @version 1.0
 * 
 */
public class KmSubCategoryMstrServiceImpl implements KmSubCategoryMstrService {

	/*
			 * Logger for the class.
			 */
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmSubCategoryMstrServiceImpl.class);
	}
	ArrayList circleList = new ArrayList();
	ArrayList categoryList = new ArrayList();
	KmSubCategoryMstrDao dao = new KmSubCategoryMstrDaoImpl();



	
	

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmCategoryMstrService#createCategoryService(com.ibm.km.dto.KmCategoryMstr)
	 */
	public void createSubCategoryService(KmSubCategoryMstr subCategoryMstrDto) throws KmException {
			
		try{	
			//Calling DAO for insertion
			
			int subCategoryId=dao.insert(subCategoryMstrDto);
			
			//Creating folder for Sub Category
			
			int categoryId=subCategoryMstrDto.getCategoryId();
			logger.info(categoryId+"");
			int circleId=subCategoryMstrDto.getCircleId();
			ResourceBundle bundle=ResourceBundle.getBundle("ApplicationResources");
			logger.info(bundle.getString("folder.path"));
			String newFolderPath =bundle.getString("folder.path");
			newFolderPath=newFolderPath+circleId+"/"+categoryId+"/"+subCategoryId;
			String directory = newFolderPath;
			File f = new File(directory);
			f.mkdirs();  
			logger.info("Folder for sub-category is created :"+directory);
			
		}catch(Exception e){
			if(e instanceof KmSubCategoryMstrDaoException)
				logger.error("DAOException ocuured while insertind caetgory details");
			else
				logger.error("Exception occured while creating folder for sub-category");	
			throw new KmException(e.getMessage());	
		}
	}









	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmSubCategoryMstrService#checkOnSubCategoryNameService(java.lang.String, java.lang.String)
	 */
	public boolean checkOnSubCategoryNameService(String subCategoryName, String categoryId) throws KmException{
		KmSubCategoryMstrDao dao=new KmSubCategoryMstrDaoImpl();
		boolean isDuplicateCategory;
		isDuplicateCategory=dao.checkOnSubCategoryName(subCategoryName, categoryId);
		return isDuplicateCategory;
	}






	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmSubCategoryMstrService#retrieveSubCategoryService(java.lang.String)
	 */
	public ArrayList retrieveSubCategoryService(String categoryId) throws KmException {
		ArrayList categoryList=new ArrayList();
		KmSubCategoryMstrDao dao=new KmSubCategoryMstrDaoImpl();
		categoryList=dao.getSubCategories(categoryId);
		return categoryList;
	}

}

