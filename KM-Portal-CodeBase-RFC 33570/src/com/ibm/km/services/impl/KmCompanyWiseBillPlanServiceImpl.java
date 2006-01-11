
package com.ibm.km.services.impl;

import java.util.ArrayList;
import org.apache.log4j.Logger;

import com.ibm.km.dao.KmCompanyWiseBillPlanDao;
import com.ibm.km.dao.impl.KmCompanyWiseBillPlanDaoImpl;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmCompanyWiseBillPlanService;

/**
* @author Anveeksha & Neeraj 
* created for Waiver Matrix Upload
* for viewing Company Wise bill Plan details
*/


public class KmCompanyWiseBillPlanServiceImpl implements KmCompanyWiseBillPlanService {

	/**
	* Logger for the class.
	**/	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCompanyWiseBillPlanServiceImpl.class);
	}

	/**
	 * @param str
	 * @return ArrayList for companies as per user entered search value
	 * @throws KmException
	 */
	public ArrayList getCompanyList(String str) throws KmException {
		KmCompanyWiseBillPlanDao daoObj = new KmCompanyWiseBillPlanDaoImpl();
		
		try{

			ArrayList compList=new ArrayList();
			compList=daoObj.getCompanyList(str);
			return compList;
		}
		catch(KmException e) 
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		} 
		catch(DAOException e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		}
		finally 
		{
			logger.info("Exit from get Company List method in Service");
			
		}
	}

	/**
	 * @param id
	 * @return ArrayList for bill plans of selected company   
	 * @throws KmException
	 */
	public ArrayList getBillPlanList(int id) throws KmException {
		// TODO Auto-generated method stub
		KmCompanyWiseBillPlanDao daoObj = new KmCompanyWiseBillPlanDaoImpl();
		
		try{
			ArrayList billPlanList=new ArrayList();
			billPlanList=daoObj.getBillPlanList(id);
			return billPlanList;
		}
		catch(KmException e) 
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		} 
		catch(DAOException e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		}
		finally 
		{
			logger.info("Exit from Get Bill Plan List method in Service");
			
		}
	}
	
	/**
	 * @param id
	 * @return ArrayList for details of Company selected 
	 * @throws KmException
	 */
	public ArrayList getCompanyDetails(int id) throws KmException {
		// TODO Auto-generated method stub
		
		KmCompanyWiseBillPlanDao daoObj = new KmCompanyWiseBillPlanDaoImpl();
		
		try{
			ArrayList companyDetails = new ArrayList();
			companyDetails=daoObj.getCompanyDetails(id);
			return companyDetails;
		}
		catch(KmException e) 
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		} 
		catch(DAOException e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		}
		finally 
		{
			logger.info("Exit from Get Company Details method in Service");
			
		}
	}
	
	/**
	 * @param planName
	 * @return ArralList for Rate Details of selected Bill Plan
	 * @throws KmException
	 */
	public ArrayList getBillPlanRateDetails(String planName) throws KmException {
		// TODO Auto-generated method stub
		KmCompanyWiseBillPlanDao daoObj = new KmCompanyWiseBillPlanDaoImpl();
		ArrayList billPlanRateDetails=new ArrayList();
		
		try
		{
			billPlanRateDetails=daoObj.getBillPlanRateDetail(planName);
			return billPlanRateDetails;
		}
		catch(KmException e) 
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		} 
		catch(DAOException e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		}
		finally 
		{
			logger.info("Exit from Get Bill Plan Rate method in Service");
		}
	}
}
