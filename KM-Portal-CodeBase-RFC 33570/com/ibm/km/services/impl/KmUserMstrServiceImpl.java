/*
 * Created on Feb 1, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package com.ibm.km.services.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.dao.KmUserMstrDao;
import com.ibm.km.dao.impl.KmUserMstrDaoImpl;
import com.ibm.km.dto.KmActorMstr;
import com.ibm.km.dto.KmUserDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmUserMstrService;

/**
 * @author Anil
 * @version 1.0
 * 
 */
public class KmUserMstrServiceImpl implements KmUserMstrService {

	public static Logger logger = Logger.getRootLogger();



	/**
	 * To check duplicate user service
	 * @param userLogingId
	 * @return boolean
	 * @throws KmException
	 */

	public boolean checkDuplicateUserLogin(String userLogingId) throws DAOException,KmException {
		KmUserMstrDao userMstrDao = null;
		boolean exist = false;
		try {
			userMstrDao = new KmUserMstrDaoImpl();
			if (userMstrDao.CheckUserId(userLogingId)) {
				exist = true;
			} else {
				exist = false;
			}
		}
		catch (DAOException e) {

			   logger.error(
				   "SQL Exception occured while CheckUserId."
					   + "Exception Message: "
					   + e.getMessage());
			   throw new DAOException("SQLException: " + e.getMessage(), e); 
		}
		catch (Exception e) {
			e.printStackTrace();
			
				logger.error("DAOException occured in checkDuplicateUserLogin():" + e.getMessage());
				throw new KmException(e.getMessage());
			
		}
		return exist;

	}


	/**
	 * 
	 * @param userMstrDto
	 * @param sessionUserBean
	 */

	public int createUserService(KmUserMstr userMstrDto) throws KmException {

			int insertCount = 0;
			KmUserMstrDao userMstrDao = new KmUserMstrDaoImpl();
			try{
				insertCount = userMstrDao.insert(userMstrDto);
			}
			catch (KmException e) {
				throw new KmException(e.getMessage());
			}
			return insertCount;
	}


	public int editUserService(KmUserMstr userMstrDto) throws KmException {
		int insertCount = 0;
		
			KmUserMstrDao userMstrDao = new KmUserMstrDaoImpl();

			/*Updating into the table KM_USER_MSTR*/
			insertCount = userMstrDao.update(userMstrDto);
			return insertCount;
	}




	public int changePasswordService(KmUserMstr dto) throws KmException {
		KmUserMstrDao userDataDao = new KmUserMstrDaoImpl();
		int updateCnt = 0;
		
			//Update password dao called
			updateCnt = userDataDao.updatePassword(dto);
			return updateCnt;
	}



	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#getUserService(java.lang.String)
	 * To get the details of using userId
	 **/
	public KmUserDto getUserService(String userId) throws KmException{
		KmUserMstrDao userDAO = new KmUserMstrDaoImpl();
		KmUserDto userDto=new KmUserDto();
		userDto=userDAO.selectUser(userId);
		return userDto;	
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#viewUsers(java.lang.String, java.lang.String)
	 */
	public ArrayList viewUsers(String loginActorId, String userId) throws KmException {
		KmUserMstrDao userDAO = new KmUserMstrDaoImpl();
		ArrayList userList=new ArrayList();
		userList=userDAO.viewUsers(loginActorId, userId);
/*		if(loginActorId.equals("1"))
			userList=userDAO.viewCircleApprovers();
		else if(loginActorId.equals("2"))
			userList=userDAO.viewCircleUsers(elementId);
		else 
			userList=userDAO.viewCsrs(elementId);	*/
		return userList;
	}
	
	//added by vishwas
	public ArrayList viewUsers(String loginActorId,String Kmactorid, String userId) throws KmException {
		KmUserMstrDao userDAO = new KmUserMstrDaoImpl();
		ArrayList userList=new ArrayList();
		userList=userDAO.viewUsers(loginActorId,Kmactorid,userId);
/*		if(loginActorId.equals("1"))
			userList=userDAO.viewCircleApprovers();
		else if(loginActorId.equals("2"))
			userList=userDAO.viewCircleUsers(elementId);
		else 
			userList=userDAO.viewCsrs(elementId);	*/
		return userList;
	}
	
	public ArrayList<KmActorMstr> getActorList() throws KmException {
		KmUserMstrDao userDAO = new KmUserMstrDaoImpl();
		ArrayList<KmActorMstr> userList=new ArrayList<KmActorMstr>();
		userList=userDAO.getActorList();
/*		if(loginActorId.equals("1"))
			userList=userDAO.viewCircleApprovers();
		else if(loginActorId.equals("2"))
			userList=userDAO.viewCircleUsers(elementId);
		else 
			userList=userDAO.viewCsrs(elementId);	*/
		return userList;
	}
	
	//end by vishwas

	//added by vishwas for UD intregation
	
	//end by vishwas for UD inregation

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#getElementUsers(java.lang.String)
	 */
	public String[] getElementUsers(String elementId) throws KmException {
		KmUserMstrDao dao=new KmUserMstrDaoImpl();
		return dao.getElementsUsers(elementId);
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#getFavCategory(java.lang.String)
	 */
	public String getFavCategory(String userId) throws KmException {
		KmUserMstrDao dao=new KmUserMstrDaoImpl();
		return dao.getFavCategory(userId);
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#noOfElementUsers(java.lang.String)
	 */
	public int noOfElementUsers(String elementId) throws KmException {
		KmUserMstrDao dao=new KmUserMstrDaoImpl();
		return dao.noOfElementUsers(elementId);
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#checkForFavourite(java.lang.String)
	 */
	public boolean checkForFavourite(String elementId) throws KmException {
		KmUserMstrDao dao=new KmUserMstrDaoImpl();
		return dao.checkForFavourite(elementId);
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#getManagerDetails(java.lang.String)
	 */
	public KmUserMstr getManagers(String circleId,int escalationCount) throws KmException {
		KmUserMstrDao dao=new KmUserMstrDaoImpl();
		return dao.getManagers(circleId,escalationCount);
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#getApprover(java.lang.String)
	 */
	public String getApprover(String circleId) throws KmException {
		KmUserMstrDao dao=new KmUserMstrDaoImpl();
		return dao.getApprover(circleId);
	}
	
	public int deleteUserService(String userLoginId)throws Exception{
		
		KmUserMstrDao dao=new KmUserMstrDaoImpl();
		return dao.deleteUser(userLoginId);
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#deleteUser(java.lang.String)
	 */
	public void deleteUser(String userLoginId) throws KmException {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#insertUserData(com.ibm.km.dto.KmUserMstr)
	 */
	public int insertUserData(KmUserMstr user) throws Exception, DAOException {
		KmUserMstrDao dao=new KmUserMstrDaoImpl();
		return dao.insertUserData(user);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#updateUserStatus(com.ibm.km.dto.KmUserMstr)
	 */
	public void updateUserStatus(KmUserMstr userMstrDto) throws KmException {
		KmUserMstrDao userMstrDao = new KmUserMstrDaoImpl();

			/*Updating into the table KM_USER_MSTR*/
			userMstrDao.updateUserStatus(userMstrDto);
		
	}
	public void updateSessionExpiry(KmUserMstr userMstrDto) throws KmException {
		KmUserMstrDao userMstrDao = new KmUserMstrDaoImpl();

			/*Updating into the table KM_USER_MSTR*/
			userMstrDao.updateSessionExpiry(userMstrDto);
		
	}


		/* (non-Javadoc)
	 * @see com.ibm.km.services.KmUserMstrService#getUserStatus()
	 */
	public String getUserStatus(String userLoginId) throws KmException {
		KmUserMstrDao dao=new KmUserMstrDaoImpl();
		return dao.getUserStatus(userLoginId);
	}
	/* KM Phase II partner list retreival service*/
	public ArrayList getPartnerName() throws KmException{
			ArrayList partnerList=new ArrayList();
			KmUserMstrDao dao=new KmUserMstrDaoImpl();
			partnerList=dao.getPartner();
			return partnerList;

		}
	public KmUserMstr checkUserLoginId(String user_login_id) throws Exception,DAOException {
		KmUserMstrDao userMstrDao = new KmUserMstrDaoImpl();
		return userMstrDao.CheckUserLoginId(user_login_id);
	
		
		
	}


	public KmUserMstr userIdFromMobile(String mobileNumber) throws KmException {
		KmUserMstrDao userMstrDao = new KmUserMstrDaoImpl();
		try{
			return userMstrDao.userIdFromMobile(mobileNumber);
		}
		catch (DAOException e) {
			throw new KmException(e.getMessage());
		}
	}
	public KmUserMstr userIdFromMobile(String mobileNumber,String actorIDs) throws KmException{
	KmUserMstrDao userMstrDao = new KmUserMstrDaoImpl();
	try{
		return userMstrDao.userIdFromMobile(mobileNumber,actorIDs);
	}
	catch (DAOException e) {
		throw new KmException(e.getMessage());
	}
  }
	public boolean validateLastThreePasswords(String userLoginId, String password) throws KmException {
		KmUserMstrDao userMstrDao = new KmUserMstrDaoImpl();
		try{
			return userMstrDao.validateLastThreePasswords(userLoginId,password);
		}
		catch (DAOException e) {
			throw new KmException(e.getMessage());
		}
	}

	
}
