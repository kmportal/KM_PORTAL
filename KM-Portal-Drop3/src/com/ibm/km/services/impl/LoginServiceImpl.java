package com.ibm.km.services.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;   

//import com.ibm.arc.dao.ArcCategoryMstrDao;
import com.ibm.km.dao.KmLoginDao;
import com.ibm.km.dao.KmPopulateUserDao;
//import com.ibm.arc.dao.impl.ArcCategoryMstrDaoImpl;
import com.ibm.km.dao.impl.KmLoginDaoImpl;
import com.ibm.km.dao.impl.KmPopulateUserDaoImpl;
//import com.ibm.arc.dto.ArcCategoryMstr;
import com.ibm.km.dto.KmCircleMstr;
import com.ibm.km.dto.KmLogin;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.DAOException;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.LoginService;
import com.ibm.appsecure.util.ReadResource;
/*
 * Created on Oct 16, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author namangup
 * @version 2.0
 * 
 */
public class LoginServiceImpl implements LoginService {
	private static final Logger LOGGER;

	static {

		LOGGER = Logger.getLogger(LoginServiceImpl.class);
	}
	
	/**
	 * @param loginId
	 */
	public boolean isValidUser(String loginId) throws KmException {
	
		KmLoginDao kmLoginDaoImpl=new KmLoginDaoImpl();
		
		try
		{
			return kmLoginDaoImpl.isValidUser(loginId);
		}catch(KmException ee)
		{
			throw ee;
		}
	}
	
	/**
	 * @param OlmId
	 */
	public boolean isValidOlmId(String OlmId) throws KmException {
	
		KmLoginDao kmLoginDaoImpl=new KmLoginDaoImpl();
		
		try
		{
			return kmLoginDaoImpl.isValidOlmId(OlmId);
		}catch(KmException ee)
		{
			throw ee;
		}
	}
	

	/**
	 * 
	 * @param dto
	 */

	public KmUserMstr populateUserDetails(KmLogin login) throws KmException, DAOException {
		LOGGER.info("Populating user credentials normal user" + login);
		KmPopulateUserDao populateDao = new KmPopulateUserDaoImpl();
		KmUserMstr userBean = new KmUserMstr();
		KmCircleMstr circleBean = new KmCircleMstr();
		List dataList = null;
		try {
			dataList = new ArrayList();
			dataList = populateDao.populateValues(login);
			userBean = setUserBeanData(dataList, userBean);
			//circleBean=setCircleBeanData(dataList, circleBean);
			////System.out.println("Circle Flag: "+circleBean.getSingleSignInFlag()+" User Login Status: "+userBean.getUserLoginStatus());
		/*	if(circleBean.getSingleSignInFlag().equals("Y")&&userBean.getUserLoginStatus().equals("Y")){
				throw new KmException("User already logged in.");
			}else{
				userBean.setUserLoginStatus("Y");
			}*/
			
		} catch (Exception ex) {
			if (ex instanceof DAOException) {
				LOGGER.error(" DAOException occured in populateUserDetails():" + ex.getMessage());
				throw new DAOException(ex.getMessage());
			} else {
				LOGGER.error(" Exception occured in populateUserDetails():" + ex.getMessage());
				throw new KmException(ex.getMessage());
			}

		}
		LOGGER.info("Populating user credentials successfully");
		return userBean;
	}
	//added by vishwas for Ud intregation
	public KmUserMstr populateUserDetailsforUD(String loginID) throws KmException, DAOException {
		LOGGER.info("Populating user credentials for UD method" + loginID);
		KmPopulateUserDao populateDao = new KmPopulateUserDaoImpl();
		KmUserMstr userBean = new KmUserMstr();
		KmCircleMstr circleBean = new KmCircleMstr();
		List dataList = null;
		try {
			dataList = new ArrayList();
			dataList = populateDao.populateValuesforUD(loginID);
			userBean = setUserBeanData1(dataList, userBean);
			//circleBean=setCircleBeanData(dataList, circleBean);
			////System.out.println("Circle Flag: "+circleBean.getSingleSignInFlag()+" User Login Status: "+userBean.getUserLoginStatus());
		/*	if(circleBean.getSingleSignInFlag().equals("Y")&&userBean.getUserLoginStatus().equals("Y")){
				throw new KmException("User already logged in.");
			}else{
				userBean.setUserLoginStatus("Y");
			}*/
			
		} catch (Exception ex) {
			if (ex instanceof DAOException) {
				LOGGER.error(" DAOException occured in populateUserDetails():" + ex.getMessage());
				throw new DAOException(ex.getMessage());
			} else {
				LOGGER.error(" Exception occured in populateUserDetails():" + ex.getMessage());
				throw new KmException(ex.getMessage());
			}

		}
		LOGGER.info("Populating user credentials successfully");
		return userBean;

	}
	
	private KmUserMstr setUserBeanData1(List dataList, KmUserMstr userBean) throws KmException {
		LOGGER.debug("Setting user bean3333333");
		HashMap categoryMap = null;
		ArrayList categoryDesc = null;
		KmLoginDao dao=new KmLoginDaoImpl();
		try {
			int count = 0;
			ArrayList moduleList = (ArrayList) dataList.get(count);
			userBean.setModuleList(moduleList);
			moduleList = null;
			 
			List userDataList = (ArrayList) dataList.get(++count);
			HashMap userData = (HashMap) userDataList.get(0);
			userBean.setUserId(userData.get("USER_ID").toString());
			userBean.setCircleId(userData.get("CIRCLE_ID").toString());
			userBean.setKmActorId(userData.get("KM_ACTOR_ID").toString());
			userBean.setCreatedBy(userData.get("CREATED_BY").toString());
			userBean.setCreatedDt((Timestamp)userData.get("CREATED_DT"));
			userBean.setUserLoginId(userData.get("USER_LOGIN_ID").toString());
		
			//Commented By Karan to get the correct Last Login Time on 29 Jan 2013
			
		/*	if (null == userData.get("LAST_LOGIN_TIME")) {
				userBean.setLastLoginTime((Timestamp)null);
			} else {
				userBean.setLastLoginTime((Timestamp)userData.get("LAST_LOGIN_TIME"));
			}*/
			
			// Timestamp lastLoginTime = dao.getLastLogin(userBean.getUserLoginId());
			//TODO fix to get Last login time; commented the above line as its taking very long time
			Timestamp lastLoginTime = new Timestamp((new java.util.Date()).getTime());
			
			SimpleDateFormat sdf;
			sdf = new SimpleDateFormat("EEE, d MMM yyyy hh:mm");
			String lastLogTime = sdf.format(lastLoginTime); 
			    
			if (null == lastLoginTime) {
				userBean.setLastLoginTime("");
			} else {
				userBean.setLastLoginTime(lastLogTime);
			}

			userBean.setLoginAttempted(userData.get("LOGIN_ATTEMPTED").toString());
			userBean.setStatus(userData.get("STATUS").toString());
			if (null == userData.get("UPDATED_DT")) {
				userBean.setUpdatedDt((Timestamp)null);
			} else {
				userBean.setUpdatedDt((Timestamp)userData.get("UPDATED_DT"));
			}
			if (null == userData.get("UPDATED_BY")) {
				userBean.setUpdatedBy("");
			} else {
				userBean.setUpdatedBy(userData.get("UPDATED_BY").toString());
			}
			if (null == userData.get("USER_EMAILID")) {
				userBean.setUserEmailid("");
			} else {
				userBean.setUserEmailid(userData.get("USER_EMAILID").toString());
			}			
			System.out.println("system user name============="+userData.get("USER_LOGIN_ID").toString());
			userBean.setUserFname(userData.get("USER_LOGIN_ID").toString());
			if (null == userData.get("USER_MNAME")) {
				userBean.setUserMname("");
			} else {
				userBean.setUserMname(userData.get("USER_MNAME").toString());
			}
			if (null == userData.get("USER_LOGIN_STATUS")) {
				userBean.setUserLoginStatus("");
			} else {
				userBean.setUserLoginStatus(userData.get("USER_LOGIN_STATUS").toString());
			}
			userBean.setUserLname(userData.get("USER_LNAME").toString());
			
			if (null == userData.get("USER_MOBILE_NUMBER")) {
				userBean.setUserMobileNumber("");
			} else {
				userBean.setUserMobileNumber(userData.get("USER_MOBILE_NUMBER").toString());
			}				
			userBean.setUserPassword(userData.get("USER_PASSWORD").toString());
			userBean.setUserPsswrdExpryDt((Timestamp)userData.get("USER_PSSWRD_EXPRY_DT"));
			if (null == userData.get("FAV_CATEGORY_ID")) {
				userBean.setFavCategoryId("");
			} else {
				userBean.setFavCategoryId(userData.get("FAV_CATEGORY_ID").toString());
			}			
			if (null == userData.get("ELEMENT_ID")) {
				userBean.setElementId("");
			} else {
				userBean.setElementId(userData.get("ELEMENT_ID").toString());
			}	

			List actorList = (ArrayList) dataList.get(++count);
			HashMap actorData = (HashMap) actorList.get(0);
			userBean.setKmActorName(actorData.get("KM_ACTOR_NAME").toString());
			userBean.setKmActorId(actorData.get("KM_ACTOR_ID").toString());

			List circleList = (ArrayList) dataList.get(++count);
			HashMap circleData = (HashMap) circleList.get(0);
			userBean.setElementName(circleData.get("ELEMENT_NAME").toString());
		
			


		} catch (Exception ex) {
			ex.printStackTrace();
			if (ex instanceof DAOException) {
				throw new KmException(ex.getMessage());
			} else {
				LOGGER.error(" Exception occured in setUserBeanData():" + ex.getMessage());
			}

		}
		LOGGER.info(" setUserBeanData:user information successfully set  ");
		return userBean;
	}
	

	//End by vishwas

	private KmUserMstr setUserBeanData(List dataList, KmUserMstr userBean) throws KmException {
		LOGGER.debug("Setting user bean3333333");
		HashMap categoryMap = null;
		ArrayList categoryDesc = null;
		KmLoginDao dao=new KmLoginDaoImpl();
		try {
			int count = 0;
			ArrayList moduleList = (ArrayList) dataList.get(count);
			userBean.setModuleList(moduleList);
			moduleList = null;
			 
			List userDataList = (ArrayList) dataList.get(++count);
			HashMap userData = (HashMap) userDataList.get(0);
			userBean.setUserId(userData.get("USER_ID").toString());
			userBean.setCircleId(userData.get("CIRCLE_ID").toString());
			userBean.setKmActorId(userData.get("KM_ACTOR_ID").toString());
			userBean.setCreatedBy(userData.get("CREATED_BY").toString());
			userBean.setCreatedDt((Timestamp)userData.get("CREATED_DT"));
			userBean.setUserLoginId(userData.get("USER_LOGIN_ID").toString());
		
			//Commented By Karan to get the correct Last Login Time on 29 Jan 2013
			
		/*	if (null == userData.get("LAST_LOGIN_TIME")) {
				userBean.setLastLoginTime((Timestamp)null);
			} else {
				userBean.setLastLoginTime((Timestamp)userData.get("LAST_LOGIN_TIME"));
			}*/
			
			// Timestamp lastLoginTime = dao.getLastLogin(userBean.getUserLoginId());
			//TODO fix to get Last login time; commented the above line as its taking very long time
			Timestamp lastLoginTime = new Timestamp((new java.util.Date()).getTime());
			
			SimpleDateFormat sdf;
			sdf = new SimpleDateFormat("EEE, d MMM yyyy hh:mm");
			String lastLogTime = sdf.format(lastLoginTime); 
			    
			if (null == lastLoginTime) {
				userBean.setLastLoginTime("");
			} else {
				userBean.setLastLoginTime(lastLogTime);
			}

			userBean.setLoginAttempted(userData.get("LOGIN_ATTEMPTED").toString());
			userBean.setStatus(userData.get("STATUS").toString());
			if (null == userData.get("UPDATED_DT")) {
				userBean.setUpdatedDt((Timestamp)null);
			} else {
				userBean.setUpdatedDt((Timestamp)userData.get("UPDATED_DT"));
			}
			if (null == userData.get("UPDATED_BY")) {
				userBean.setUpdatedBy("");
			} else {
				userBean.setUpdatedBy(userData.get("UPDATED_BY").toString());
			}
			if (null == userData.get("USER_EMAILID")) {
				userBean.setUserEmailid("");
			} else {
				userBean.setUserEmailid(userData.get("USER_EMAILID").toString());
			}			
			
			userBean.setUserFname(userData.get("USER_FNAME").toString());
			if (null == userData.get("USER_MNAME")) {
				userBean.setUserMname("");
			} else {
				userBean.setUserMname(userData.get("USER_MNAME").toString());
			}
			if (null == userData.get("USER_LOGIN_STATUS")) {
				userBean.setUserLoginStatus("");
			} else {
				userBean.setUserLoginStatus(userData.get("USER_LOGIN_STATUS").toString());
			}
			userBean.setUserLname(userData.get("USER_LNAME").toString());
			
			if (null == userData.get("USER_MOBILE_NUMBER")) {
				userBean.setUserMobileNumber("");
			} else {
				userBean.setUserMobileNumber(userData.get("USER_MOBILE_NUMBER").toString());
			}				
			userBean.setUserPassword(userData.get("USER_PASSWORD").toString());
			userBean.setUserPsswrdExpryDt((Timestamp)userData.get("USER_PSSWRD_EXPRY_DT"));
			if (null == userData.get("FAV_CATEGORY_ID")) {
				userBean.setFavCategoryId("");
			} else {
				userBean.setFavCategoryId(userData.get("FAV_CATEGORY_ID").toString());
			}			
			if (null == userData.get("ELEMENT_ID")) {
				userBean.setElementId("");
			} else {
				userBean.setElementId(userData.get("ELEMENT_ID").toString());
			}	

			List actorList = (ArrayList) dataList.get(++count);
			HashMap actorData = (HashMap) actorList.get(0);
			userBean.setKmActorName(actorData.get("KM_ACTOR_NAME").toString());
			userBean.setKmActorId(actorData.get("KM_ACTOR_ID").toString());

			List circleList = (ArrayList) dataList.get(++count);
			HashMap circleData = (HashMap) circleList.get(0);
			userBean.setElementName(circleData.get("ELEMENT_NAME").toString());
		
			


		} catch (Exception ex) {
			ex.printStackTrace();
			if (ex instanceof DAOException) {
				throw new KmException(ex.getMessage());
			} else {
				LOGGER.error(" Exception occured in setUserBeanData():" + ex.getMessage());
			}

		}
		LOGGER.info(" setUserBeanData:user information successfully set  ");
		return userBean;
	}
	
	private KmCircleMstr setCircleBeanData(List dataList, KmCircleMstr circleBean) throws KmException {
		LOGGER.debug("Setting circle bean");
		HashMap categoryMap = null;
		ArrayList categoryDesc = null;
		try {
			int count = 0;
			ArrayList moduleList = (ArrayList) dataList.get(count);
			List userDataList = (ArrayList) dataList.get(++count);
			List actorList = (ArrayList) dataList.get(++count);
			List circleList = (ArrayList) dataList.get(++count);
			HashMap circleData = (HashMap) circleList.get(0);
			circleBean.setCircleName(circleData.get("CIRCLE_NAME").toString());
			circleBean.setCircleId(circleData.get("CIRCLE_ID").toString());
			circleBean.setSingleSignInFlag(circleData.get("SINGLE_SIGN_IN").toString());

		} catch (Exception ex) {
			ex.printStackTrace();
			if (ex instanceof DAOException) {
				throw new KmException(ex.getMessage());
			} else {
				LOGGER.error(" Exception occured in setCircleBeanData():" + ex.getMessage());
			}

		}
		LOGGER.info(" setUserBeanData:user information successfully set  ");
		return circleBean;
	}	
	public String getWarning(Object ob) {
				int expiredDays = Integer.parseInt(ReadResource.getValue("GSD",
						"PwdExpiryLimit"));
				String msg = "";
				Timestamp timestamp = null;
				if (ob instanceof Timestamp) {
					timestamp = (Timestamp) ob;
				} else {
					timestamp = new Timestamp(((Date) ob).getTime());
				}

				Calendar c = GregorianCalendar.getInstance();
				c.set(c.get(GregorianCalendar.YEAR), c.get(GregorianCalendar.MONTH), c
						.get(GregorianCalendar.DATE)
						- expiredDays + 5); // +5 = It will start warning 5 days before expiration
				Date d = c.getTime();
				Timestamp timestamp2 = new Timestamp(d.getTime());
				if (timestamp2.getTime() > timestamp.getTime()) {
					c.setTime(new Date(timestamp.getTime()));
					c.set(c.get(GregorianCalendar.YEAR),
							c.get(GregorianCalendar.MONTH), c
									.get(GregorianCalendar.DATE)
									+ expiredDays);
					msg = "Change your password.Your KM password is valid till :"
							+ getStringFromDate(c.getTime());
				}
				return msg;
			}
	
	public String getStringFromDate(java.util.Date d) {
			String dateTime = null;
			try {

				if (d == null) {
					return "";
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
						"dd MMM yyyy");
				dateTime = dateTimeFormatter.format(cal.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return dateTime;
		}		
	/* (non-Javadoc)
		 * @see com.ibm.km.services.LoginService#getConfigValue(java.lang.String)
		 */
		public String getConfigValue(String string) throws KmException {
			KmLoginDao dao=new KmLoginDaoImpl();
			return 	dao.getConfigValue(string);
		
		}

		public ArrayList getEmailId(String userName) throws KmException {
			KmLoginDao dao=new KmLoginDaoImpl();
			return 	dao.getEmailId(userName);
		}

		public void updatePassword(String userName, String encPassword) throws KmException {
			KmLoginDao kmLoginDaoImpl=new KmLoginDaoImpl();
			kmLoginDaoImpl.updatePassword(userName,encPassword);
			
		}

		public int getExpiredDocumentCount(KmUserMstr userBean) throws KmException {
			KmLoginDao kmLoginDaoImpl=new KmLoginDaoImpl();
			
			return kmLoginDaoImpl.getExpiredDocumentCount(userBean);
		}	
		
		 public List getFavorites(int userId) throws KmException
		 {
			 LOGGER.info(" Inside getFavorites to get favorites of a user  ");
				KmLoginDao kmLoginDaoImpl=new KmLoginDaoImpl();
				
				return kmLoginDaoImpl.getFavorites(userId);
				 
		 }
		 public int getUserIdForUD(String userId) throws KmException {
				KmLoginDao kmLoginDaoImpl=new KmLoginDaoImpl();
				
				return kmLoginDaoImpl.getUserIdForUD(userId);
			}	
}
