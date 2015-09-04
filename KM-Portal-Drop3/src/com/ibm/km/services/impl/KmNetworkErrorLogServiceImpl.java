/*
 * Created on Nov 24, 2010
 *
 */
package com.ibm.km.services.impl;


import org.apache.log4j.Logger;

import com.ibm.km.dao.KmNetworkErrorLogDao;
import com.ibm.km.dao.impl.KmNetworkErrorLogDaoImpl;
import com.ibm.km.dto.NetworkErrorLogDto;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmNetworkErrorLogService;



/**
 * @author Kundan Kumar
 * @version 1.0
 * 
 */
public class KmNetworkErrorLogServiceImpl implements KmNetworkErrorLogService {

	/**
	* Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmNetworkErrorLogServiceImpl.class);
	}
	KmNetworkErrorLogDao dao = new KmNetworkErrorLogDaoImpl();


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmCategoryMstrService#createCategoryService(com.ibm.km.dto.KmCategoryMstr)
	 */
	public int insertNetworkErrorLogService(NetworkErrorLogDto networkErrorLogDto) throws KmException {
		int inserted=0;
		try{	
			//Calling insert method in DAO
			 inserted=dao.insert(networkErrorLogDto);
		}catch(Exception e){
			e.printStackTrace();
			if(e instanceof KmException)	
				logger.error("DaoException occured while creating category :"+e);		
			else
				logger.error("Exception occured while creating folder for category :"+e);
			throw new KmException(e.getMessage());
		}	
		return inserted;
	}
	public  String selectCSDUsers(int circleId) throws KmException 
	{
		String csdUsers = "";
		try{	
			//selecting csd users
			csdUsers=dao.selectCSDUsers(circleId);
		}catch(Exception e){
			e.printStackTrace();
			if(e instanceof KmException)	
				logger.error("DaoException occured while selecting csd Users :"+e);		
			else
				logger.error("Exception occured while selecting csd Users :"+e);
			throw new KmException(e.getMessage());
		}	
		return csdUsers;
		
	}
	
	//new change
	
	public  String getCircle(int circleId) throws KmException {
		
		String circlename = "";
		try{	
			//selecting csd users
			circlename=dao.getCircle(circleId);
		}catch(Exception e){
			e.printStackTrace();
			if(e instanceof KmException)	
				logger.error("DaoException occured while selecting circlename :"+e);		
			else
				logger.error("Exception occured while selecting circlename :"+e);
			throw new KmException(e.getMessage());
		}	
		return circlename;
		
		
		
	}
	
}

	