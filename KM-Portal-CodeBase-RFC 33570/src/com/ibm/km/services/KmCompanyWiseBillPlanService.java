
package com.ibm.km.services;

import java.util.ArrayList;
import java.sql.ResultSet;

import com.ibm.km.exception.KmException;
import com.ibm.km.dto.KmCompanyWiseBillPlanDTO;
/**
* @author Anveeksha & Neeraj 
* created for Waiver Matrix Upload
* for viewing Company Wise bill Plan details
*/
public interface KmCompanyWiseBillPlanService {
	/**
	 * @param str
	 * @return ArrayList for companies as per user entered search value
	 * @throws KmException
	 */
	public ArrayList getCompanyList(String str) throws KmException;
	
	/**
	 * @param id
	 * @return ArrayList for bill plans of selected company   
	 * @throws KmException
	 */
	public ArrayList getBillPlanList(int id) throws KmException;
	
	/**
	 * @param id
	 * @return ArrayList for details of Company selected 
	 * @throws KmException
	 */
	public ArrayList getCompanyDetails(int id) throws KmException;
	
	/**
	 * @param planName
	 * @return ArralList for Rate Details of selected Bill Plan
	 * @throws KmException
	 */
	public ArrayList getBillPlanRateDetails(String planName) throws KmException;
}
