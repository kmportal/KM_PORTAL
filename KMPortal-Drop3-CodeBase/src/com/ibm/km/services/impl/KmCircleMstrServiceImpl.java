/*
 * Created on Oct 29, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.ibm.km.dao.KmCircleMstrDao;
import com.ibm.km.dao.impl.KmCircleMstrDaoImpl;
import com.ibm.km.dto.KmCircleMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.DAOException;
import com.ibm.km.services.KmCircleMstrService;
import org.apache.log4j.Logger;
import com.ibm.km.common.PropertyReader;


/**
 * @author Anil
 * @version 1.0
 * 
 */
public class KmCircleMstrServiceImpl implements KmCircleMstrService {

	/*
			 * Logger for the class.
			 */
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCircleMstrServiceImpl.class);
	}



	/**
	* 
	 * @return circleList
	 * @throws KmException
	 */
	public List getCircleDetails() throws KmException {
		KmCircleMstrDao circleData = new KmCircleMstrDaoImpl();
		List circleList = null;
		KmCircleMstr circleDto = null;
		try {
			KmCircleMstr arcCircleMstr[] = circleData.getCircleData();
			circleList = new ArrayList();
			
			for (int i = 0; i < arcCircleMstr.length; i++) {
				 
				 circleList.add(arcCircleMstr[i]);
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof DAOException) {
				logger.error("DAOException occured in getZoneDataService():" + e.getMessage());
				throw new KmException(e.getMessage());
			} else {
				logger.error("Exception occured in getZoneDataService():" + e.getMessage());
			}
		}
		return circleList;

	}

	public void createCircleService(KmCircleMstr circleMstrDto) throws KmException {
		try {
			KmCircleMstrDao circleMstrDao = new KmCircleMstrDaoImpl();

			//Inserting into the table ARC_CIRCLE_MSTR
			int insertCount = circleMstrDao.insert(circleMstrDto);
			String circleId=circleMstrDto.getCircleId();
			ResourceBundle bundle=ResourceBundle.getBundle("ApplicationResources");
			String newFolderPath =bundle.getString("folder.path");
			newFolderPath=newFolderPath+circleId;
			String newFolderName = circleId;
			String directory = newFolderPath;
			File f = new File(directory);
			f.mkdirs();
			logger.info("A folder is created for circle : "+directory);	
		
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof KmException) {
				logger.error("DAOException occured in createZoneService():" + e.getMessage());
				throw new KmException(e.getMessage());
			} else {
				logger.error("Exception occured while creating the folder for circle():" + e.getMessage());
			}
		}
	}



	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmCircleMstrService#checkCircleOnCircleName(java.lang.String)
	 */
	public boolean checkCircleOnCircleName(String string) throws KmException{
		boolean isCircleExists = false;
		try {
			KmCircleMstrDao circleMstrDao = new KmCircleMstrDaoImpl();

			//Check the Existing Circle from table KM_CIRCLE_MSTR
			isCircleExists = circleMstrDao.checkCircleName(string);

		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof DAOException) {
				logger.error("DAOException occured in checkCircleOnCircleName():" + e.getMessage());
				throw new KmException(e.getMessage());
			} else {
				logger.error("Exception occured in checkCircleOnCircleName():" + e.getMessage());
			}
		}
		// TODO Auto-generated method stub
		return isCircleExists;
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmCircleMstrService#retrieveCircles()
	 */
	public ArrayList retrieveCircles() throws KmException {
		KmCircleMstrDao dao=new KmCircleMstrDaoImpl();
		ArrayList circleList=new ArrayList();
		circleList=dao.getCircles();
		return circleList;	
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmCircleMstrService#retrieveCircles()
	 */
	public ArrayList retrieveAllCircles() throws KmException {
		KmCircleMstrDao dao=new KmCircleMstrDaoImpl();
		ArrayList circleList=new ArrayList();
		circleList=dao.getAllCircles();
		return circleList;	
	}


}
